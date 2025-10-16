package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseAtomTest {
    private lateinit var feed: RSSFeedObject

    @Test
    fun validVersion() {
        feed = parseRSS(Feed.rssAtom).getOrThrow()
        assertEquals(RSSVersion.RSS_ATOM, feed.version)
    }

    @Test
    fun validSimpleFeedAndItem() {
        feed = parseRSS(Feed.rssAtom).getOrThrow()
        assertEquals("dive into mark", feed.title)
        assertEquals(2, feed.items.size)
        assertEquals( "Atom draft-07 snapshot", feed.items[0].title)
        assertEquals( "Atom-Powered Robots Run Amok", feed.items[1].title)
        assertEquals("tag:example.org,2003:3", feed.guId!!.value)
        assertEquals("Some text.", feed.items[1].summary)
        assertEquals("2003-12-13T18:30:02Z", feed.items[1].lastUpdated)
        assertEquals("f8dy@example.com", feed.items[0].author!!.email)
    }
}
