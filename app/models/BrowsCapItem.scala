package models

import java.util.NoSuchElementException

class BrowsCapItem(val name: String, val attrs: Map[String, String]) {
}

object BrowsCapItem {
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

  def apply(elem: scala.xml.Elem) = {
    val name = elem.attribute("name").get.head.text
    val attrs = Map(elem \ "item" map(extract_name_value) : _*)
    new BrowsCapItem(name, attrs)
  }
}

