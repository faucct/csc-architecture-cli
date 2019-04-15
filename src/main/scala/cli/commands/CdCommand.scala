package cli.commands

import java.io.File
import java.nio.file.Paths

import cli.Session

case class CdCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    session.workingDirectory =
      if (args.isEmpty)
        System.getProperty("user.home")
      else if (Paths.get(args(0)).isAbsolute)
        args(0)
      else
        new File(session.workingDirectory, args(0)).getCanonicalPath
    0
  }
}
