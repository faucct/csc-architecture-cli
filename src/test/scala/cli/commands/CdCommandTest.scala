package cli.commands

import java.io.{ByteArrayOutputStream, File}

import cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class CdCommandTest extends FunSuite {

  test("run without args") {
    val stream = new ByteArrayOutputStream()
    val session = this.session
    CdCommand(Array()).run(session, None, stream.write(_))
    assertResult("")(stream.toString)
    assertResult(System.getProperty("user.home"))(session.workingDirectory)
  }

  test("run with args") {
    val stream = new ByteArrayOutputStream()
    val session = this.session
    CdCommand(Array("src/test")).run(session, None, stream.write(_))
    assertResult("")(stream.toString)
    assertResult(new File(System.getProperty("user.dir"), "src/test").getCanonicalPath)(session.workingDirectory)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }
}
