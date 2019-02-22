package cli

trait Command {
  def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int
}
