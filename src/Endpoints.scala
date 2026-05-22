package net.ivoah.flex

import net.ivoah.vial.*

import java.nio.file.Paths
import java.time.LocalDate

class Endpoints(debug: Boolean = false) {
  def router: Router = Router {
    case (_, _, _, e) if debug =>
      e.printStackTrace()
      Response.InternalServerError(e)

    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get("static"), Paths.get(file))

    case ("GET", "/", _) =>

      Response(Templates.root(Seq(
        Movie("Castle in the Sky", LocalDate.of(1986, 8, 2), "https://m.media-amazon.com/images/M/MV5BZjcyMjg2MzktNjg4YS00MjQzLTg0YWQtMjUyZDk2Y2Y0YzZjXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg"),
        Movie("Howl's Moving Castle", LocalDate.of(2004, 11, 20), "https://m.media-amazon.com/images/M/MV5BMTY1OTg0MjE3MV5BMl5BanBnXkFtZTcwNTUxMTkyMQ@@._V1_FMjpg_UY2340_.jpg"),
        Movie("Kiki's Delivery Service", LocalDate.of(1989, 7, 29), "https://m.media-amazon.com/images/M/MV5BOTFhYWI1NGUtZWFhZS00MTdkLWIzZTItMDBhNWNiZDNlMjYyXkEyXkFqcGc@._V1_FMjpg_UX986_.jpg"),
        Movie("The Secret Life of Walter Mitty", LocalDate.of(2013, 12, 19), "https://m.media-amazon.com/images/M/MV5BODYwNDYxNDk1Nl5BMl5BanBnXkFtZTgwOTAwMTk2MDE@._V1_FMjpg_UY2048_.jpg")
      )))
  }
}
