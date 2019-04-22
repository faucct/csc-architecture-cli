package faucct.cli

import scala.util.parsing.combinator._
import scala.util.parsing.input.Reader
import InstructionsToInterpolate._

// Is responsible for parsing user input.
// Does not perform interpolation.
object LineParser {
  def apply(reader: Reader[Char]) = new Scanners.Scanner(reader)

  object Scanners extends lexical.Lexical with token.Tokens {

    case class Line(instructions: List[Instruction]) extends Token {
      override def chars: String = ???
    }

    override def token: Parser[Token] = repsep(pipe | assignment | command, ';') <~ '\n' ^^ Line

    private def assignment = {
      identifier ~ '=' ~ rep(text) <~ guard(separator) ^^ {
        case identifier ~ '=' ~ text => Assignment(identifier, text)
      }
    }

    private def separator = {
      accept(';') | '\n'
    }

    private def identifier = {
      '?' ^^ (_.toString) | letter ~ rep(letter | digit) <~ guard(not(letter | digit)) ^^ {
        case letter ~ rest => letter + rest.mkString
      }
    }

    private def command = rep(text) ^^ Command

    private def text: Parser[Text] = quoted | doubleQuoted | interpolated | raw

    private def quoted =
      ''' ~> rep(acceptIf(_ != ''')(_ => "")) <~ ''' ^^ (chars => Text.Quoted(chars.mkString))

    private def doubleQuoted = '"' ~> rep(interpolated | doubleQuotedRaw) <~ '"' ^^ Text.DoubleQuoted

    private def interpolated = '$' ~> identifier ^^ Text.Interpolated

    private def raw =
      rep1(acceptIf({ case ''' | ';' | '|' | '"' | '\n' | '$' => false; case _ => true })(_ => "")) ^^
        (chars => Text.Raw(chars.mkString))

    private def doubleQuotedRaw =
      rep1(acceptIf({ case '"' | '$' => false; case _ => true })(_ => "")) ^^
        (chars => Text.Raw(chars.mkString))

    private def pipe =
      command ~ '|' ~ command ^^ { case from ~ _ ~ to => Pipe(from, to) }

    override def whitespace: Parser[Any] = success(Unit)
  }

}
