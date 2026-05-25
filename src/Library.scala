package net.ivoah.flex

import java.io.File
import java.sql.ResultSet

case class Library(libraryId: Int, name: String, icon: String, path: File) {
  lazy val items: Seq[Movie] = Database.getMovies(libraryId)

  def scan(meta: MetadataExtractor[Movie]): Unit = {
    path.listFiles.flatMap { file =>
      file.getName match {
        case s"$title (${IntExtractor(year)})" => Some(title, year)
        case s"$title (${IntExtractor(year)}).$ext" => Some(title, year)
        case _ => None
      }
    }.filter {
      case (title, year) => !items.contains((m: Movie) => m.title == title && m.year == year)
    }.foreach {
      case (title, year) =>
        Database.saveMovie(libraryId, meta.hydrate(Movie(0, title, year)))
    }
  }
}

object Library {
  def fromResultSet(r: ResultSet): Library = Library(
    r.getInt("libraryId"),
    r.getString("name"),
    r.getString("icon"),
    r.getFile("path")
  )
}
