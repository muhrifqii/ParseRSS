package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.RSSFeedObject
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

interface TestRSSService {
    @GET("rss")
    fun rss(): Call<RSSFeedObject>

    @GET("async-rss")
    suspend fun awaitRss(): RSSFeedObject
}

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DeserializationTest {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(TestInterceptor)
        .build()
    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(ParseRSSConverterFactory.create())
        .baseUrl("https://someapi.com/rss/")
        .client(okHttpClient)
        .build()
    val service: TestRSSService = retrofit.create(TestRSSService::class.java)


    @Test
    fun `should be able to parse RSS on retrofit on non-suspending function`() {
        val latch = CountDownLatch(1)
        var testPassed = false

        service.rss().enqueue(object : Callback<RSSFeedObject> {
            override fun onFailure(call: Call<RSSFeedObject>, t: Throwable) {
                print(t.message)
                testPassed = false
                latch.countDown()
            }

            override fun onResponse(call: Call<RSSFeedObject>, response: Response<RSSFeedObject>) {
                assertThat(response).isNotNull()
                val feed = response.body()!!
                assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
                assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                assertThat(feed.publishDate).isNull()
                assertThat(feed.items).isNotEmpty()
                testPassed = true
                latch.countDown()
            }
        })
        latch.await(2, TimeUnit.SECONDS)
        assertThat(testPassed).isTrue()
    }

    @Test
    fun `should be able to parse RSS on retrofit on suspending function`() {
        runBlocking {
            val feed = service.awaitRss()
            assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
            assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
            assertThat(feed.publishDate).isNull()
            assertThat(feed.items).isNotEmpty()
        }
    }
}
