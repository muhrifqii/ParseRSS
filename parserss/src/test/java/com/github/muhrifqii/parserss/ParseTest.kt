package com.github.muhrifqii.parserss

import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

internal const val xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<!-- Generated on Mon, 19 Aug 2019 10:54:07 +0000 -->\n" +
        "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
        "  <channel>\n" +
        "    <atom:link href=\"http://dp3ap2.jogjaprov.go.id/frontend/index.php\" rel=\"self\" type=\"application/rss+xml\" />\n" +
        "    <title><![CDATA[AAAA - RSS Channel - International Edition]]></title>\n" +
        "    <link> https://dp3ap2.jogjaprov.go.id/</link>\n" +
        "    <description>website DP3AP2</description>" +
        "    <language>en-us</language>\n" +
        "    <image>\n" +
        "      <title>NYT &gt; World News</title>\n" +
        "      <url>https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png</url>\n" +
        "      <link>https://www.nytimes.com/section/world?emc=rss&amp;partner=rss</link>\n" +
        "    </image>" +
        "    <item>\n" +
        "      <title>Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018</title>\n" +
        "      <description>Yogyakarta, BPPM- Anak adalah masa depan dan generasi penerus bangsa.  Setiap anak perlu mendapatkan kesempatan yang seluas-luasnya untuk tumbuh dan berkembang secara optimal, baik fisik, mental ...</description>\n" +
        "      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018</link>\n" +
        "      <pubDate>05 Agustus 2019</pubDate>\n" +
        "      <media:content height=\"151\" medium=\"image\" url=\"https://static01.nyt.com/images/2019/08/25/world/25europe-climate/merlin_159634833_bcca0a79-3684-4fa2-9c42-b11095bc196e-moth.jpg\" width=\"151\"></media:content>\n" +
        "      <media:credit>Leo Correa/Associated Press</media:credit>\n" +
        "      <media:description>Under increasing international pressure to contain fires sweeping parts of the Amazon, President Jair Bolsonaro of Brazil on Friday authorized use of the military to battle the massive blazes.</media:description>" +
        "    </item>\n" +
        "    <item>\n" +
        "      <guid isPermaLink=\"false\">trump-g7-summit</guid>\n" +
        "      <title>Pemda DIY raih penghargaan pada peringatan HAN 2018 di Surabaya</title>\n" +
        "      <description>Yogyakarta, BPPM - Hari Anak Nasional merupakan hari yang sepenuhnya menjadi milik anak Indonesia, sehingga setiap anak Indonesia memiliki kesempatan seluas-luasnya untuk mengembangkan dan ...</description>\n" +
        "      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/245-pemda-diy-raih-penghargaan-pada-peringatan-han-2018-di-surabaya</link>\n" +
        "      <pubDate>05 Agustus 2019</pubDate>\n" +
        "      <media:group>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-super-169.jpg\" height=\"619\" width=\"1100\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-large-11.jpg\" height=\"300\" width=\"300\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-vertical-large-gallery.jpg\" height=\"552\" width=\"414\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-video-synd-2.jpg\" height=\"480\" width=\"640\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-live-video.jpg\" height=\"324\" width=\"576\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-t1-main.jpg\" height=\"250\" width=\"250\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-vertical-gallery.jpg\" height=\"360\" width=\"270\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-story-body.jpg\" height=\"169\" width=\"300\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-t1-main.jpg\" height=\"250\" width=\"250\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-assign.jpg\" height=\"186\" width=\"248\"/>\n" +
        "        <media:content medium=\"image\" url=\"https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-hp-video.jpg\" height=\"144\" width=\"256\"/>\n" +
        "      </media:group>" +
        "    </item>\n" +
        "  </channel>\n" +
        "</rss>"

class ParseTest {
    lateinit var feed: RSSFeedObject

    @Before
    fun configure() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
        feed = ParseRSS.parse(xml)
    }

    @Test
    fun validRSSFeed_Reader() {
        val reader = StringReader(xml)
        val feed: RSSFeedObject = ParseRSS.parse(reader)
        assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
    }

    @Test
    fun validateRSSFeed() {
        assertThat(feed.title).matches("AAAA - RSS Channel - International Edition")
        assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
        assertThat(feed.publishDate).isNull()
        assertThat(feed.image).isNotNull()
        assertThat(feed.image!!.imageUrl).matches("https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png")
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
        val item2 = feed.items[1]
        assertThat(item2.guId).isNotNull()
        assertThat(item2.guId!!.isPermaLink).isFalse()
        assertThat(item2.media).hasSize(11)
        assertThat(item2.media[10].height).isEqualTo(144)
    }
}