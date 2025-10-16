package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.requests.DefaultBody
import com.github.kittinunf.fuel.toolbox.HttpClient
import com.github.muhrifqii.parserss.fuel.samples.Feed
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DeserializationTest {

    val fuelClient = mockk<HttpClient>()

    // https://github.com/kittinunf/fuel/issues/186 doesnt even work
    // because ResponseDeserializable calling internal variable, causing mockk issue
    @Before
    fun prepare() {
        val rssString = Feed.rssV2EnUS
        val rssData = rssString.toByteArray()
        val mockResponseBody = mockk<DefaultBody>(relaxed = true).apply {
            every { toStream() } answers { ByteArrayInputStream(rssData) }
        }
        val response = Response(
            URL("https://api.com/rss"),
            200,
            "OK",
            contentLength = rssData.size.toLong(),
            body = mockResponseBody,
        )

        every { fuelClient.executeRequest(any()) } returns response
        coEvery { fuelClient.awaitRequest(any()) } returns response

        FuelManager.instance.client = fuelClient
    }

    @Test
    fun `should be able to parse RSS on Fuel using Future callback`() {
        val url = "https://api.com/rss"
        val latch = CountDownLatch(1)
        var testPassed = false

        Fuel.get(url).responseRss { result ->
            result.fold({ feed ->
                assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
                assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                assertThat(feed.publishDate).isNull()
                assertThat(feed.items).isNotEmpty()
                testPassed = true
            }, {
                print(it)
                testPassed = false
            })
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
        assertThat(testPassed).isTrue()
    }

    @Test
    fun `should be able to parse RSS on Fuel using coroutine`() {
        runBlocking {
            val url = "https://api.com/rss"
            Fuel.get(url)
                .awaitRss().let { feed ->
                    assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
                    assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                    assertThat(feed.publishDate).isNull()
                    assertThat(feed.items).isNotEmpty()
                }
        }
    }

    @Test
    fun `should return Result of parsed RSS on Fuel using coroutine`() {
        runBlocking {
            val url = "https://api.com/rss"
            Fuel.get(url)
                .awaitRssResult()
                .fold({ feed ->
                    assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
                    assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                    assertThat(feed.publishDate).isNull()
                    assertThat(feed.items).isNotEmpty()
                }, {
                    print(it)
                    throw AssertionError(it)
                })
        }
    }
}
