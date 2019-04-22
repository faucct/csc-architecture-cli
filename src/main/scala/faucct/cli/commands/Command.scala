package faucct.cli.commands

import faucct.cli.Session

/**
  * Runnable command interface.
  */
trait Command {
  /**
    * @param session Contains env and working directory. Should be used to exit.
    * @param input A stream of bytes. Run with a consumer to process them.
    * @param output Bytes should be fed to it.
    * @return Exit code.
    */
  def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int
}
