package com.github.muhrifqii.parserss

import java.io.Serializable

interface RSSObject
interface RSSFeed: RSSObject {
    var image: RSSImage?
    var items: MutableList<RSSItem>
}
interface RSSItem: RSSObject {
    var title: String?
    var description: String?
    var link: String?
    var publishDate: String?
    var guId: GUId?
}
interface RSSImage: RSSObject {
    var imageUrl: String
    var title: String?
    var link: String?
}

data class GUId (
    val value: String,
    val isPermaLink: Boolean
): Serializable

data class RSSFeedObject(
    var title: String? = null,
    var description: String? = null,
    var link: String? = null,
    var publishDate: String? = null,
    override var image: RSSImage? = null,
    override var items: MutableList<RSSItem> = mutableListOf()
): RSSFeed

data class RSSItemObject(
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null,
    override var guId: GUId? = null
): Serializable, RSSItem

data class RSSImageObject (
    override var imageUrl: String = "",
    override var link: String? = null,
    override var title: String? = null
): Serializable, RSSImage