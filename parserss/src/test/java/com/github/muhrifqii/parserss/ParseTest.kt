package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.StringReader

class ParseTest : AbstractTest() {

    private lateinit var feed: RSSFeedObject
    private val xml = Feed.rssV2EnUS

    override fun configure() {
        feed = ParseRSS.parse(xml)
    }

    @Test
    fun validRSSFeedReader() {
        val reader = StringReader(xml)
        val feed: RSSFeedObject = ParseRSS.parse(reader) {
            RSSFeedObject()
        }
        assertThat(feed.title)
            .matches("AAAA - RSS Channel - International Edition")
    }

    @Test
    fun validateRSSFeed() {
        assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
        assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
        assertThat(feed.publishDate).isNull()
        assertThat(feed.image).isNotNull()
        assertThat(feed.image!!.url).matches("https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png")
        assertThat(feed.language).matches("en-us")
        assertThat(feed.items).hasSize(2)
    }

    @Test
    fun validateRSSItem() {
        val item1 = feed.items[0]
        assertThat(item1.title).matches("Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018")
        assertThat(item1.link).matches("http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018")
        assertThat(item1.publishDate).matches("05 Agustus 2019")
        assertThat(item1.description).startsWith("Yogyakarta, BPPM- Anak adalah")
        assertThat(item1.media).hasSize(1)
        assertThat(item1.media[0].credit).matches("Leo Correa/Associated Press")
        assertThat(item1.author).matches("John Doe")
        assertThat(item1.category).hasSize(2)
        assertThat(item1.category[1].domain).matches("http://www.nytimes.com/namespaces/keywords/nyt_geo")
        val item2 = feed.items[1]
        assertThat(item2.guId).isNotNull()
        assertThat(item2.guId!!.isPermaLink).isFalse()
        assertThat(item2.media).hasSize(11)
        assertThat(item2.media[10].height).isEqualTo(144)
    }
}
