package com.github.muhrifqii.parserss

import java.io.Serializable

interface RSSObject : Serializable
interface RSSFeed : RSSObject {
    var title: String?
    var description: String?
    var link: String?
    var publishDate: String?
    var language: String?
    var image: RSSImage?
    var items: MutableList<RSSItem>
}

interface RSSItem : RSSObject {
    var title: String?
    var description: String?
    var link: String?
    var publishDate: String?
    var guId: GUId?
    var media: MutableList<RSSMedia>
    var author: String?
    var category: MutableList<RSSCategory>
}

interface RSSImage : RSSObject {
    var imageUrl: String
    var title: String?
    var link: String?
}

interface RSSMedia : RSSObject {
    var height: Int
    var width: Int
    var medium: MediaType
    var url: String

    var credit: String?
    var description: String?
}

interface RSSCategory : RSSObject {
    var domain: String?
    var name: String
}

enum class MediaType(val rawValue: String) {
    Image("image"), Video("video"), Unspecified("");

    override fun toString(): String {
        return rawValue
    }

    companion object {
        fun from(rawValue: String): MediaType {
            return when (rawValue) {
                "image" -> Image
                "video" -> Video
                else -> Unspecified
            }
        }
    }
}

data class GUId (
    val value: String,
    val isPermaLink: Boolean
): Serializable

data class RSSFeedObject(
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null,
    override var language: String? = null,
    override var image: RSSImage? = null,
    override var items: MutableList<RSSItem> = mutableListOf()
) : RSSFeed

data class RSSItemObject(
    override var title: String? = null,
    override var description: String? = null,
    override var link: String? = null,
    override var publishDate: String? = null,
    override var guId: GUId? = null,
    override var media: MutableList<RSSMedia> = mutableListOf(),
    override var author: String? = null,
    override var category: MutableList<RSSCategory> = mutableListOf()
) : RSSItem

data class RSSImageObject(
    override var imageUrl: String = "",
    override var link: String? = null,
    override var title: String? = null
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
