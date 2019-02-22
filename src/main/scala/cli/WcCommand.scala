package cli

import java.io._

import scala.util.control.NonFatal

case class WcCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    if (args.isEmpty) {
      if (input.isEmpty) {
        println("No input stream")
        return 1
      }
      input.foreach(stream => {
        val (lines, words, chars) = counts(stream)
        s"$lines\t$words\t$chars${System.getProperty("line.separator")}".getBytes.foreach(output)
      })
    }
    args.foreach(path => {
      try {
        withResource(new BufferedInputStream(new FileInputStream(new File(session.workingDirectory, path))))(stream => {
          val (lines, words, chars) = counts(write => {
            var read: Int = stream.read
            while (read >= 0) {
              write(read.toByte)
              read = stream.read()
            }
          })
          s"$lines\t$words\t$chars\t$path${System.getProperty("line.separator")}".getBytes().foreach(output)
        })
      } catch {
        case _: FileNotFoundException =>
          s"cat: $path: No such file or directory${System.getProperty("line.separator")}".getBytes.foreach(output)
          return 1
      }
    })
    0
  }

  private def counts(input: (Byte => Unit) => Unit): (Integer, Integer, Integer) = {
    var lines = 0
    var words = 0
    var chars = 0
    var wasWord = false
    input.apply { char =>
      if (char == '\n')
        lines += 1
      if (!wasWord && !char.toChar.isWhitespace)
        words += 1
      chars += 1
      wasWord = char.toChar.isWhitespace
    }
    (lines, words, chars)
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
