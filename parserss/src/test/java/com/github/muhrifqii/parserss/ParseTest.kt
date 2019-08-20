package com.github.muhrifqii.parserss

import com.google.common.truth.Truth.*
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory

internal const val xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<!-- Generated on Mon, 19 Aug 2019 10:54:07 +0000 -->\n" +
        "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
        "  <channel>\n" +
        "    <atom:link href=\"http://dp3ap2.jogjaprov.go.id/frontend/index.php\" rel=\"self\" type=\"application/rss+xml\" />\n" +
        "    <title>Berita di Portal DP3AP2</title>\n" +
        "    <link> https://dp3ap2.jogjaprov.go.id/</link>\n" +
        "    <description>website DP3AP2</description>" +
        "    <item>\n" +
        "      <title>Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018</title>\n" +
        "      <description>Yogyakarta, BPPM- Anak adalah masa depan dan generasi penerus bangsa.  Setiap anak perlu mendapatkan kesempatan yang seluas-luasnya untuk tumbuh dan berkembang secara optimal, baik fisik, mental ...</description>\n" +
        "      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018</link>\n" +
        "      <pubDate>05 Agustus 2019</pubDate>\n" +
        "    </item>\n" +
        "    <item>\n" +
        "      <title>Pemda DIY raih penghargaan pada peringatan HAN 2018 di Surabaya</title>\n" +
        "      <description>Yogyakarta, BPPM - Hari Anak Nasional merupakan hari yang sepenuhnya menjadi milik anak Indonesia, sehingga setiap anak Indonesia memiliki kesempatan seluas-luasnya untuk mengembangkan dan ...</description>\n" +
        "      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/245-pemda-diy-raih-penghargaan-pada-peringatan-han-2018-di-surabaya</link>\n" +
        "      <pubDate>05 Agustus 2019</pubDate>\n" +
        "    </item>\n" +
        "  </channel>\n" +
        "</rss>"

class ParseTest {
    @Test
    fun RSSFeed_ValidFeed() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
        val feed: RSSFeedObject = ParseRSS.parse(xml)
        assertThat(feed.title).matches("Berita di Portal DP3AP2")
        assertThat(feed.link).matches("https://dp3ap2.jogjaprov.go.id/")
        assertThat(feed.publishDate).isNull()
        assertThat(feed.items).hasSize(2)
    }

    @Test
    fun RSSItem_ValidItem() {
        ParseRSS.init(XmlPullParserFactory.newInstance())
        val feed: RSSFeedObject = ParseRSS.parse(xml)
        val item1 = feed.items[0]
        assertThat(item1.title).matches("Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018")
        assertThat(item1.link).matches("http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018")
        assertThat(item1.publishDate).matches("05 Agustus 2019")
        assertThat(item1.description).startsWith("Yogyakarta, BPPM- Anak adalah")
    }
}