package net.ivoah.flex

import java.time.LocalDate
import java.io.File
import java.sql.ResultSet

case class Movie(
  movieId: Int,
  title: String,
  year: Int,
  releaseDate: Option[LocalDate] = None,
  poster: Option[String] = None,
  tmdbId: Option[Int] = None,
  added: Option[LocalDate] = None,
  files: Seq[File] = Seq()
) {
  val url: String = s"/movies/$title ($year)"
  // lazy val files: Seq[File] = Database.getFiles(movieId)
}

object Movie {
  val extensions: Seq[String] = Seq("mp4", "webm", "mkv", "avi", "mov")

  def fromResultSet(r: ResultSet): Movie = Movie(
    r.getInt("movieId"),
    r.getString("title"),
    r.getInt("year"),
    r.getLocalDateOption("releaseDate"),
    r.getStringOption("poster"),
    r.getIntOption("tmdbId"),
    r.getLocalDateOption("added")
  )
}
