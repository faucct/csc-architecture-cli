package faucct.cli

/**
  * An intermediate representation of user input between [[LineParser]] and [[InstructionInterpolator]]
  */
object InstructionsToInterpolate {
  sealed trait Instruction

  case class Assignment(key: String, value: List[Text]) extends Instruction

  case class Command(texts: List[Text]) extends Instruction

  sealed trait Text

  object Text {

    case class Raw(string: String) extends Text

    case class Quoted(string: String) extends Text

    case class DoubleQuoted(texts: List[Text]) extends Text

    case class Interpolated(key: String) extends Text

  }

  case class Pipe(from: Instruction, to: Instruction) extends Instruction
}
