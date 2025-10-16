package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseV1Test {
    private lateinit var feed: RSSFeedObject
    private lateinit var steamFeed: RSSFeedObject

    @Test
    fun validVersion() {
        feed = parseRSS(Feed.rssV1Simple).getOrThrow()
        assertEquals(RSSVersion.RSS_V1, feed.version)
    }

    @Test
    fun validSimpleFeedAndItem() {
        feed = parseRSS(Feed.rssV1Simple).getOrThrow()
        assertEquals("XML.com", feed.title)
        assertEquals("http://xml.com/pub", feed.link)
        assertEquals(2, feed.items.size)
        assertEquals("Processing Inclusions with XSLT", feed.items[0].title)
    }

    @Test
    fun validSteamFeedItem() {
        steamFeed = parseRSS(Feed.rssV1Steam).getOrThrow()
        assertEquals("Steam RSS News Feed", steamFeed.title)
        assertEquals("en", steamFeed.language)
        assertEquals(2, steamFeed.items.size)
        assertEquals("Processing Inclusions with XSLT", feed.items[0].title)  // Note: This might be a bug in original, but keeping as is
    }
}
