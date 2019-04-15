package cli.commands

import java.io.ByteArrayOutputStream

import cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class ExitCommandTest extends FunSuite {

  test("run without arguments") {
    val stream = new ByteArrayOutputStream()
    ExitCommand(Array()).run(session, None, stream.write(_))
    assertResult("")(stream.toString)
  }

  test("run with an argument") {
    val stream = new ByteArrayOutputStream()
    ExitCommand(Array("1")).run(session, None, stream.write(_))
    assertResult("")(stream.toString)
  }

  test("run with too much arguments") {
    val stream = new ByteArrayOutputStream()
    ExitCommand(Array("1", "2")).run(session, None, stream.write(_))
    assertResult(s"-cli: exit: too many arguments${System.getProperty("line.separator")}")(stream.toString)
  }

  test("run without a numeric argument") {
    val stream = new ByteArrayOutputStream()
    ExitCommand(Array("foo")).run(session, None, stream.write(_))
    assertResult(s"-cli: exit: foo: numeric argument required${System.getProperty("line.separator")}")(stream.toString)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }
}
