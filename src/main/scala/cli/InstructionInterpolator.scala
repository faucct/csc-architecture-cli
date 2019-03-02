package cli

import InstructionsToInterpolate._

class InstructionInterpolator(env: scala.collection.Map[String, String]) {
  private val interpolate: PartialFunction[Text, List[String]] = {
    case Text.Raw(text) => words(text)
    case Text.DoubleQuoted(quotedTexts) => quotedTexts.flatMap(interpolate)
    case Text.Interpolated(key) => words(env.getOrElse(key, ""))
    case Text.Quoted(text) => List(text)
  }

  private def words(text: String) = text.split(' ').toList.filterNot(_.isEmpty)

  def apply: PartialFunction[Instruction, Command] = {
    case Assignment(key, texts) => AssignmentCommand(key, texts.flatMap(interpolate).mkString)
    case Command(texts) =>
      texts.flatMap(interpolate) match {
        case "cat" :: args => CatCommand(args.toArray)
        case "cd" :: args => CdCommand(args.toArray)
        case "echo" :: args => EchoCommand(args.toArray)
        case "exit" :: args => ExitCommand(args.toArray)
        case "pwd" :: args => PwdCommand(args.toArray)
        case "wc" :: args => WcCommand(args.toArray)
        case "grep" :: args => GrepCommand(args.toArray, System.err)
        case name :: args => ExternalCommand(name, args.toArray)
        case _ => NoopCommand
      }
    case Pipe(from, to) => PipeCommand(apply(from), apply(to))
  }
}
