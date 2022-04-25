package com.github.muhrifqii.parserss

internal object ParseRSSKeyword {

    const val DEFAULT_NS = ""
    const val MEDIA_NS = "media"
    const val RDF_NS = "rdf"
    const val ATOM_NS = "atom"
    const val DC_NS = "dc"

    const val CHANNEL = "channel"
    const val ITEM = "item"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val IMAGE = "image"
    const val PUBLISH_DATE = "pubdate"
    const val GUID = "guid"
    const val LINK = "link"
    const val URL = "url"
    const val LANG = "language"
    const val CATEGORY = "category"
    const val AUTHOR = "author"
    const val COPYRIGHT = "copyright"
    const val RIGHTS = "rights"
    const val LAST_BUILD_DATE = "lastbuilddate"
    const val GROUP = "group"
    const val CONTENT = "content"
    const val CREDIT = "credit"
    const val COMMENTS = "comments"

    const val ENTRY = "entry"
    const val SUBTITLE = "subtitle"
    const val NAME = "name"
    const val URI = "uri"
    const val EMAIL = "email"
    const val ID = "id"
    const val UPDATED = "updated"
    const val PUBLISHED = "published"
    const val CONTRIBUTOR = "contributor"
    const val SUMMARY = "summary"

    const val ATTR_PERMALINK = "isPermaLink"
    const val ATTR_DOMAIN = "domain"
    const val ATTR_MEDIUM = "medium"
    const val ATTR_HEIGHT = "height"
    const val ATTR_WIDTH = "width"
    const val ATTR_URL = "url"
    const val ATTR_URI = "uri"
    const val ATTR_HREF = "href"
    const val ATTR_REL = "rel"
}

enum class RSSVersion(val elementName: String, val xmlns: String) {
    TBD("", ""),
    RSS_V1("rdf", "http://purl.org/rss/1.0/"),
    RSS_V2("rss", ""),
    RSS_ATOM("feed", "http://www.w3.org/2005/Atom");

    override fun toString(): String = "RSSVersion(elementName='$elementName', xmlns='$xmlns')"

    companion object {
        /**
         * Deduce version based from rss element
         */
        fun valueOfElement(elementName: String): RSSVersion = when (elementName) {
            RSS_V1.elementName -> RSS_V1
            RSS_V2.elementName -> RSS_V2
            RSS_ATOM.elementName -> RSS_ATOM
            else -> throw IllegalArgumentException("$elementName is not a valid element")
        }
    }
}
