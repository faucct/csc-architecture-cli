package cli.commands

import java.io.{BufferedInputStream, File, FileInputStream, FileNotFoundException}

import cli.Session

import scala.util.control.NonFatal

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
        withResource(new BufferedInputStream(new FileInputStream(new File(session.workingDirectory, path))))(stream => {
          var read: Int = stream.read
          while (read >= 0) {
            output(read.toByte)
            read = stream.read()
          }
        })
      } catch {
        case _: FileNotFoundException =>
          s"cat: $path: No such file or directory${System.getProperty("line.separator")}".getBytes.foreach(output)
          return 1
      }
    })
    0
  }

  // @see https://medium.com/@dkomanov/scala-try-with-resources-735baad0fd7d
  private def withResource[T <: AutoCloseable, V](r: => T)(f: T => V): V = {
    val resource: T = r
    require(resource != null, "resource is null")
    var exception: Throwable = null
    try {
      f(resource)
    } catch {
      case NonFatal(e) =>
        exception = e
        throw e
    } finally {
      closeAndAddSuppressed(exception, resource)
    }
  }

  private def closeAndAddSuppressed(e: Throwable, resource: AutoCloseable): Unit = {
    if (e != null) {
      try {
        resource.close()
      } catch {
        case NonFatal(suppressed) =>
          e.addSuppressed(suppressed)
      }
    } else {
      resource.close()
    }
  }
}
