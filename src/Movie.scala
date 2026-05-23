package net.ivoah.flex

import java.time.LocalDate

case class Movie(title: String, year: Int, releaseDate: Option[LocalDate] = None, poster: Option[String] = None) {
  val url: String = s"/movies/$title ($year)"
}

object Movie {

}
