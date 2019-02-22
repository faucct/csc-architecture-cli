package cli

import java.io.InputStreamReader

import scala.collection.mutable
import scala.util.parsing.input.{PagedSeq, PagedSeqReader}

object Application {
  def main(args: Array[String]): Unit = {
    val session = Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty)
    while (true) {
      print("CLI$ ")
      LineParser(new PagedSeqReader(PagedSeq.fromReader(new InputStreamReader(System.in)))).first match {
        case LineParser.Scanners.ErrorToken(message) => println(message)
        case LineParser.Scanners.Line(instructions) =>
          instructions.foreach(new InstructionInterpolator(session.env).apply(_).run(session, None, System.out.write(_)))
      }
    }
  }
}
