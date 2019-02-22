package cli

import cli.InstructionsToInterpolate._
import cli.LineParser.Scanners.Line
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

}
