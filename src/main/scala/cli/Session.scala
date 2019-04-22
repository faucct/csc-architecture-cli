package cli

import scala.collection.mutable

/**
  * A storage.
  */
class Session(
  var workingDirectory: String,
  val env: mutable.Map[String, String],
  val commandPathCache: mutable.Map[String, String],
) {
  def exit(status: Integer): Unit = System.exit(status)
}
