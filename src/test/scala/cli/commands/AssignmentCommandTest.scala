package cli.commands

import java.io.ByteArrayOutputStream

import cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class AssignmentCommandTest extends FunSuite {

  test("run") {
    val stream = new ByteArrayOutputStream()
    val session = this.session
    AssignmentCommand("foo", "bar").run(session, None, stream.write(_))
    assertResult("")(stream.toString)
    assertResult(Some("bar"))(session.env.get("foo"))
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }
}
