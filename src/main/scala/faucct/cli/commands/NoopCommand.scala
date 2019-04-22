package faucct.cli.commands

import faucct.cli.Session

case object NoopCommand extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    0
  }
}
