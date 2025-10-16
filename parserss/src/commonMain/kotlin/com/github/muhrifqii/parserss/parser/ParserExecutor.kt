package com.github.muhrifqii.parserss.parser

import com.github.muhrifqii.parserss.AuthorEnabledObject
import com.github.muhrifqii.parserss.CategoryEnabledObject
import com.github.muhrifqii.parserss.CommentEnabledObject
import com.github.muhrifqii.parserss.CopyrightsEnabledObject
import com.github.muhrifqii.parserss.DescriptionEnabledObject
import com.github.muhrifqii.parserss.GUId
import com.github.muhrifqii.parserss.GUIdEnabledObject
import com.github.muhrifqii.parserss.LangEnabledObject
import com.github.muhrifqii.parserss.LastUpdatedEnabledObject
import com.github.muhrifqii.parserss.LinkEnabledObject
import com.github.muhrifqii.parserss.MediaEnabledObject
import com.github.muhrifqii.parserss.MediaType
import com.github.muhrifqii.parserss.PublishDateEnabledObject
import com.github.muhrifqii.parserss.RSSCategoryObject
import com.github.muhrifqii.parserss.RSSFeed
import com.github.muhrifqii.parserss.RSSItem
import com.github.muhrifqii.parserss.RSSMediaObject
import com.github.muhrifqii.parserss.RSSPersonAwareObject
import com.github.muhrifqii.parserss.SummaryEnabledObject
import com.github.muhrifqii.parserss.TitleEnabledObject
import com.github.muhrifqii.parserss.UrlEnabledObject
import com.github.muhrifqii.parserss.element.ParseRSSElement
import com.github.muhrifqii.parserss.element.ParseRSSKeyword
import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.element.toIntOrZero
import com.github.muhrifqii.parserss.xml.PullParserEventType
import com.github.muhrifqii.parserss.xml.PullParserFactory
import com.github.muhrifqii.parserss.xml.getRSSAttributeElement
import com.github.muhrifqii.parserss.xml.getRSSElement
import com.github.muhrifqii.parserss.xml.nextTextTrimmed

internal inline fun <T : RSSFeed> executeRssParser(
    feed: T,
    factory: PullParserFactory,
    crossinline inputBlock: ParserExecutor<T>.() -> Unit
): T {
    return ParserExecutor(feed, factory)
        .apply(inputBlock)()
}

internal class ParserExecutor<out T : RSSFeed>(
    private val feed: T,
    factory: PullParserFactory,
) {
    private val mode: ParsingMode = ParsingMode.Read()
    private val namespaces: LinkedHashMap<String, String> = LinkedHashMap()
    private val pullParserNSAware = factory.isNamespaceAware

    val parser = factory.newPullParser()

    /**
     * Execute the parser
     */
    operator fun invoke(): T {
        loopThrough()
        return feed
    }

    private fun loopThrough() {
        var token = parser.eventType
        while (token != PullParserEventType.END_DOCUMENT) {
            when (token) {
                PullParserEventType.END_TAG -> closingTag()
                PullParserEventType.START_TAG -> openingTag()
                else -> {
                    /* Ignore other event types */
                }
            }
            token = parser.next()
        }
    }

    private fun collectNS(element: ParseRSSElement): ParseRSSElement {
        if (element.name != RSSVersion.RSS_V1.elementName &&
            element.name != RSSVersion.RSS_V2.elementName &&
            element.name != RSSVersion.RSS_ATOM.elementName
        ) {
            return element
        }
        val attrCount = parser.attributeCount
        for (i in 0 until attrCount) {
            val attribute = parser.getRSSAttributeElement(i, pullParserNSAware)
            namespaces[attribute.name] = attribute.value
        }
        return element
    }

    private fun openingTag() {
        var element = parser.getRSSElement(pullParserNSAware)
        mode += deduceParsingMode(feed, element)
        element = collectNS(element)
        if (mode.contains(RootDocument.Atom)) {
            parseAtom(element)
        } else {
            parseV1V2(element)
        }
    }

    private fun closingTag() {
        when (parser.getRSSElement(pullParserNSAware).name) {
            ParseRSSKeyword.GROUP -> {
                mode -= ParsingMode.MediaNS.Group()
            }

            ParseRSSKeyword.IMAGE -> {
                mode -= ParsingMode.Image()
            }

            ParseRSSKeyword.ITEM -> {
                mode -= ParsingMode.Item()
            }

            ParseRSSKeyword.ENTRY -> {
                mode -= ParsingMode.Item()
            }

            ParseRSSKeyword.AUTHOR -> {
                mode -= ParsingMode.Author()
            }
        }
    }

    private fun parseNSDefault(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.TITLE -> mode[TitleEnabledObject::class] = {
                it?.title = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.DESCRIPTION -> mode[DescriptionEnabledObject::class] = {
                it?.description = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.LINK -> mode[LinkEnabledObject::class] = {
                it?.link = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.PUBLISH_DATE -> mode[PublishDateEnabledObject::class] = {
                it?.publishDate = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.LAST_BUILD_DATE -> mode[LastUpdatedEnabledObject::class] = {
                it?.lastUpdated = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.URL -> mode[UrlEnabledObject::class] = {
                it?.url = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.LANG -> mode[LangEnabledObject::class] = {
                it?.language = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.GUID -> mode[GUIdEnabledObject::class] = {
                val isPerma =
                    (parser.getAttributeValue(null, ParseRSSKeyword.ATTR_PERMALINK) ?: "true")
                        .toBoolean()
                it?.guId = GUId(parser.nextTextTrimmed(), isPerma)
            }

            ParseRSSKeyword.AUTHOR -> mode[AuthorEnabledObject::class] = {
                it?.author = RSSPersonAwareObject(parser.nextTextTrimmed())
            }

            ParseRSSKeyword.CATEGORY -> mode[CategoryEnabledObject::class] = {
                val domain = parser.getAttributeValue(null, ParseRSSKeyword.ATTR_DOMAIN)
                it?.category?.add(
                    RSSCategoryObject(domain, parser.nextTextTrimmed())
                )
            }

            ParseRSSKeyword.COPYRIGHT -> mode[CopyrightsEnabledObject::class] = {
                it?.copyright = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.COMMENTS -> mode[CommentEnabledObject::class] = {
                it?.comments = parser.nextTextTrimmed()
            }
        }
    }

    private fun parseNSMedia(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.CONTENT -> mode[MediaEnabledObject::class] = {
                val media = RSSMediaObject()
                media.url = parser.getAttributeValue(null, ParseRSSKeyword.ATTR_URL) ?: ""
                media.medium = MediaType.from(
                    parser.getAttributeValue(null, ParseRSSKeyword.ATTR_MEDIUM) ?: ""
                )
                media.width =
                    parser.getAttributeValue(null, ParseRSSKeyword.ATTR_WIDTH).toIntOrZero()
                media.height =
                    parser.getAttributeValue(null, ParseRSSKeyword.ATTR_HEIGHT).toIntOrZero()
                it?.media?.add(media)
            }

            ParseRSSKeyword.DESCRIPTION -> mode[MediaEnabledObject::class] = {
                it?.apply {
                    media.lastOrNull()?.description = parser.nextTextTrimmed()
                }
            }

            ParseRSSKeyword.CREDIT -> mode[MediaEnabledObject::class] = {
                it?.apply {
                    media.lastOrNull()?.credit = parser.nextTextTrimmed()
                }
            }
        }
    }

    private fun parseNSAtom(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.TITLE -> mode[TitleEnabledObject::class] = {
                it?.title = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.SUBTITLE -> mode[DescriptionEnabledObject::class] = {
                it?.description = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.PUBLISHED -> mode[PublishDateEnabledObject::class] = {
                it?.publishDate = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.ID -> mode[GUIdEnabledObject::class] = {
                it?.guId = GUId(parser.nextTextTrimmed(), false)
            }

            ParseRSSKeyword.UPDATED -> mode[LastUpdatedEnabledObject::class] = {
                it?.lastUpdated = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.LINK -> mode[LinkEnabledObject::class] = {
                val rel = parser.getAttributeValue(null, ParseRSSKeyword.ATTR_REL) ?: ""
                val href = parser.getAttributeValue(null, ParseRSSKeyword.ATTR_HREF)
                if (rel == "self" && it is RSSFeed) {
                    it.link = href
                } else if (rel == "alternate" && it is RSSItem) {
                    it.link = href
                }
            }

            ParseRSSKeyword.RIGHTS -> mode[CopyrightsEnabledObject::class] = {
                it?.copyright = parser.nextTextTrimmed()
            }

            ParseRSSKeyword.SUMMARY -> mode[SummaryEnabledObject::class] = {
                it?.summary = parser.nextTextTrimmed()
            }
        }
        parseAtomPersonAwareObject(element)
    }

    private fun parseAtomPersonAwareObject(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.AUTHOR -> mode[AuthorEnabledObject::class] = {
                it?.author = RSSPersonAwareObject("")
            }

            ParseRSSKeyword.NAME -> mode[AuthorEnabledObject::class] = {
                it?.apply {
                    author?.name = parser.nextTextTrimmed()
                }
            }

            ParseRSSKeyword.URI -> mode[AuthorEnabledObject::class] = {
                it?.apply {
                    author?.uri = parser.nextTextTrimmed()
                }
            }

            ParseRSSKeyword.EMAIL -> mode[AuthorEnabledObject::class] = {
                it?.apply {
                    author?.email = parser.nextTextTrimmed()
                }
            }
        }
    }

    private fun parseV1V2(element: ParseRSSElement) {
        when (element.prefix) {
            ParseRSSKeyword.ATOM_NS -> parseAtom(element)
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia(element)
            else -> parseNSDefault(element)
        }
    }

    private fun parseAtom(element: ParseRSSElement) {
        when (element.prefix) {
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia(element)
            else -> parseNSAtom(element)
        }
    }
}
