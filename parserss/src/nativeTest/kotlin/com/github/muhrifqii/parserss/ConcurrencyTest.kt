package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ConcurrencyTest {

    private fun assertion(it: RSSFeedObject) {
        when (it.title) {
            "AAAA - RSS Channel - International Edition" -> {
                assertEquals(
                    "Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018",
                    it.items[0].title
                )
                assertEquals(
                    "Pemda DIY raih penghargaan pada peringatan HAN 2018 di Surabaya",
                    it.items[1].title
                )
            }

            "XML.com" -> {
                assertEquals("Processing Inclusions with XSLT", it.items[0].title)
                assertEquals("Putting RDF to Work", it.items[1].title)
            }

            "Steam RSS News Feed" -> {
                assertEquals("Team Fortress 2 Update Released", it.items[0].title)
                assertEquals("Team Fortress 2 Update Released", it.items[1].title)
            }
        }
    }

    @Test
    fun parallelParsing() {
        runBlocking {
            coroutineScope {
                listOf(Feed.rssV2EnUS, Feed.rssV1Simple, Feed.rssV1Steam)
                    .map { feedXml ->
                        async(Dispatchers.Default) {
                            val result = parseRSS(feedXml).getOrThrow()
                            assertion(result)
                        }
                    }
                    .awaitAll()
            }
        }
    }

    @Test
    fun stress() {
        runBlocking(Dispatchers.Default) {
            repeat(10) { i ->
                println("Step $i")
                coroutineScope {
                    listOf(Feed.rssV2EnUS, Feed.rssV1Simple, Feed.rssV1Steam)
                        .map { async { parseRSS(it).getOrThrow() } }
                        .awaitAll().forEach { assertion(it) }
                }
            }
        }
    }
}