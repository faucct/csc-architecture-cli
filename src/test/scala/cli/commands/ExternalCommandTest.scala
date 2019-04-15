package cli.commands

import java.io.ByteArrayOutputStream

import cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class ExternalCommandTest extends FunSuite {

  test("run ls with dir") {
    val stream = new ByteArrayOutputStream()
    ExternalCommand("ls", Array("src/test/resources")).run(session, None, stream.write(_))
    assertResult(s"foo.txt${System.getProperty("line.separator")}")(stream.toString)
  }

  test("run unknown program") {
    val stream = new ByteArrayOutputStream()
    ExternalCommand("foo", Array()).run(session, None, stream.write(_))
    assertResult(s"-cli: foo: command not found${System.getProperty("line.separator")}")(stream.toString)
  }

  test("run with input") {
    val stream = new ByteArrayOutputStream()
    ExternalCommand("cat", Array()).run(session, Some(s"foo${System.getProperty("line.separator")}".getBytes.foreach(_)), stream.write(_))
    assertResult(s"foo${System.getProperty("line.separator")}")(stream.toString)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }
}
