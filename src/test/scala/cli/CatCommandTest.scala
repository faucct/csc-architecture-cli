package cli

import java.io.ByteArrayOutputStream

import org.scalatest.FunSuite

import scala.collection.mutable

class CatCommandTest extends FunSuite {

  test("run without args") {
    val stream = new ByteArrayOutputStream()
    CatCommand(Array()).run(session, Some(s"foo${System.getProperty("line.separator")}".getBytes.foreach(_)), stream.write(_))
    assertResult(s"foo${System.getProperty("line.separator")}")(stream.toString)
  }

  test("run with args") {
    val stream = new ByteArrayOutputStream()
    CatCommand(Array("src/test/resources/foo.txt")).run(session, None, stream.write(_))
    assertResult(s"foo${System.getProperty("line.separator")}bar${System.getProperty("line.separator")}")(stream.toString)
  }

  test("run with unknown file") {
    val stream = new ByteArrayOutputStream()
    CatCommand(Array("foo.txt")).run(session, None, stream.write(_))
    assertResult(s"cat: foo.txt: No such file or directory${System.getProperty("line.separator")}")(stream.toString)
  }

  private def session = {
    new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty) {
      override def exit(status: Integer): Unit = {}
    }
  }

}
