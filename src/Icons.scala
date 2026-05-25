package net.ivoah.flex

import scalatags.Text.all.{Frag, raw}
import java.nio.file.{Paths, Files}

object Icons {
  private val ROOT = Paths.get("static/icons")
  def get(icon: String): Frag = raw(Files.readString(ROOT.resolve(s"$icon.svg")))
}
