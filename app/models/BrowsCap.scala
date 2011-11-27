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

  /** Convert xml.Node to BrowsCapItem
    *
    * The item is populated with Default as parent, but may change later once parent is identified.
    */
  private def convert(node: scala.xml.Node): BrowsCapItem = {
    val item = BrowsCapItem(node, defaultProperties)
    if (defaultProperties.isEmpty && "DefaultProperties".equals(item.name)) defaultProperties = Some(item)
    if ("true".equals(item.attr("MasterParent"))) {
      parents(item.name) = item
    }
    item
  }

  /** Attach a child to its parent, and re-create the child item
    */
  private def attachParent(item: BrowsCapItem): BrowsCapItem = {
    var newItem = item;
    if (item.hasAttr("Parent")) {
      val parent = parents(item.attr("Parent"))
      newItem = new BrowsCapItem(item.name, item.attrs, Some(parent))
      parent.children = parent.children :+ newItem
    }
    newItem
  }

  /** Sort the list by "SortOrder"
    *
    * Should be called by sortWith(), which is stable.
    */
  private def lt(item1: BrowsCapItem, item2: BrowsCapItem): Boolean = {
    val s1 = item1.attr("SortOrder").toInt
    val s2 = item2.attr("SortOrder").toInt
    s1 < s2
  }

  val items = input \ "browsercapitems" \ "browscapitem" map(convert) map(attachParent) sortWith(lt)

  /** Return first item with matching pattern
    *
    */
  def firstMatch(ua: String): Option[BrowsCapItem] = {
    for (item <- items) {
      if (item.matches(ua)) {
        return Some(item)
      }
    }
    None
  }

  def matches(ua: String): Option[BrowsCapItem] = {
    firstMatch(ua)
  }

  /** Return first item with matching InternalID
    */
  def getByInternalID(id: Int): Option[BrowsCapItem] = {
    for (item <- items) {
      if (item.attr("InternalID").toInt == id) return Some(item)
    }
    None
  }

  /** Return an master parent item with matching name
    */
  def getMasterParentByName(name: String): Option[BrowsCapItem] = {
     parents.get(name)
  }
}

class GjkBrowsCap(override val input : scala.xml.Elem) extends BrowsCap(input) with GjkVersion {
  def getInput() = input
}

object BrowsCap {
  def apply(): BrowsCap = apply(xml.XML.load("app/resources/browscap.xml"))
  def apply(xmlInput: scala.xml.Elem) = new GjkBrowsCap(xmlInput)
}
