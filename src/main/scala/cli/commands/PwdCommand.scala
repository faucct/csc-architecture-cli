package cli.commands

import cli.Session

/**
  * Outputs current working directory.
  * @param args
  */
case class PwdCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    session.workingDirectory.getBytes.foreach(output)
    System.getProperty("line.separator").getBytes.foreach(output)
    0
  }
}
