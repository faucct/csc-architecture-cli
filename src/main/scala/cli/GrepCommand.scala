package cli

import java.io._
import java.util.regex.Pattern

case class GrepCommand(args: Array[String], errorOutput: PrintStream) extends Command {

  private case class Options(
    patterns: List[Pattern] = List.empty,
    afterContext: Integer = 0,
    ignoreCase: Boolean = false,
    wordRegexp: Boolean = false,
  )

  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    val usage = "usage: grep [-iw] [-A num] [pattern] [file ...]"
    val (files, rawOptions) =
      parseArgs(args.toList) match {
        case None =>
          errorOutput.println(usage)
          return 2
        case Some((Nil, options)) if options.patterns.isEmpty =>
          errorOutput.println(usage)
          return 2
        case Some((pattern :: files, options)) =>
          (files, options.copy(patterns = Pattern.compile(pattern) :: options.patterns))
        case Some(filesWithOptions) => filesWithOptions
      }
    val options =
      rawOptions.copy(patterns = rawOptions.patterns.map(pattern => Pattern.compile(
        if (rawOptions.wordRegexp) s"(?<!\\B)${pattern.pattern()}(?!\\B)" else pattern.pattern(),
        if (rawOptions.ignoreCase) Pattern.CASE_INSENSITIVE else 0
      )))

    if (files.isEmpty) {
      if (input.isEmpty) {
        errorOutput.println("No input stream")
        return 1
      }
      input.foreach(stream => {
        scan(options, stream, _.getBytes.foreach(output))
      })
    }
    files.foreach(path => {
      try {
        scan(options, WithFileInput(new File(session.workingDirectory, path), _), _.getBytes.foreach(output))
      } catch {
        case _: FileNotFoundException =>
          errorOutput.println(s"cat: $path: No such file or directory${System.getProperty("line.separator")}")
          return 1
      }
    })
    0
  }

  private def scan(options: Options, input: (Byte => Unit) => Unit, output: String => Unit) {
    var afterCounter = 0
    val builder = new ByteArrayOutputStream()
    val outputLine = (line: String) => {
      if (options.patterns.exists(pattern => pattern.matcher(line).find())) {
        afterCounter = options.afterContext
        output(line)
      } else if (afterCounter > 0) {
        afterCounter -= 1
        output(line)
      }
    }
    input.apply { char =>
      builder.write(char.toChar)
      if (char == '\n') {
        outputLine(builder.toString())
        builder.reset()
      }
    }
    outputLine(builder.toString())
  }

  private def parseArgs(args: List[String]): Option[(List[String], Options)] =
    args match {
      case "-i" :: rest => parseArgs(rest).map(pair => pair.copy(_2 = pair._2.copy(ignoreCase = true)))
      case "-A" :: numberString :: rest =>
        (try {
          Some(Integer.parseInt(numberString))
        } catch {
          case _: NumberFormatException =>
            errorOutput.println("grep: option requires an integer argument -- A")
            None
        }).flatMap {
          number => parseArgs(rest).map(pair => pair.copy(_2 = pair._2.copy(afterContext = number)))
        }
      case "-A" :: _ =>
        errorOutput.println("grep: option requires an argument -- A")
        None
      case "-w" :: rest => parseArgs(rest).map(pair => pair.copy(_2 = pair._2.copy(wordRegexp = true)))
      case arg :: rest => parseArgs(rest).map(pair => pair.copy(_1 = arg :: pair._1))
      case Nil => Some((List.empty, Options()))
    }
}
