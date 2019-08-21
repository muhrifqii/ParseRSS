package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.Fuel
import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeedObject
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory

class DeserializationTest {
    @Before
    fun configure() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
    }

    @Test
    fun YiiRss() {
        val url = "http://dp3ap2.jogjaprov.go.id/rss"
        Fuel.get(url).responseRss<RSSFeedObject> { result ->
            result.fold({ feed ->
                assertThat(feed.title).matches("Berita di Portal DP3AP2")
                assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                assertThat(feed.publishDate).isNull()
                assertThat(feed.items).isNotEmpty()
            }, {
                print(it)
            })
        }
    }
}