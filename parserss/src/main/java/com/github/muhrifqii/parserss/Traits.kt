package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader

interface ParseRSSPullParser {
    fun init(pullParserFactory: XmlPullParserFactory)
    var applyRSSFeedConstructor: (() -> RSSFeed)
    @Throws(XmlPullParserException::class, IOException::class)
    fun <R : RSSFeed> parse(xml: Reader): R
    fun <R : RSSFeed> parse(xml: String): R
}