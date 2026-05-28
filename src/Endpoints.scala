package net.ivoah.flex

import net.ivoah.vial.*

import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

class Endpoints(debug: Boolean = false) {
  private val tmdb = TheMovieDB(Config.tmdb.token)
  // private val library = Library.scan(libraryPath, tmdb)

  def router: Router = Router {
    case (_, _, _, e) if debug =>
      e.printStackTrace()
      Response.InternalServerError(e)

    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get("static"), Paths.get(file))

    case ("GET", "/", _) =>
      Response.Redirect(s"/${Database.getLibraries().head.name}")

    case ("GET", s"/$library/$movie (${IntExtractor(year)})", _) =>
      val allLibraries = Database.getLibraries()
      allLibraries.find(_.name == library).flatMap { l =>
        l.items.find(m => m.title == movie && m.year == year).map { m =>
          Response(Templates.movie(allLibraries, m))
        }
      }.getOrElse(Response.NotFound())

    case ("POST", s"/scan/$library", _) =>
      val allLibraries = Database.getLibraries()
      allLibraries.find(_.name == library)
        .map { l =>
          l.scan(tmdb)
          Response.Redirect(s"/$library")
        }
        .getOrElse(Response.NotFound())

    case ("GET", s"/$library", _) =>
      val allLibraries = Database.getLibraries()
      allLibraries.find(_.name == library)
        .map(l => Response(Templates.library(allLibraries, l)))
        .getOrElse(Response.NotFound())
  }
}
