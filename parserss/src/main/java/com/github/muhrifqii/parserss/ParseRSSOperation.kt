package com.github.muhrifqii.parserss

import java.io.IOException
import java.io.Reader

interface ParseRSSOperation {
    fun release()
    var applyRSSFeedConstructor: (() -> RSSFeed)

    @Throws(ParseRSSException::class, IOException::class)
    fun <R : RSSFeed> parse(xml: Reader): R

    @Throws(ParseRSSException::class)
    fun <R : RSSFeed> parse(xml: String): R

    @Throws(ParseRSSException::class)
    fun <T : RSSFeed> parse(xml: Reader, feedSupplier: () -> T): T
}
