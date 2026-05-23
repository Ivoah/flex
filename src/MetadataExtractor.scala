package net.ivoah.flex

trait MetadataExtractor[T] {
  def hydrate(item: T): T
}
