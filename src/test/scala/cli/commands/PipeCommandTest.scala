package cli.commands

import java.io.ByteArrayOutputStream

import cli.Session
import org.scalatest.FunSuite

import scala.collection.mutable

class PipeCommandTest extends FunSuite {

  test("testRun") {
    val stream = new ByteArrayOutputStream()
    PipeCommand(EchoCommand(Array("123")), WcCommand(Array()))
      .run(new Session("", mutable.Map(), mutable.Map()), None, stream.write(_))
    assertResult(s"1\t3\t4${System.getProperty("line.separator")}")(stream.toString)
  }

}
