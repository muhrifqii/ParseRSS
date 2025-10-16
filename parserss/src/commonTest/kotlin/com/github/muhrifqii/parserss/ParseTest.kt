package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.toPrefixNamedElement
import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParseTest {

    private lateinit var feed: RSSFeedObject
    private val xml = Feed.rssV2EnUS

    @Test
    fun rssNamePrefixedCheck() {
        assertEquals("", "item".toPrefixNamedElement().prefix)
        assertEquals("item", "item".toPrefixNamedElement().name)
        assertEquals("xmlns", "xmlns:rdf".toPrefixNamedElement().prefix)
        assertEquals("rdf", "xmlns:rdf".toPrefixNamedElement().name)
    }

    @Test
    fun validRSSFeedReader() {
        val feed1 = parseRSS(xml).getOrThrow()
        val feed2: RSSFeedObject = parseRSS(xml).getOrThrow()
        assertTrue { feed1.title == "AAAA - RSS Channel - International Edition" }
        assertTrue { feed2.title == "AAAA - RSS Channel - International Edition" }
    }

    @Test
    fun validateRSSFeed() {
        feed = parseRSS(xml).getOrThrow()
        assertTrue { feed.title == "AAAA - RSS Channel - International Edition" }
        assertTrue { feed.link == "https://dp3ap2.jogjaprov.go.id/" }
        assertNull(feed.publishDate)
        assertNotNull(feed.image)
        assertEquals(
            "https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png",
            feed.image!!.url
        )
        assertEquals("en-us", feed.language)
        assertEquals(2, feed.items.size)
    }

    @Test
    fun validateRSSItem() {
        feed = parseRSS(xml).getOrThrow()
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

    @Test
    fun `should able to parse description inside image tag`() {
        feed = parseRSS(xml).getOrThrow()
        assertNotNull(feed.image)
        assertEquals("Breaking News, World News and Video", feed.image!!.description)
    }
}
