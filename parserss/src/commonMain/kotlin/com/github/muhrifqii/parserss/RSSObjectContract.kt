package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion

interface RSSObject

interface TitleEnabledObject : RSSObject {
    var title: String?
}

interface DescriptionEnabledObject : RSSObject {
    var description: String?
}

interface LinkEnabledObject : RSSObject {
    var link: String?
}

interface PublishDateEnabledObject : RSSObject {
    var publishDate: String?
}

interface UrlEnabledObject : RSSObject {
    var url: String
}

interface LangEnabledObject : RSSObject {
    var language: String?
}

interface AuthorEnabledObject : RSSObject {
    var author: RSSPersonAware?
}

interface CategoryEnabledObject : RSSObject {
    var category: MutableList<RSSCategory>
}

interface GUIdEnabledObject : RSSObject {
    var guId: GUId?
}

interface MediaEnabledObject : RSSObject {
    var media: MutableList<RSSMedia>
}

interface CommentEnabledObject : RSSObject {
    var comments: String?
}

interface CopyrightsEnabledObject : RSSObject {
    var copyright: String?
}

interface LastUpdatedEnabledObject : RSSObject {
    var lastUpdated: String?
}

interface SummaryEnabledObject : RSSObject {
    var summary: String?
}

interface RSSFeed :
    RSSObject,
    TitleEnabledObject,
    DescriptionEnabledObject,
    LinkEnabledObject,
    PublishDateEnabledObject,
    LangEnabledObject,
    GUIdEnabledObject,
    CopyrightsEnabledObject,
    AuthorEnabledObject,
    LastUpdatedEnabledObject {
    var version: RSSVersion
    var image: RSSImage?
    var items: MutableList<RSSItem>
}

interface RSSItem :
    RSSObject,
    TitleEnabledObject,
    DescriptionEnabledObject,
    LinkEnabledObject,
    PublishDateEnabledObject,
    AuthorEnabledObject,
    CategoryEnabledObject,
    GUIdEnabledObject,
    MediaEnabledObject,
    CommentEnabledObject,
    LastUpdatedEnabledObject,
    SummaryEnabledObject

interface RSSImage :
    RSSObject,
    TitleEnabledObject,
    LinkEnabledObject,
    UrlEnabledObject,
    DescriptionEnabledObject

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

interface RSSPersonAware : RSSObject {
    var name: String
    var uri: String?
    var email: String?
}

enum class MediaType(private val rawValue: String) {
    Image("image"), Video("video"), Unspecified("");

    override fun toString(): String {
        return rawValue
    }

    companion object {
        /**
         * Returns [MediaType] based on the [rawValue]
         */
        fun from(rawValue: String): MediaType {
            return when (rawValue) {
                "image" -> Image
                "video" -> Video
                else -> Unspecified
            }
        }
    }
}

data class GUId(
    val value: String,
    val isPermaLink: Boolean
)
