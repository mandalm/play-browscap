package models

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.OneInstancePerTest

import collection.mutable.HashMap

class BrowseCapItemSuite extends FunSuite with ShouldMatchers with OneInstancePerTest {

  val defaultItemXml = <browscapitem name="DefaultProperties">
                        <item name="Comments" value="DefaultProperties" />
                        <item name="Pattern" value="DefaultProperties" />
                        <item name="Browser" value="DefaultProperties" />
                        <item name="Version" value="0" />
                        <item name="MajorVer" value="0" />
                        <item name="MinorVer" value="0" />
                        <item name="Platform" value="" />
                        <item name="Alpha" value="false" />
                        <item name="Beta" value="false" />
                        <item name="Win16" value="false" />
                        <item name="Win32" value="false" />
                        <item name="Win64" value="false" />
                        <item name="Frames" value="false" />
                        <item name="IFrames" value="false" />
                        <item name="Tables" value="false" />
                        <item name="Cookies" value="false" />
                        <item name="BackgroundSounds" value="false" />
                        <item name="JavaScript" value="false" />
                        <item name="VBScript" value="false" />
                        <item name="JavaApplets" value="false" />
                        <item name="ActiveXControls" value="false" />
                        <item name="isBanned" value="false" />
                        <item name="isMobileDevice" value="false" />
                        <item name="isSyndicationReader" value="false" />
                        <item name="Crawler" value="false" />
                        <item name="CssVersion" value="0" />
                        <item name="AolVersion" value="0" />
                        <item name="MasterParent" value="true" />
                        <item name="SortOrder" value="1" />
                        <item name="InternalID" value="7294" />
                      </browscapitem>

  val askItemXml = <browscapitem name="Ask">
                     <item name="Parent" value="DefaultProperties" />
                     <item name="Comments" value="Ask" />
                     <item name="Pattern" value="Ask" />
                     <item name="Browser" value="Ask" />
                     <item name="Frames" value="true" />
                     <item name="IFrames" value="true" />
                     <item name="Tables" value="true" />
                     <item name="Crawler" value="true" />
                     <item name="MasterParent" value="true" />
                     <item name="SortOrder" value="100" />
                     <item name="InternalID" value="4163" />
                   </browscapitem>

  test("browscapitem has name") {
    val item = BrowsCapItem(defaultItemXml)
    item.name should not be (None)
    item.name should equal ("DefaultProperties")
  }

  test("browscapitem has attributes") {
    val item = BrowsCapItem(defaultItemXml)
    val attrs = item.attrs
    attrs should not be (None)

    evaluating { attrs("Undefined") } should produce [java.util.NoSuchElementException]

    attrs("Version") should be ("0")

    // "Platform" -> "", not None
    attrs("Platform") should be ("")
  }

  test("browscapitem with default") {
    val default = BrowsCapItem(defaultItemXml)
    val ask = BrowsCapItem(askItemXml, Some(default))

    default.attr("Browser") should be ("DefaultProperties")
    ask.attr("Browser") should be ("Ask")

    default.attr("SortOrder") should be ("1")
    ask.attr("SortOrder") should be ("100")

    default.attr("Version") should be ("0")
    ask.attr("Version") should be ("0")
  }
}
