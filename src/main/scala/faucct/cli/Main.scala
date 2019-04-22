package faucct.cli

import java.io.InputStreamReader

import scala.collection.mutable
import scala.util.parsing.input.{PagedSeq, PagedSeqReader}

/**
  * Main loop.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val inputProcessor = new InputProcessor(
      new Session(System.getProperty("user.dir"), mutable.Map(sys.env.toSeq: _*), mutable.Map.empty)
    )
    while (true) {
      print("CLI$ ")
      inputProcessor(new InputStreamReader(System.in), System.out)
    }
  }
}
