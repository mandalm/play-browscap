package models

import collection.mutable
import collection.immutable

trait GjkVersion {
  def getInput() : scala.xml.Elem
  private lazy val attrs = XmlUtils.extract_items(getInput() \ "gjk_browscap_version" head)
  lazy val version = attrs("Version")
  lazy val released = attrs("Released")
}

class BrowsCap(val input : scala.xml.Elem) {
  lazy val comments = input \ "comments" \ "comment" map(_.text) reduceLeft(_ + "\n" + _)
  private var default : Option[BrowsCapItem] = None
  def convert(node: scala.xml.Node) : BrowsCapItem = {
    val item = BrowsCapItem(node, default)
    if (default.isEmpty) default = Some(item)
    item
  }
  lazy val items = input \ "browsercapitems" \ "browscapitem" map(convert)
}

class GjkBrowsCap(override val input : scala.xml.Elem) extends BrowsCap(input) with GjkVersion {
  def getInput() = input
}

object BrowsCap {
  def apply(xmlInput: scala.xml.Elem) = new GjkBrowsCap(xmlInput)
}
