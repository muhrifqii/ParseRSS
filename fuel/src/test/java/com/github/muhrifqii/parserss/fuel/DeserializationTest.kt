package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.Fuel
import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeedObject
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParserFactory

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DeserializationTest {
    @Before
    fun configure() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
    }

    @After
    fun release() {
        ParseRSS.release()
    }

    @Test
    fun yiiRss() {
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
