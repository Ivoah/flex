package net.ivoah.flex

import java.io.File

object Database {
  given Connector = Connector("jdbc:sqlite:library.sqlite")

  def getLibraries(): Seq[Library] = {
    sql"""
      select *
      from library
    """.query(Library.fromResultSet)
  }

  def getMovies(libraryId: Int): Seq[Movie] = {
    sql"""
      select *
      from movie
      where libraryId=$libraryId
    """.query(Movie.fromResultSet)
  }

  def getFiles(movieId: Int): Seq[File] = {
    sql"""
      select path
      from file
      where movieId=$movieId
    """.query(_.getFile("path"))
  }

  def saveMovie(libraryId: Int, movie: Movie): Movie = {
    val movieId = sql"""
      insert into movie (libraryId, title, year, releaseDate, poster, tmdbId, added)
      values ($libraryId, ${movie.title}, ${movie.year}, ${movie.releaseDate.orNull}, ${movie.poster.orNull}, ${movie.tmdbId.orNull}, date())
    """.updateGetKey()
    movie.copy(movieId = movieId)
  }
}
