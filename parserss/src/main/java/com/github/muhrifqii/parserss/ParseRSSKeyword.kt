package com.github.muhrifqii.parserss

internal object ParseRSSKeyword {
    const val CHANNEL = "channel"
    const val ITEM = "item"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val MEDIA = "media"
    const val MEDIA_GROUP = "$MEDIA:group"
    const val MEDIA_CONTENT = "$MEDIA:content"
    const val MEDIA_CREDIT = "$MEDIA:credit"
    const val MEDIA_DESC = "$MEDIA:description"
    const val IMAGE = "image"
    const val PUBLISH_DATE = "pubdate"
    const val GUID = "guid"
    const val LINK = "link"
    const val URL = "url"
    const val LANG = "language"
    const val CATEGORY = "category"

    const val ATTR_PERMALINK = "isPermaLink"
    const val ATTR_DOMAIN = "domain"
    const val ATTR_MEDIUM = "medium"
    const val ATTR_HEIGHT = "height"
    const val ATTR_WIDTH = "width"
    const val ATTR_URL = "url"
}