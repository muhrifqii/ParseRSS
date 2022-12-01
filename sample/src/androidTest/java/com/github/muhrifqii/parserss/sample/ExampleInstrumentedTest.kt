package com.github.muhrifqii.parserss.sample

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeedObject
import com.github.muhrifqii.parserss.retrofit.ParseRSSConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.* // ktlint-disable no-wildcard-imports
import org.junit.Test
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.* // ktlint-disable no-wildcard-imports
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.github.muhrifqii.parserss.sample", appContext.packageName)
    }

    @Test
    fun testGetRss() {
        // Init ParseRSS
        ParseRSS.init(XmlPullParserFactory.newInstance())

        // Init params
        val url = "https://www.albipretori.it/Public/Rss?Codice=192"
        val baseUrl = "https://www.google.com/"

        // Init retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ParseRSSConverterFactory.create<RSSFeedObject>())
            .build()
            .create(RssApiInterface::class.java)

        // Make the request
        runBlocking {
            val response = RssApi.getRss(retrofit, url).awaitResponse().body()
            println("HERE full response $response")
            println(
                "HERE image urls ${ response!!.items.joinToString(", ") {
                    it.imageUrls.joinToString(" --- ")
                }
                }"
            )
            println(
                "HERE publishDate ${
                response.items.joinToString(", ") { it.publishDate ?: "" }
                }"
            )
            assert(true)
        }
    }
}

interface RssApiInterface {
    @GET
    fun getRss(@Url url: String): Call<RSSFeedObject>
}

object RssApi {
    fun getRss(service: RssApiInterface, url: String): Call<RSSFeedObject> {
        return service.getRss(url)
    }
}
