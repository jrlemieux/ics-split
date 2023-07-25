package cc.lemieux.ics_split

import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer

object Source {
  val eventStartMarker = "BEGIN:VEVENT"
  val eventEndMarker = "END:VEVENT"
  val fileEndMarker = "END:VCALENDAR"
}

import cc.lemieux.ics_split.Source._

case class Source(fileName: String) {

  private val iterator = scala.io.Source.fromFile(fileName)(StandardCharsets.UTF_8).getLines().map(Util.removeCR)

  private val (headerIterator, restIterator) = iterator.span(_ != eventStartMarker)

  val headerLines = headerIterator.toSeq

  private var optionalCurrentLine: Option[String] = None

  private def getNext() =
    optionalCurrentLine =
      if (restIterator.isEmpty)
        None
      else
        Some(restIterator.next())

  private def eat() =
    optionalCurrentLine match {
      case None =>
        throw new RuntimeException("eat() called while at EOF.")
      case Some(line) =>
        getNext()
        line
    }

  // Look-ahead.
  getNext()

  if (optionalCurrentLine.isEmpty)
    throw new Exception("No data.")

  def getEvents(): Seq[Event] = {
    val events = ListBuffer[Event]()
    while (optionalCurrentLine == Some(eventStartMarker)) {
      val buffer = ListBuffer[String]()
      var done = false
      while (!done) {
        val line = eat()
        buffer += line
        done = line == Source.eventEndMarker
      }
      events += Event(buffer.toSeq)
    }
    if (optionalCurrentLine != Some(fileEndMarker))
      throw new RuntimeException(s"Not end of calendar file, found '$optionalCurrentLine' instead of '$fileEndMarker'.")
    events.toSeq
  }
}
