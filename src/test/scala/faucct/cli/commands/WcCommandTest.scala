package faucct.cli.commands

import java.io.ByteArrayOutputStream

import faucct.cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class WcCommandTest extends FunSuite {

  test("run with file") {
    val stream = new ByteArrayOutputStream()
    WcCommand(Array("src/test/resources/foo.txt")).run(session, None, stream.write(_))
    assertResult(s"2\t5\t8\tsrc/test/resources/foo.txt${System.getProperty("line.separator")}")(stream.toString)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }
}
