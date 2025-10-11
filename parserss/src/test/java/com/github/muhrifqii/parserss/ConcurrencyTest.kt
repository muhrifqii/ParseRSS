package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.StringReader
import java.util.stream.Stream

class ConcurrencyTest : AbstractTest() {

    override fun configure() {
    }

    @Test
    fun parallelParsing() = Stream.of(Feed.rssV2EnUS, Feed.rssV1Simple, Feed.rssV1Steam)
        .map { StringReader(it) }
        .parallel()
        .map {
            ParseRSS.parse(it, false) { RSSFeedObject() }
        }.forEach { assertion(it) }

    @Test
    fun concurrentCoroutine() = runBlocking(Dispatchers.Default) {
        repeat(10) {
            println("Step $it")
            coroutineScope {
                listOf(Feed.rssV2EnUS, Feed.rssV1Simple, Feed.rssV1Steam)
                    .map { async { ParseRSS.parse<RSSFeedObject>(it) } }
                    .awaitAll().forEach { assertion(it) }
            }
        }
    }

    private fun assertion(it: RSSFeedObject) = when (it.title) {
        "AAAA - RSS Channel - International Edition" -> {
            assertThat(it.items[0].title).isEqualTo("Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018")
            assertThat(it.items[1].title).isEqualTo("Pemda DIY raih penghargaan pada peringatan HAN 2018 di Surabaya")
        }

        "XML.com" -> {
            assertThat(it.items[0].title).isEqualTo("Processing Inclusions with XSLT")
            assertThat(it.items[1].title).isEqualTo("Putting RDF to Work")
        }

        "Steam RSS News Feed" -> {
            assertThat(it.items[0].title).isEqualTo("Team Fortress 2 Update Released")
            assertThat(it.items[1].title).isEqualTo("Team Fortress 2 Update Released")
        }

        else -> println("unknown")
    }
}
