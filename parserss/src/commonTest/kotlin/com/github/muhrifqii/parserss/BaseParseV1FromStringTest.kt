package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.assertEquals

abstract class BaseParseV1FromStringTest : FromStringParser {

    open fun `should correctly categorized as RSS v1`() {
        val feed = parseFromString(Feed.rssV1Simple).getOrThrow()
        assertEquals(RSSVersion.RSS_V1, feed.version)
    }

    open fun `should correctly parse standard feed channel`() {
        val feed = parseFromString(Feed.rssV1Simple).getOrThrow()
        assertEquals("XML.com", feed.title)
        assertEquals("http://xml.com/pub", feed.link)
        assertEquals(2, feed.items.size)
        assertEquals("Processing Inclusions with XSLT", feed.items[0].title)
    }

    open fun `should correctly parse items`() {
        val steamFeed = parseFromString(Feed.rssV1Steam).getOrThrow()
        assertEquals("Steam RSS News Feed", steamFeed.title)
        assertEquals("en", steamFeed.language)
        assertEquals(2, steamFeed.items.size)
        assertEquals(
            "Team Fortress 2 Update Released",
            steamFeed.items[0].title
        )  // Note: This might be a bug in original, but keeping as is
    }
}