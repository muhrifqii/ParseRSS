package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BasicParsingTest {
    @Test
    fun parseSimpleFeed() {
        val feed = parseRSS(Feed.rssV2EnUS).getOrThrow()
        assertEquals("AAAA - RSS Channel - International Edition", feed.title)
        assertEquals(2, feed.items.size)
    }

    @Test
    fun parseV1Feed() {
        val feed = parseRSS(Feed.rssV1Simple).getOrThrow()
        assertEquals("XML.com", feed.title)
        assertNotNull(feed.link)
    }

    @Test
    fun parseAtomFeed() {
        val feed = parseRSS(Feed.rssAtom).getOrThrow()
        assertEquals("dive into mark", feed.title)
        assertEquals(2, feed.items.size)
    }
}
