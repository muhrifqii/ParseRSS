package com.github.muhrifqii.parserss

import java.io.Serializable

interface RSSObject : Serializable

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
    var author: String?
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
    var rights: String?
}

interface RSSFeed :
    RSSObject,
    TitleEnabledObject,
    DescriptionEnabledObject,
    LinkEnabledObject,
    PublishDateEnabledObject,
    LangEnabledObject,
    CopyrightsEnabledObject {
    var version: RSSVersion
    var image: RSSImage?
    var lastBuildDate: String?
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
    CommentEnabledObject

interface RSSImage :
    RSSObject,
    TitleEnabledObject,
    LinkEnabledObject,
    UrlEnabledObject

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

enum class MediaType(private val rawValue: String) {
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

@SuppressWarnings("SerialVersionUIDInSerializableClass")
data class GUId(
    val value: String,
    val isPermaLink: Boolean
) : Serializable
