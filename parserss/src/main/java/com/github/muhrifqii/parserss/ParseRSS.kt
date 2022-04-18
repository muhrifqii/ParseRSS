package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader
import java.io.StringReader

object ParseRSS : ParseRSSOperation {
    private var factory: XmlPullParserFactory? = null

    /**
     * ParseRSS Initialization. Call this method once on application start
     */
    fun init(pullParserFactory: XmlPullParserFactory) {
        factory = pullParserFactory
    }

    /**
     * Release Parser Factory
     */
    override fun release() {
        factory = null
    }

    /**
     * RSS Feed constructor function for generic RSSFeed object
     */
    @Deprecated("unused", ReplaceWith(""), DeprecationLevel.ERROR)
    override var applyRSSFeedConstructor: (() -> RSSFeed) = { RSSFeedObject() }

    /**
     * Parse RSS from Reader Object
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: Reader): R {
        return parse(xml, false) {
            RSSFeedObject()
        } as R
    }

    /**
     * Parse RSS from xml String
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: String): R {
        return parse(StringReader(xml), false) {
            RSSFeedObject()
        } as R
    }

    /**
     * Parse RSS customizable
     */
    override fun <T : RSSFeed> parse(xml: Reader, strictlyNamespaceChecking: Boolean, feedSupplier: () -> T): T {
        val unwrappedFactory =
            factory ?: throw ParseRSSException("xmlPullParserFactory is null. Should call ParseRSS.init() once.")
        return ParserExecutor(unwrappedFactory, xml, strictlyNamespaceChecking, feedSupplier)
            .run()
    }
}
