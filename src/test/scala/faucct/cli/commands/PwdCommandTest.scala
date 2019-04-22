package faucct.cli.commands

import java.io.ByteArrayOutputStream

import faucct.cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class PwdCommandTest extends FunSuite {

  test("run") {
    val stream = new ByteArrayOutputStream()
    PwdCommand(Array()).run(session, None, stream.write(_))
    assertResult(s"${System.getProperty("user.dir")}${System.getProperty("line.separator")}")(stream.toString)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }

}
