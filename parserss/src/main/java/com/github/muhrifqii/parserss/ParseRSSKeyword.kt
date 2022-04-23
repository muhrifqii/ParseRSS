package com.github.muhrifqii.parserss

internal object ParseRSSKeyword {

    const val DEFAULT_NS = ""
    const val MEDIA_NS = "media"
    const val RDF_NS = "rdf"
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

    const val FEED = "feed"
    const val ENTRY = "entry"

    const val RDF_SEQ = "$RDF_NS:seq"
    const val RDF_SEQ_LIST = "$RDF_NS:li"

    const val ATTR_PERMALINK = "isPermaLink"
    const val ATTR_DOMAIN = "domain"
    const val ATTR_MEDIUM = "medium"
    const val ATTR_HEIGHT = "height"
    const val ATTR_WIDTH = "width"
    const val ATTR_URL = "url"
}

enum class RSSVersion(val elementName: String) {
    TBD(""), RSS_V1("rdf"), RSS_V2("rss"), RSS_ATOM("feed");

    override fun toString(): String {
        return "$name(elementName='$elementName')"
    }

    companion object {
        /**
         * Deduce version based from rss element
         */
        fun valueOfElement(elementName: String): RSSVersion {
            return when (elementName) {
                RSS_V1.elementName -> RSS_V1
                RSS_V2.elementName -> RSS_V2
                else -> throw IllegalArgumentException("$elementName is not a valid element")
            }
        }
    }
}
