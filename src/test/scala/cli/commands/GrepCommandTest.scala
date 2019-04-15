package cli.commands

import java.io.ByteArrayOutputStream

import org.scalatest.FunSuite

import scala.collection.mutable

import cli.Session

class GrepCommandTest extends FunSuite {

  test("testWord") {
    val command = new GrepCommand(Array("-w", "foo"), System.err)
    val session = new Session(null, mutable.Map.empty, mutable.Map.empty)
    val builder = new ByteArrayOutputStream()
    assertResult(0)(command.run(session, Some(output => "food\nfoo\n".getBytes.foreach(output)), builder.write(_)))
    assertResult("foo\n")(builder.toString)
  }

  test("testCaseInsensitive") {
    val command = new GrepCommand(Array("-i", "foo"), System.err)
    val session = new Session(null, mutable.Map.empty, mutable.Map.empty)
    val builder = new ByteArrayOutputStream()
    assertResult(0)(command.run(session, Some(output => "FOO\n".getBytes.foreach(output)), builder.write(_)))
    assertResult("FOO\n")(builder.toString)
  }

  test("testAfterContext") {
    val command = new GrepCommand(Array("-A", "1", "foo"), System.err)
    val session = new Session(null, mutable.Map.empty, mutable.Map.empty)
    val builder = new ByteArrayOutputStream()
    assertResult(0)(command.run(session, Some(output => "foo\nbar\n".getBytes.foreach(output)), builder.write(_)))
    assertResult("foo\nbar\n")(builder.toString)
  }

}
