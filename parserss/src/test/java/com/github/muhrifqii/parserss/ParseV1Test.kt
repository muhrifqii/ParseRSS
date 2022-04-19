package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import com.google.common.truth.Truth
import org.junit.Test

class ParseV1Test : AbstractTest() {
    private lateinit var feed: RSSFeedObject
    private lateinit var steamFeed: RSSFeedObject

    override fun configure() {
        feed = ParseRSS.parse(Feed.rssV1Simple)
        steamFeed = ParseRSS.parse(Feed.rssV1Steam)
    }

    @Test
    fun validVersion() {
        Truth.assertThat(feed.version).isEqualTo(RSSVersion.RSS_V1)
    }

    @Test
    fun validSimpleFeedAndItem() {
        Truth.assertThat(feed.title).matches("XML.com")
        Truth.assertThat(feed.link).matches("http://xml.com/pub")
        Truth.assertThat(feed.items).hasSize(2)
        Truth.assertThat(feed.items[0].title).matches("Processing Inclusions with XSLT")
    }

    @Test
    fun validSteamFeedItem() {
        Truth.assertThat(steamFeed.title).matches("Steam RSS News Feed")
        Truth.assertThat(steamFeed.language).matches("en")
        Truth.assertThat(feed.items).hasSize(2)
        Truth.assertThat(feed.items[0].title).matches("Processing Inclusions with XSLT")
    }
}
