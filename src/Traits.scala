package net.ivoah.flex

import java.io.File

trait MetadataExtractor[T] {
  def hydrate(item: T): T
}

trait MediaItem {
  def fromFile(path: File): this.type
}
