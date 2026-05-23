package net.ivoah.flex

import net.ivoah.vial.*

import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

class Endpoints(libraryPath: File, debug: Boolean = false) {
  private val meta = TheMovieDB(Config.tmdb.token)
  private val library = libraryPath.list.collect {
    case s"$title (${IntExtractor(year)})" => meta.hydrate(Movie(title, year))
  }

  def router: Router = Router {
    case (_, _, _, e) if debug =>
      e.printStackTrace()
      Response.InternalServerError(e)

    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get("static"), Paths.get(file))

    case ("GET", "/", _) => Response(Templates.root(library))
  }
}
