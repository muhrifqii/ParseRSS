package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.internal.xml.AndroidPullParserFactory
import com.github.muhrifqii.parserss.internal.xml.PullParserFactory
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.Reader
import java.io.StringReader

object ParseRSS : ParseRSSOperation {
    private var factory: PullParserFactory? = null

    /**
     * ParseRSS Initialization. Call this method once on application start
     */
    @Deprecated("Singleton initialization will be deleted once KMP is supported. Should use DI later")
    fun init(pullParserFactory: XmlPullParserFactory) {
        factory = AndroidPullParserFactory(pullParserFactory)
    }

    @Deprecated("Singleton de-initialization will be deleted once KMP is supported")
    override fun release() {
        factory = null
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: Reader): R {
        return parse(xml, false) {
            RSSFeedObject()
        } as R
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(XmlPullParserException::class, IOException::class, ParseRSSException::class)
    override fun <R : RSSFeed> parse(xml: String): R {
        return parse(StringReader(xml), false) {
            RSSFeedObject()
        } as R
    }

    override fun <T : RSSFeed> parse(
        xml: Reader,
        strictlyNamespaceChecking: Boolean,
        feedSupplier: () -> T
    ): T {
        val unwrappedFactory =
            factory
                ?: throw ParseRSSException("xmlPullParserFactory is null. Should call ParseRSS.init() once.")
        return ParserExecutor(unwrappedFactory, xml, strictlyNamespaceChecking, feedSupplier)
            .run()
    }
}
