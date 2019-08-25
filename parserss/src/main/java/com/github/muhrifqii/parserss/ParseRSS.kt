package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.util.*

object ParseRSS : ParseRSSPullParser {
    private var factory: XmlPullParserFactory? = null

    /**
     * ParseRSS Initialization. Call this method once on application start
     */
    override fun init(pullParserFactory: XmlPullParserFactory) {
        factory = pullParserFactory
    }

    /**
     * RSS Feed constructor function for generic RSSFeed object
     */
    override var applyRSSFeedConstructor: (() -> RSSFeed) = { RSSFeedObject() }

    /**
     * Parse RSS from Reader Object
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: Reader): R {
        if (factory == null) throw ParseRSSException("xmlPullParserFactory is null. Should call ParseRSS.init() once.")
        val feed = applyRSSFeedConstructor()
        var item = RSSItemObject()
        var image = RSSImageObject()
        factory!!.isNamespaceAware = false
        val parser = factory!!.newPullParser()
        parser.setInput(xml)
        var isParsingChannel = false
        var isParsingItem = false
        var isParsingImage = false
        // start searching token
        var token = parser.eventType
        while (token != XmlPullParser.END_DOCUMENT) {
            if (token == XmlPullParser.START_TAG) {
                when (parser.name.toLowerCase(Locale.ENGLISH)) {
                    ParseRSSKeyword.CHANNEL -> isParsingChannel = true
                    ParseRSSKeyword.ITEM -> isParsingItem = true
                    ParseRSSKeyword.IMAGE -> isParsingImage = true
                    ParseRSSKeyword.TITLE -> {
                        when {
                            isParsingImage -> image.title = parser.nextText().trim()
                            isParsingItem -> item.title = parser.nextText().trim()
                            isParsingChannel -> feed.title = parser.nextText().trim()
                        }
                    }
                    ParseRSSKeyword.DESCRIPTION -> {
                        when {
                            isParsingItem -> item.description = parser.nextText().trim()
                            isParsingChannel -> feed.description = parser.nextText().trim()
                        }
                    }
                    ParseRSSKeyword.LINK -> {
                        when {
                            isParsingImage -> image.link = parser.nextText().trim()
                            isParsingItem -> item.link = parser.nextText().trim()
                            isParsingChannel -> feed.link = parser.nextText().trim()
                        }
                    }
                    ParseRSSKeyword.PUBLISH_DATE -> {
                        when {
                            isParsingItem -> item.publishDate = parser.nextText().trim()
                            isParsingChannel -> feed.publishDate = parser.nextText().trim()
                        }
                    }
                    ParseRSSKeyword.URL -> {
                        when {
                            isParsingImage -> image.imageUrl = parser.nextText().trim()
                        }
                    }
                    ParseRSSKeyword.GUID -> {
                        val isPerma = (parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_PERMALINK) ?: "true")
                            .toBoolean()
                        when {
                            isParsingItem -> item.guId = GUId(parser.nextText().trim(), isPerma)
                        }
                    }
                    ParseRSSKeyword.LANG -> {
                        when {
                            isParsingChannel -> feed.language = parser.nextText().trim()
                        }
                    }
                }
            } else if (token == XmlPullParser.END_TAG) {
                if (parser.name.equals(ParseRSSKeyword.IMAGE, true)) {
                    isParsingImage = false
                    feed.image = image
                    image = RSSImageObject()
                } else if (parser.name.equals(ParseRSSKeyword.ITEM, true)) {
                    isParsingItem = false
                    feed.items.add(item)
                    item = RSSItemObject()
                }
            }
            token = parser.next()
        }
        return feed as R
    }

    /**
     * Parse RSS from xml String
     */
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: String): R {
        return parse(StringReader(xml))
    }
}