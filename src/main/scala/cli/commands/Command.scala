package cli.commands

import cli.Session

trait Command {
  def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int
}
