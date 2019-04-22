package faucct.cli.commands

import faucct.cli.Session

/**
  * Is not an independent command but is used to pipe input from one to another.
  * @param from
  * @param to
  */
case class PipeCommand(from: Command, to: Command) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    to.run(session, Some(from.run(session, input, _)), output)
  }
}
