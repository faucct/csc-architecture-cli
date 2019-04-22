package cli.commands

import cli.Session

/**
  * Outputs its args to STDIN.
  * @param args
  */
case class EchoCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    args.mkString(" ").getBytes.foreach(output)
    System.getProperty("line.separator").getBytes.foreach(output)
    0
  }
}
