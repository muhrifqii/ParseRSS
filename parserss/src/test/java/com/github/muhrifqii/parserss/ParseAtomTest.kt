package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.samples.Feed
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ParseAtomTest : AbstractTest() {
    private lateinit var feed: RSSFeedObject

    override fun configure() {
        feed = ParseRSS.parse(Feed.rssAtom)
    }

    @Test
    fun validVersion() {
        assertThat(feed.version).isEqualTo(RSSVersion.RSS_ATOM)
    }

    @Test
    fun validSimpleFeedAndItem() {
        assertThat(feed.title).matches("dive into mark")
        assertThat(feed.items).hasSize(2)
        assertThat(feed.items[0].title)
            .matches("Atom draft-07 snapshot")
        assertThat(feed.items[1].title)
            .matches("Atom-Powered Robots Run Amok")
        assertThat(feed.guId!!.value).matches("tag:example.org,2003:3")
        assertThat(feed.items[1].summary).matches("Some text.")
        assertThat(feed.items[1].lastUpdated).matches("2003-12-13T18:30:02Z")
        assertThat(feed.items[0].author!!.email).matches("f8dy@example.com")
    }
}
