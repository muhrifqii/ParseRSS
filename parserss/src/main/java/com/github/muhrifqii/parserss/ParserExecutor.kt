package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

class ParserExecutor<T>(
    factory: XmlPullParserFactory,
    private val input: Reader,
    feedSupplier: () -> T
) where T : RSSFeed {

    private val parser: XmlPullParser
    private val feed: T
    private val mode: ParsingMode = ParsingMode.Read()

    init {
        factory.isNamespaceAware = true
        parser = factory.newPullParser()
        feed = feedSupplier()
    }

    /**
     * Execute the parser
     */
    fun run(): T {
        parser.setInput(input)
        loopThrough(parser.eventType)
        return feed
    }

    private fun loopThrough(token: Int) {
        while (token != XmlPullParser.END_DOCUMENT) {
            when (token) {
                XmlPullParser.END_TAG -> closingTag()
                XmlPullParser.START_TAG -> openingTag()
            }
            parser.next()
        }
    }

    /** deduce mode based on given name */
    private fun deduceParsingMode(root: RSSFeed, prefix: String, localName: String): ParsingMode {
        return when (prefix) {
            ParseRSSKeyword.DEFAULT_NS ->
                when (localName) {
                    ParseRSSKeyword.CHANNEL -> ParsingMode.Channel(root)
                    ParseRSSKeyword.ITEM -> ParsingMode.Item
                    ParseRSSKeyword.IMAGE -> ParsingMode.Image
                    ParseRSSKeyword.MEDIA_GROUP -> ParsingMode.MediaNS.Group
                    else -> ParsingMode.Read()
                }
            ParseRSSKeyword.MEDIA_NS ->
                when (prefix.buildRSSName(localName)) {
                    ParseRSSKeyword.MEDIA_GROUP -> ParsingMode.MediaNS.Group
                    else -> ParsingMode.Read()
                }
            else -> ParsingMode.Read()
        }
    }

    private fun openingTag() {
        mode += deduceParsingMode(feed, parser.lowerCasePrefix(), parser.lowerCaseName())
        when (parser.lowerCasePrefix()) {
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia()
            else -> parseNSDefault()
        }
    }

    private fun closingTag() {
        when (parser.lowerCaseName()) {
            ParseRSSKeyword.MEDIA_GROUP -> {
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
        when (parser.lowerCaseName()) {
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
        }
    }

    private fun parseNSMedia() {
        when (parser.lowerCasePrefix().buildRSSName(parser.lowerCaseName())) {
            ParseRSSKeyword.MEDIA_CONTENT -> mode[MediaEnabledObject::class.java] = {
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
            ParseRSSKeyword.MEDIA_DESC -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    media.lastOrNull()?.description = parser.nextTextTrimmed()
                }
            }
            ParseRSSKeyword.MEDIA_CREDIT -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    media.lastOrNull()?.credit = parser.nextTextTrimmed()
                }
            }
        }
    }
}
