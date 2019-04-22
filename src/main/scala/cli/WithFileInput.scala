package cli

import java.io.{BufferedInputStream, File, FileInputStream}

import scala.util.control.NonFatal

/**
  * Encapsulates reading from files.
  */
object WithFileInput {
  def apply(file: File, output: Byte => Unit) {
    withResource(new BufferedInputStream(new FileInputStream(file)))(stream => {
      var read: Int = stream.read
      while (read >= 0) {
        output(read.toByte)
        read = stream.read()
      }
    })
  }

  /**
    * @see https://medium.com/@dkomanov/scala-try-with-resources-735baad0fd7d
    */
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
