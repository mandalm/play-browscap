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

  def apply(elem: scala.xml.Elem, default: Option[BrowsCapItem] = None) = {

    def extract_name_value(item: scala.xml.Node) = {
      val n = item.attribute("name").get.head.text
      try {
        val v = item.attribute("value").get.head.text
        (n, v)
      } catch {
        case _:NoSuchElementException => (n, "")
        case e => throw e
      }
    }

    val name = elem.attribute("name").get.head.text
    val attrs = Map(elem \ "item" map(extract_name_value) : _*)
    new BrowsCapItem(name, attrs, default)
  }
}

