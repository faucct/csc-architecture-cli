package cli.commands

import cli.Session

/**
  *
  * Changes environment variable.
  * @example
  *          $ FOO=bar
  *          $ echo $foo
  *          bar
  *
  * @param key
  * @param value
  */
case class AssignmentCommand(key: String, value: String) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    session.env += {
      key -> value
    }
    0
  }
}
