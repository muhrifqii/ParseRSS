package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.RSSFeedObject
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface TestRSSService {
    @GET("rss")
    fun rss(): Call<RSSFeedObject>
}

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DeserializationTest {

    @Test
    fun yiiRss() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(ParseRSSConverterFactory.create())
            .baseUrl("http://dp3ap2.jogjaprov.go.id/")
            .build()
        val service = retrofit.create(TestRSSService::class.java)
        service.rss().enqueue(object : Callback<RSSFeedObject> {
            override fun onFailure(call: Call<RSSFeedObject>, t: Throwable) {
                print(t.message)
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
