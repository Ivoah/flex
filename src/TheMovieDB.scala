package net.ivoah.flex

import java.time.LocalDate

import upickle.Reader

given Reader[LocalDate] = upickle.reader[String].map(LocalDate.parse)

class TheMovieDB(token: String) extends MetadataExtractor[Movie] {
  private case class ImagesDetails(
    base_url: String,
    secure_base_url: String,
    backdrop_sizes: Seq[String],
    logo_sizes: Seq[String],
    poster_sizes: Seq[String],
    profile_sizes: Seq[String],
    still_sizes: Seq[String],
  ) derives Reader
  
  private case class ConfigurationDetails(images: ImagesDetails) derives Reader

  private case class MovieDetails(
    adult: Boolean,
    backdrop_path: String,
    genre_ids: Seq[Int],
    id: Int,
    original_language: String,
    original_title: String,
    overview: String,
    popularity: Double,
    poster_path: String,
    release_date: LocalDate,
    title: String,
    video: Boolean,
    vote_average: Double,
    vote_count: Int,
  ) derives Reader

  private case class PaginatedResults[T](page: Int, results: Seq[T]) derives Reader

  private val BASE_URL: String = "https://api.themoviedb.org/3"
  private val configurationDetails = api[ConfigurationDetails]("/configuration")

  private val posterSize = configurationDetails.images.poster_sizes.collect {
    case s"w${IntExtractor(width)}" if width >= 130 => width
  }.min

  private def api[T](endpoint: String, params: Map[String, String] = Map())(using Reader[T]): T = {
    val r = requests.get(
      s"$BASE_URL$endpoint",
      headers = Map(
        "Authorization" -> s"Bearer $token"
      ),
      params = params
    )
    upickle.read[T](r.text())
  }

  def hydrate(movie: Movie): Movie = {
    val searchResults = api[PaginatedResults[MovieDetails]]("/search/movie", Map("query" -> movie.title, "primary_release_year" -> movie.year.toString))
    val firstResult = searchResults.results.headOption
    movie.copy(
      releaseDate = firstResult.map(_.release_date),
      poster = firstResult.map(m => s"${configurationDetails.images.base_url}w$posterSize${m.poster_path}"),
      summary = firstResult.map(_.overview),
      backdrop = firstResult.map(m => s"${configurationDetails.images.base_url}original${m.poster_path}")
    )
  }
}
