package net.ivoah.flex

import scalatags.Text.all.*

import java.time.format.{DateTimeFormatter, FormatStyle}


object Templates {
  private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
  private def regularPolygon(size: (Int, Int), r: Int, n: Int): Frag = {
    import scalatags.Text.svgTags.{svg, polygon}
    import scalatags.Text.svgAttrs.{viewBox, fill, points}
    import math.{sin, cos, Pi}
    svg(viewBox:=s"0 0 ${size._1} ${size._2}",
      polygon(fill:="currentColor", points:=(0 to n).map{ i =>
        s"${size._1/2 + r*cos(i*2*Pi/n)},${size._2/2 + r*sin(i*2*Pi/n)}"
      }.mkString(" "))
    )
  }
  private val playButton = regularPolygon((256, 256), 64, 3)

  def root(movies: Seq[Movie]): String = doctype("html")(html(
    head(
      link(rel:="stylesheet", href:="/static/style.css"),
      tag("title")("Flex")
    ),
    body(
      div(cls:="movies",
        for (movie <- movies) yield {
          div(cls:="movie",
            div(cls:="poster",
              a(cls:="playButton", href:=s"${movie.url}/play", playButton),
              a(cls:="border", href:=movie.url),
              img(src:=movie.poster)
            ),
            a(href:=movie.url, title:=movie.name, movie.name),
            div(cls:="secondary", movie.releaseDate.format(dateFormat))
          )
        }
      )
    )
  )).render
}
