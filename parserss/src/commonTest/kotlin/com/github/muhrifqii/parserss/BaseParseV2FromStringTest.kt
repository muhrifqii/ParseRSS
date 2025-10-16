package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.RSSVersion
import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

abstract class BaseParseV2FromStringTest : FromStringParser {
    private val xml = Feed.rssV2EnUS

    open fun `should correctly categorized as RSS v2`() {
        val feed = parseFromString(xml).getOrThrow()
        assertEquals(RSSVersion.RSS_V2, feed.version)
    }

    open fun `should correctly parse standard feed channel`() {
        val feed = parseFromString(xml).getOrThrow()
        assertEquals("AAAA - RSS Channel - International Edition", feed.title)
        assertEquals("https://dp3ap2.jogjaprov.go.id/", feed.link)
        assertNull(feed.publishDate)
        assertNotNull(feed.image)
        assertEquals(
            "https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png",
            feed.image!!.url
        )
        assertEquals("en-us", feed.language)
        assertEquals(2, feed.items.size)
    }

    open fun `should correctly parse items`() {
        val feed = parseFromString(xml).getOrThrow()
        val item1 = feed.items[0]
        assertEquals("Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018", item1.title)
        assertEquals(
            "http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018",
            item1.link
        )
        assertEquals("05 Agustus 2019", item1.publishDate)
        assertTrue(item1.description?.startsWith("Yogyakarta, BPPM- Anak adalah") == true)
        assertEquals(1, item1.media.size)
        assertEquals("Leo Correa/Associated Press", item1.media[0].credit)
        assertNotNull(item1.author)
        assertEquals("John Doe", item1.author!!.name)
        assertEquals(2, item1.category.size)
        assertEquals("http://www.nytimes.com/namespaces/keywords/nyt_geo", item1.category[1].domain)
        val item2 = feed.items[1]
        assertNotNull(item2.guId)
        assertEquals(false, item2.guId!!.isPermaLink)
        assertEquals(11, item2.media.size)
        assertEquals(144, item2.media[10].height)
    }

    open fun `should able to parse description inside image tag`() {
        val feed = parseFromString(xml).getOrThrow()
        assertNotNull(feed.image)
        assertEquals("Breaking News, World News and Video", feed.image!!.description)
    }
}