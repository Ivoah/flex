package net.ivoah.flex

object IntExtractor {
  def unapply(s: String): Option[Int] = s.toIntOption
}
