package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.utils.ParseRSSElement
import com.github.muhrifqii.parserss.utils.ParserExecutorUtils
import com.github.muhrifqii.parserss.utils.getRSSAttributeElement
import com.github.muhrifqii.parserss.utils.getRSSElement
import com.github.muhrifqii.parserss.utils.nextTextTrimmed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

class ParserExecutor<T>(
    factory: XmlPullParserFactory,
    private val input: Reader,
    strictMode: Boolean,
    feedSupplier: () -> T
) where T : RSSFeed {

    private val parser: XmlPullParser
    private val feed: T
    private val mode: ParsingMode = ParsingMode.Read()
    private val namespaces: LinkedHashMap<String, String> = LinkedHashMap()
    private val pullParserNSAware = strictMode

    init {
        factory.isNamespaceAware = pullParserNSAware
        parser = factory.newPullParser()
        feed = feedSupplier()
    }

    /**
     * Execute the parser
     */
    fun run(): T {
        parser.setInput(input)
        loopThrough()
        return feed
    }

    private fun loopThrough() {
        var token = parser.eventType
        while (token != XmlPullParser.END_DOCUMENT) {
            when (token) {
                XmlPullParser.END_TAG -> closingTag()
                XmlPullParser.START_TAG -> openingTag()
            }
            token = parser.next()
        }
    }

    private fun collectNS(element: ParseRSSElement): ParseRSSElement {
        if (element.name != RSSVersion.RSS_V1.elementName && element.name != RSSVersion.RSS_V2.elementName) {
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
        mode += ParserExecutorUtils.deduceParsingMode(feed, element)
        element = collectNS(element)
        when (element.prefix) {
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia()
            else -> parseNSDefault()
        }
    }

    private fun closingTag() {
        when (parser.getRSSElement(pullParserNSAware).name) {
            ParseRSSKeyword.GROUP -> {
                mode -= ParsingMode.MediaNS.Group
            }
            ParseRSSKeyword.IMAGE -> {
                feed.image = ParsingMode.Image.rssObject
                mode -= ParsingMode.Image
            }
            ParseRSSKeyword.ITEM -> {
                feed.items.add(ParsingMode.Item.rssObject)
                mode -= ParsingMode.Item
            }
        }
    }

    private fun parseNSDefault() {
        when (parser.getRSSElement(pullParserNSAware).name) {
            ParseRSSKeyword.TITLE -> mode[TitleEnabledObject::class.java] = {
                it?.title = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.DESCRIPTION -> mode[DescriptionEnabledObject::class.java] = {
                it?.description = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.LINK -> mode[LinkEnabledObject::class.java] = {
                it?.link = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.PUBLISH_DATE -> mode[PublishDateEnabledObject::class.java] = {
                it?.publishDate = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.LAST_BUILD_DATE -> mode[RSSFeed::class.java] = {
                it?.lastBuildDate = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.URL -> mode[UrlEnabledObject::class.java] = {
                it?.url = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.LANG -> mode[LangEnabledObject::class.java] = {
                it?.language = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.GUID -> mode[GUIdEnabledObject::class.java] = {
                val isPerma =
                    (parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_PERMALINK) ?: "true")
                        .toBoolean()
                it?.guId = GUId(parser.nextTextTrimmed(), isPerma)
            }
            ParseRSSKeyword.AUTHOR -> mode[AuthorEnabledObject::class.java] = {
                it?.author = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.CATEGORY -> mode[CategoryEnabledObject::class.java] = {
                val domain = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_DOMAIN)
                it?.category?.add(
                    RSSCategoryObject(domain, parser.nextTextTrimmed())
                )
            }
            ParseRSSKeyword.COPYRIGHT -> mode[CopyrightsEnabledObject::class.java] = {
                it?.copyright = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.RIGHTS -> mode[CopyrightsEnabledObject::class.java] = {
                it?.rights = parser.nextTextTrimmed()
            }
        }
    }

    private fun parseNSMedia() {
        when (parser.getRSSElement(pullParserNSAware).name) {
            ParseRSSKeyword.CONTENT -> mode[MediaEnabledObject::class.java] = {
                val media = RSSMediaObject()
                media.url = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_URL)
                media.medium = MediaType.from(
                    parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_MEDIUM)
                )
                media.width =
                    parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_WIDTH).toInt()
                media.height =
                    parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_HEIGHT).toInt()
                it?.media?.add(media)
            }
            ParseRSSKeyword.DESCRIPTION -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    media.lastOrNull()?.description = parser.nextTextTrimmed()
                }
            }
            ParseRSSKeyword.CREDIT -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    media.lastOrNull()?.credit = parser.nextTextTrimmed()
                }
            }
        }
    }
}
