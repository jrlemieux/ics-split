package cc.lemieux.ics_split

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

object Util {

  val CR = 13.toChar
  val LF = 10.toChar
  val CRLF = s"$CR$LF"

  def removeCR(line: String): String = if (line.length > 0 && line(line.length - 1) == CR) line.dropRight(1) else line

  def saveLinesWithCRLF(fileName: String, lines: Seq[String]): Unit = {
    val sb = new StringBuilder()
    lines.foreach(line => sb ++= line + CRLF)
    val bytes = sb.toString().getBytes(StandardCharsets.UTF_8)
    Files.write(Paths.get(fileName), bytes)
  }

  def run(code: => Unit): Unit =
    try
      code
    catch {
      case e: Exception =>
        val exceptionName = e.getClass().getSimpleName()
        val exceptionMessage = Option(e.getMessage()).getOrElse("<no message>")
        System.err.println(s"Error: $exceptionName - $exceptionMessage")
        System.exit(1)
    }
}
