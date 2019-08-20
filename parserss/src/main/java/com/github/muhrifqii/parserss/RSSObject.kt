package com.github.muhrifqii.parserss

import java.io.Serializable

interface RSSObject
interface RSSFeed: RSSObject {
    var items: MutableList<RSSItem>
}
interface RSSItem: RSSObject {
    var title: String?
    var description: String?
    var link: String?
    var publishDate: String?
}

data class RSSFeedObject(
    var title: String? = null,
    var description: String? = null,
    var link: String? = null,
    var publishDate: String? = null,
    override var items: MutableList<RSSItem> = mutableListOf()
): RSSFeed

data class RSSItemObject(
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null
): Serializable, RSSItem