package cli

import scala.collection.mutable

case class Session(
  var workingDirectory: String,
  env: mutable.Map[String, String],
  commandPathCache: mutable.Map[String, String],
)
