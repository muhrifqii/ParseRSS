package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.util.*

object ParseRSS: ParseRSSPullParser {
    private var factory: XmlPullParserFactory? = null

    override fun init(pullParserFactory: XmlPullParserFactory) {
        factory = pullParserFactory
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: String): R {
        if (factory == null) throw ParseRSSException("xmlPullParserFactory is null. Should call ParseRSS.init() once.")
        val feed = RSSFeedObject()
        var item = RSSItemObject()
        factory!!.isNamespaceAware = false
        val parser = factory!!.newPullParser()
        parser.setInput(StringReader(xml))
        var isParsingChannel = false
        var isParsingItem = false
        // start searching token
        var token = parser.eventType
        while (token != XmlPullParser.END_DOCUMENT) {
            if (token == XmlPullParser.START_TAG) {
                val name = parser.name.toLowerCase(Locale.ENGLISH)
                when (name) {
                    ParseRSSKeyword.CHANNEL -> isParsingChannel = true
                    ParseRSSKeyword.ITEM -> isParsingItem = true
                    ParseRSSKeyword.TITLE -> {
                        if (isParsingItem)
                            item.title = parser.nextText().trim()
                        else if (isParsingChannel)
                            feed.title = parser.nextText().trim()
                    }
                    ParseRSSKeyword.DESCRIPTION -> {
                        if (isParsingItem)
                            item.description = parser.nextText().trim()
                        else if (isParsingChannel)
                            feed.title = parser.nextText().trim()
                    }
                    ParseRSSKeyword.LINK -> {
                        if (isParsingItem)
                            item.link = parser.nextText().trim()
                        else if (isParsingChannel)
                            feed.link = parser.nextText().trim()
                    }
                    ParseRSSKeyword.PUBLISH_DATE -> {
                        if (isParsingItem)
                            item.publishDate = parser.nextText().trim()
                        else if (isParsingChannel)
                            feed.publishDate = parser.nextText().trim()
                    }
                }
            } else if (token == XmlPullParser.END_TAG &&
                parser.name.equals(ParseRSSKeyword.ITEM, true)) {
                isParsingItem = false
                feed.items.add(item)
                item = RSSItemObject()
            }
            token = parser.next()
        }
        return feed as R
    }
}