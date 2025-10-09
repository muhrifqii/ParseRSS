package com.github.muhrifqii.parserss

data class RSSFeedObject(
    override var version: RSSVersion = RSSVersion.TBD,
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null,
    override var lastUpdated: String? = null,
    override var language: String? = null,
    override var image: RSSImage? = null,
    override var author: RSSPersonAware? = null,
    override var copyright: String? = null,
    override var guId: GUId? = null,
    override var items: MutableList<RSSItem> = mutableListOf()
) : RSSFeed

data class RSSItemObject(
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null,
    override var guId: GUId? = null,
    override var media: MutableList<RSSMedia> = mutableListOf(),
    override var author: RSSPersonAware? = null,
    override var category: MutableList<RSSCategory> = mutableListOf(),
    override var comments: String? = null,
    override var lastUpdated: String? = null,
    override var summary: String? = null
) : RSSItem

data class RSSImageObject(
    override var url: String = "",
    override var link: String? = null,
    override var title: String? = null,
    override var description: String? = null,
) : RSSImage

data class RSSMediaObject(
    override var medium: MediaType = MediaType.Unspecified,
    override var url: String = "",
    override var width: Int = 0,
    override var height: Int = 0,
    override var credit: String? = null,
    override var description: String? = null
) : RSSMedia

data class RSSCategoryObject(
    override var domain: String?,
    override var name: String
) : RSSCategory

data class RSSPersonAwareObject(
    override var name: String,
    override var uri: String? = null,
    override var email: String? = null
) : RSSPersonAware
