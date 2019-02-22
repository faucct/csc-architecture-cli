package cli

import java.io.ByteArrayOutputStream

import org.scalatest.FunSuite

import scala.collection.mutable

class PipeCommandTest extends FunSuite {

  test("testRun") {
    val stream = new ByteArrayOutputStream()
    PipeCommand(EchoCommand(Array("123")), WcCommand(Array()))
      .run(Session("", mutable.Map(), mutable.Map()), None, stream.write(_))
    assertResult("1\t3\t4\n")(stream.toString)
  }

}
