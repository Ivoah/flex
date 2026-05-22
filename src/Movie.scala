package net.ivoah.flex

import java.time.LocalDate

case class Movie(name: String, releaseDate: LocalDate, poster: String) {
  val url: String = s"/movies/$name (${releaseDate.getYear})"
}

object Movie {

}
