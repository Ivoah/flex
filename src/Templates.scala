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

  def library(allLibraries: Seq[Library], currentLibrary: Library): String = doctype("html")(html(
    head(
      link(rel:="icon", href:="/static/icon.svg"),
      link(rel:="stylesheet", href:="/static/style.css"),
      script(src:="/static/jquery-4.0.0.min.js"),
      tag("title")("Flex")
    ),
    body(
      div(cls:="header",
        "Flex"
      ),
      div(cls:="sidebar",
        for (library <- allLibraries) yield div(cls:="library",
          a(cls:=s"libraryLink clickable ${if (library == currentLibrary) "orange" else ""}", href:=s"/${library.name}", Icons.get(library.icon), library.name),
          div(cls:="spacer"),
          form(method:="POST", action:=s"/scan/${library.name}",
            button(cls:="scan clickable", Icons.get("refresh")),
            // language=JavaScript
            script(raw("""
              $(document.currentScript).parent().on("submit", e => {
                e.preventDefault();
                const form = $(e.target);
                $.ajax(form.prop("action"), {
                  method: "POST",
                  data: form.serialize(),
                  success: () => location.reload()
                });
                $(e.target).children("button").addClass("spinning");
              });
            """))
          )
        )
      ),
      div(cls:="movies",
        for (movie <- currentLibrary.items.sortBy(_.releaseDate).reverse) yield {
          div(cls:="movie",
            div(cls:="poster",
              a(cls:="playButton", href:=s"${movie.url}/play", playButton),
              a(cls:="border", href:=movie.url),
              img(src:=movie.poster.getOrElse(""))
            ),
            a(href:=movie.url, title:=movie.title, movie.title),
            div(cls:="secondary", movie.releaseDate.map(dateFormat.format).getOrElse("Unknown"))
          )
        }
      )
    )
  )).render
}
