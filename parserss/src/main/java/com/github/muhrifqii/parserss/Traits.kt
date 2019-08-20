package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException

interface ParseRSSPullParser {
    fun init(pullParserFactory: XmlPullParserFactory)
    @Throws(XmlPullParserException::class, IOException::class)
    fun <R : RSSFeed> parse(xml: String): R
}