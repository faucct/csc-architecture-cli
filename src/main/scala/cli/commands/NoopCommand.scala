package cli.commands

import cli.Session

case object NoopCommand extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    0
  }
}
