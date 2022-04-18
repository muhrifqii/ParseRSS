package com.github.muhrifqii.parserss

internal object ParseRSSKeyword {

    const val DEFAULT_NS = ""
    const val MEDIA_NS = "media"

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

    const val MEDIA_GROUP = "$MEDIA_NS:group"
    const val MEDIA_CONTENT = "$MEDIA_NS:content"
    const val MEDIA_CREDIT = "$MEDIA_NS:credit"
    const val MEDIA_DESC = "$MEDIA_NS:description"

    const val ATTR_PERMALINK = "isPermaLink"
    const val ATTR_DOMAIN = "domain"
    const val ATTR_MEDIUM = "medium"
    const val ATTR_HEIGHT = "height"
    const val ATTR_WIDTH = "width"
    const val ATTR_URL = "url"
}
