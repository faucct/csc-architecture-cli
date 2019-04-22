package faucct.cli

import InstructionsToInterpolate._
import LineParser.Scanners.Line
import org.scalatest.FunSuite

import scala.util.parsing.input.CharSequenceReader

class LineParserTest extends FunSuite {

  test("testApply") {
    val parser = LineParser(new CharSequenceReader("foo=bar\n"))
    assertResult(Line(List(Assignment("foo", List(Text.Raw("bar"))))))(parser.first)
  }

  test(">echo \"Hello, world!\"") {
    val parser = LineParser(new CharSequenceReader("echo \"Hello, world!\"\n"))
    assertResult(
      Line(List(Command(List(Text.Raw("echo "), Text.DoubleQuoted(List(Text.Raw("Hello, world!")))))))
    )(parser.first)
  }

  test("echo \"hello ' world\"") {
    val parser = LineParser(new CharSequenceReader("echo \"hello ' world\"\n"))
    assertResult(
      Line(List(Command(List(Text.Raw("echo "), Text.DoubleQuoted(List(Text.Raw("hello ' world")))))))
    )(parser.first)
  }

  test("echo $?") {
    val parser = LineParser(new CharSequenceReader("echo $?\n"))
    assertResult(
      Line(List(Command(List(Text.Raw("echo "), Text.Interpolated("?")))))
    )(parser.first)
  }

}
