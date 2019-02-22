package cli

case class PipeCommand(from: Command, to: Command) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    to.run(session, Some(from.run(session, input, _)), output)
  }
}
