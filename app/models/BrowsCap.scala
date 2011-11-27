package models

import collection.mutable
import collection.immutable

trait GjkVersion {
  def getInput() : scala.xml.Elem
  private val attrs = XmlUtils.extract_items(getInput() \ "gjk_browscap_version" head)
  val version = attrs("Version")
  val released = attrs("Released")
}

class BrowsCap(val input: scala.xml.Elem) {
  val comments = input \ "comments" \ "comment" map(_.text) reduceLeft(_ + "\n" + _)
  var defaultProperties: Option[BrowsCapItem] = None

  private val parents: mutable.Map[String, BrowsCapItem] = new mutable.HashMap()

  private def convert(node: scala.xml.Node): BrowsCapItem = {
    val item = BrowsCapItem(node, defaultProperties)
    if (defaultProperties.isEmpty && "DefaultProperties".equals(item.name)) defaultProperties = Some(item)
    if ("true".equals(item.attr("MasterParent"))) {
      parents(item.name) = item
    }
    item
  }

  private def attachParent(item: BrowsCapItem): BrowsCapItem = {
    if (item.hasAttr("Parent")) {
      val parent = parents(item.attr("Parent"))
      parent.children = parent.children :+ item
    }
    item
  }

  val items = input \ "browsercapitems" \ "browscapitem" map(convert) map(attachParent)
}

class GjkBrowsCap(override val input : scala.xml.Elem) extends BrowsCap(input) with GjkVersion {
  def getInput() = input
}

object BrowsCap {
  def apply(xmlInput: scala.xml.Elem) = new GjkBrowsCap(xmlInput)
}
