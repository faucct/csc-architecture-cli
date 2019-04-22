package faucct.cli

import java.io.{ByteArrayOutputStream, StringReader}

import org.scalatest.FunSuite

import scala.collection.mutable

class InputProcessorTest extends FunSuite {

  test("testApply") {
    val inputProcessor = new InputProcessor(
      new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
        override def exit(status: Integer): Unit = {}
      }
    )
    val output = new ByteArrayOutputStream()
    inputProcessor.apply(new StringReader(s"exit${System.getProperty("line.separator")}"), output)

  }

}
