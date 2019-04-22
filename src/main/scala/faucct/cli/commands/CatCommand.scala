package faucct.cli.commands

import java.io._

import faucct.cli.{Session, WithFileInput}

/**
  * Concats output from argument files. Reads from STDIN if there are none.
  * @param args files to read from
  */
case class CatCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    if (args.isEmpty) {
      if (input.isEmpty) {
        println("No input stream")
        return 1
      }
      input.foreach(_ (output))
    }
    args.foreach(path => {
      try {
        WithFileInput(new File(session.workingDirectory, path), output)
      } catch {
        case _: FileNotFoundException =>
          s"cat: $path: No such file or directory${System.getProperty("line.separator")}".getBytes.foreach(output)
          return 1
      }
    })
    0
  }
}
