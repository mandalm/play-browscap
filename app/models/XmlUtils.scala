package models

object XmlUtils {
  /** Extracts name/value from XML node of the form <item name="..." value="..."></item>
    *
    */
  def extract_item_name_value(item: scala.xml.Node) = {
    val n = item.attribute("name").get.head.text
    try {
      val v = item.attribute("value").get.head.text
      (n, v)
    } catch {
      case _:NoSuchElementException => (n, "")
      case e => throw e
    }
  }

  /** Builds a Map of name -> value with the items' names and values
    *
    */
  def extract_items(node: scala.xml.Node) = {
    Map(node \ "item" map(extract_item_name_value) : _*)
  }
}

