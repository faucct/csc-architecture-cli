package cli

case class ExitCommand(args: Array[String]) extends Command {
  override def run(session: Session, input: Option[(Byte => Unit) => Unit], output: Byte => Unit): Int = {
    val status: Int = args.length match {
      case 0 => 0
      case 1 =>
        try {
          args(0).toInt
        } catch {
          case _: NumberFormatException =>
            s"-cli: exit: $args(0): numeric argument required${System.getProperty("line.separator")}".getBytes
              .foreach(output)
            return 255
        }
      case _ =>
        s"-cli: exit: too many arguments${System.getProperty("line.separator")}".getBytes.foreach(output)
        return 1
    }

    if (input.isEmpty)
      System.exit(status)
    status
  }
}
