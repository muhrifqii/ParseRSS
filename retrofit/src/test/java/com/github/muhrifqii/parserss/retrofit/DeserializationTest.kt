package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeedObject
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface TestRSSService {
    @GET("rss")
    fun rss(): Call<RSSFeedObject>
}

class DeserializationTest {
    @Before
    fun configure() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
    }

    @Test
    fun YiiRss() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(ParseRSSConverterFactory.create<RSSFeedObject>())
            .baseUrl("http://dp3ap2.jogjaprov.go.id/")
            .build()
        val service = retrofit.create(TestRSSService::class.java)
        service.rss().enqueue(object : Callback<RSSFeedObject> {
            override fun onFailure(call: Call<RSSFeedObject>, t: Throwable) {
            }

            override fun onResponse(call: Call<RSSFeedObject>, response: Response<RSSFeedObject>) {
                assertThat(response).isNotNull()
                val feed = response.body()!!
                assertThat(feed.title).matches("Berita di Portal DP3AP2")
                assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
                assertThat(feed.publishDate).isNull()
                assertThat(feed.items).isNotEmpty()
            }
        })
    }
}