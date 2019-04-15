package cli.commands

import java.io.{BufferedInputStream, File}

import cli.Session

case class ExternalCommand(name: String, args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    val process = Runtime.getRuntime.exec(
      Array(
        session.commandPathCache.get(name).orElse {
          val maybeFile =
            session.env.getOrElse("PATH", "").split(':').map(new File(_, name)).find(_.exists()).map(_.getAbsolutePath)
          maybeFile.foreach(file => session.commandPathCache += (name -> file))
          maybeFile
        } match {
          case Some(path) => path
          case None =>
            s"-cli: $name: command not found${System.getProperty("line.separator")}".getBytes.foreach(output)
            return 127
        }
      ) ++ args
    )
    if (input.isDefined)
      input.get(process.getOutputStream.write(_))
    process.getOutputStream.close()
    val buffer = new BufferedInputStream(process.getInputStream)
    var read: Int = buffer.read()
    while (read >= 0) {
      output(read.toByte)
      read = buffer.read()
    }
    process.waitFor()
  }
}
