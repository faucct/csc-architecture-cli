package cli

import java.io._

case class WcCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    if (args.isEmpty) {
      if (input.isEmpty) {
        println("No input stream")
        return 1
      }
      input.foreach(stream => {
        counts(
          stream,
          (lines, words, chars) =>
            s"$lines\t$words\t$chars${System.getProperty("line.separator")}".getBytes.foreach(output),
        )
      })
    }
    args.foreach(path => {
      try {
        counts(
          WithFileInput(new File(session.workingDirectory, path), _),
          (lines, words, chars) =>
            s"$lines\t$words\t$chars\t$path${System.getProperty("line.separator")}".getBytes().foreach(output)
        )
      } catch {
        case _: FileNotFoundException =>
          s"cat: $path: No such file or directory${System.getProperty("line.separator")}".getBytes.foreach(output)
          return 1
      }
    })
    0
  }

  private def counts(input: (Byte => Unit) => Unit, output: (Integer, Integer, Integer) => Unit) = {
    var lines = 0
    var words = 0
    var chars = 0
    var wasWord = false
    input.apply { char =>
      if (char == '\n')
        lines += 1
      if (!wasWord && !char.toChar.isWhitespace)
        words += 1
      chars += 1
      wasWord = char.toChar.isWhitespace
    }
    output(lines, words, chars)
  }
}
