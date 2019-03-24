package cli

import java.io.{OutputStream, Reader}

import scala.util.parsing.input.{PagedSeq, PagedSeqReader}

// Reads and processes an unseparable batch of commands.
class InputProcessor(session: Session) {
  def apply(input: Reader, output: OutputStream): Unit = {
    LineParser(new PagedSeqReader(PagedSeq.fromReader(input))).first match {
      case LineParser.Scanners.ErrorToken(message) => println(message)
      case LineParser.Scanners.Line(instructions) =>
        instructions.foreach(instruction =>
          session.env.put(
            "?",
            new InstructionInterpolator(session.env).apply(instruction).run(session, None, output.write(_)).toString,
          )
        )
    }
  }
}
