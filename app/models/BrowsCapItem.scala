package models

import java.util.NoSuchElementException

class BrowsCapItem(val name: String, val attrs: Map[String, String], val default: Option[BrowsCapItem] = None) {

  /** Returns attribute value in attrs given attribute name.
    *
    * If the attribute is not defined in this instance's attrs, use default's attrs recursively
    */
  def attr(name: String): String = {
    attrs.get(name) match {
      case Some(x) => x
      case None => if (default.isDefined) default.get.attr(name) else throw new NoSuchElementException
    }
  }
}

/** Builds a BrowsCapItem object given a XML node
  */
object BrowsCapItem {
  def apply(node: scala.xml.Node, default: Option[BrowsCapItem] = None) = {
    val name = node.attribute("name").get.head.text
    val attrs = XmlUtils.extract_items(node)
    new BrowsCapItem(name, attrs, default)
  }
}

