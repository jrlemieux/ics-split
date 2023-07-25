package cc.lemieux.ics_split

import java.io.File

case class Main(args: Array[String]) {

  val (fileName, nbMaxBytesPerFile) = args match {
    case Array(fn, max) => (fn, max.toInt)
    case _ => throw new RuntimeException(s"Invalid arguments.\nUsage: ics-split <ics file name> <maximum size per file>")
  }

  val fileSize = new File(fileName).length()
  val splitCount = ((fileSize / nbMaxBytesPerFile) max 1).toInt

  println(s"Processing ICS file $fileName with $fileSize bytes ($splitCount output files).")

  val outputFileName = if (fileName.endsWith(".ics")) fileName.dropRight(4) else fileName

  def tell(fname: String, nbEvents: Int) = println(s"File $fname: $nbEvents events.")

  val source = Source(fileName)
  val events = source.getEvents()
  tell(fileName, events.length)
  val nbEventsPerFile = events.length / splitCount
  events.grouped(nbEventsPerFile).zipWithIndex.foreach { case (group, index) =>
    val lines = source.headerLines ++ group.flatMap(_.lines) ++ Seq(Source.fileEndMarker)
    val ofname = s"$outputFileName-${index + 1}.ics"
    tell(ofname, group.length)
    Util.saveLinesWithCRLF(ofname, lines)
  }
}

object Main {
  def main(args: Array[String]): Unit = Util.run(Main(args))
}