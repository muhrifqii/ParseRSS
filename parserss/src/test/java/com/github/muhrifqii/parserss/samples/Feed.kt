package com.github.muhrifqii.parserss.samples

object Feed {
    const val rssV2EnUS = """<?xml version="1.0" encoding="utf-8"?>
<!-- Generated on Mon, 19 Aug 2019 10:54:07 +0000 -->
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
   <channel>
    <atom:link href="http://dp3ap2.jogjaprov.go.id/frontend/index.php" rel="self" type="application/rss+xml" />
    <title><![CDATA[AAAA - RSS Channel - International Edition]]></title>
    <link> https://dp3ap2.jogjaprov.go.id/</link>
    <description>website DP3AP2</description>    <language>en-us</language>
    <image>
      <title>NYT &gt; World News</title>
      <url>https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png</url>
      <link>https://www.nytimes.com/section/world?emc=rss&amp;partner=rss</link>
    </image>    
    <item>
      <title>Puncak Peringatan Hari Anak Nasional D.I. Yogyakarta 2018</title>
      <description>Yogyakarta, BPPM- Anak adalah masa depan dan generasi penerus bangsa.  Setiap anak perlu mendapatkan kesempatan yang seluas-luasnya untuk tumbuh dan berkembang secara optimal, baik fisik, mental ...</description>
      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/246-puncak-peringatan-hari-anak-nasional-han-d-i-yogyakarta-2018</link>
      <pubDate>05 Agustus 2019</pubDate>
      <media:content height="151" medium="image" url="https://static01.nyt.com/images/2019/08/25/world/25europe-climate/merlin_159634833_bcca0a79-3684-4fa2-9c42-b11095bc196e-moth.jpg" width="151"></media:content>
      <media:credit>Leo Correa/Associated Press</media:credit>
      <media:description>Under increasing international pressure to contain fires sweeping parts of the Amazon, President Jair Bolsonaro of Brazil on Friday authorized use of the military to battle the massive blazes.</media:description>      
      <author>John Doe</author>      
      <category>News</category>      
      <category domain="http://www.nytimes.com/namespaces/keywords/nyt_geo">Indonesia</category>    
    </item>
    <item>
      <guid isPermaLink="false">trump-g7-summit</guid>
      <title>Pemda DIY raih penghargaan pada peringatan HAN 2018 di Surabaya</title>
      <description>Yogyakarta, BPPM - Hari Anak Nasional merupakan hari yang sepenuhnya menjadi milik anak Indonesia, sehingga setiap anak Indonesia memiliki kesempatan seluas-luasnya untuk mengembangkan dan ...</description>
      <link>http://dp3ap2.jogjaprov.go.id/berita/detail/245-pemda-diy-raih-penghargaan-pada-peringatan-han-2018-di-surabaya</link>
      <pubDate>05 Agustus 2019</pubDate>
      <media:group>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-super-169.jpg" height="619" width="1100"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-large-11.jpg" height="300" width="300"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-vertical-large-gallery.jpg" height="552" width="414"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-video-synd-2.jpg" height="480" width="640"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-live-video.jpg" height="324" width="576"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-t1-main.jpg" height="250" width="250"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-vertical-gallery.jpg" height="360" width="270"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-story-body.jpg" height="169" width="300"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-t1-main.jpg" height="250" width="250"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-assign.jpg" height="186" width="248"/>
        <media:content medium="image" url="https://cdn.cnn.com/cnnnext/dam/assets/190823131204-lyubov-sobol-hp-video.jpg" height="144" width="256"/>
      </media:group>    
    </item>
  </channel>
</rss>"""

    const val rssV1Simple = """<?xml version="1.0"?>
<rdf:RDF 
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns="http://purl.org/rss/1.0/"
>

  <channel rdf:about="http://www.xml.com/xml/news.rss">
    <title>XML.com</title>
    <link>http://xml.com/pub</link>
    <description>
      XML.com features a rich mix of information and services 
      for the XML community.
    </description>

    <image rdf:resource="http://xml.com/universal/images/xml_tiny.gif" />

    <items>
      <rdf:Seq>
        <rdf:li resource="http://xml.com/pub/2000/08/09/xslt/xslt.html" />
        <rdf:li resource="http://xml.com/pub/2000/08/09/rdfdb/index.html" />
      </rdf:Seq>
    </items>

  </channel>
  <image rdf:about="http://xml.com/universal/images/xml_tiny.gif">
    <title>XML.com</title>
    <link>http://www.xml.com</link>

    <url>http://xml.com/universal/images/xml_tiny.gif</url>
  </image>
  <item rdf:about="http://xml.com/pub/2000/08/09/xslt/xslt.html">
    <title>Processing Inclusions with XSLT</title>
    <link>http://xml.com/pub/2000/08/09/xslt/xslt.html</link>

    <description>
     Processing document inclusions with general XML tools can be 
     problematic. This article proposes a way of preserving inclusion 
     information through SAX-based processing.
    </description>
  </item>
    <item rdf:about="http://xml.com/pub/2000/08/09/rdfdb/index.html">
    <title>Putting RDF to Work</title>

    <link>http://xml.com/pub/2000/08/09/rdfdb/index.html</link>
    <description>
     Tool and API support for the Resource Description Framework 
     is slowly coming of age. Edd Dumbill takes a look at RDFDB, 
     one of the most exciting new RDF toolkits.
    </description>
  </item>

</rdf:RDF>
        """

    const val rssV1Steam = """
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:sy="http://purl.org/rss/1.0/modules/syndication/" xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:cc="http://web.resource.org/cc/" xmlns:steam="http://www.steampowered.com" xmlns="http://purl.org/rss/1.0/">
<channel rdf:about="http://www.steampowered.com">
<title>Steam RSS News Feed</title>
<link>http://www.steampowered.com/</link>
<description>All Steam news, all the time!</description>
<language>en</language>
<pubDate>Wed, 30 Mar 2022 14:29:00 -0700</pubDate>
<generator>http://www.steampowered.com/</generator>
<items>
<rdf:Seq>
<rdf:li rdf:resource="https://store.steampowered.com/news/136588/"/>
<rdf:li rdf:resource="https://store.steampowered.com/news/136511/"/>
</rdf:Seq>
</items>
</channel>
<item rdf:about="https://store.steampowered.com/news/136588/">
<title>
<![CDATA[ Team Fortress 2 Update Released ]]>
</title>
<link>
<![CDATA[ https://store.steampowered.com/news/136588/ ]]>
</link>
<guid isPermaLink="true">
<![CDATA[ https://store.steampowered.com/news/136588/ ]]>
</guid>
<comments>https://steamcommunity.com//discussions/</comments>
<pubDate>Wed, 30 Mar 2022 14:29:00 -0700</pubDate>
<author>Valve</author>
<category>Valve news update</category>
<dc:subject>Valve news update</dc:subject>
<dc:creator>Valve</dc:creator>
<dc:date>2022-03-30T14:29:00-0700</dc:date>
<content:encoded>
<![CDATA[ An update to Team Fortress 2 has been released. The update will be applied automatically when you restart Team Fortress 2. The major changes include:<br/><br><ul style="padding-bottom: 0px; margin-bottom: 0px;" ><li>Updated Taunt: Shooter's Stakeout to fix LOD bugs<br><li>Updated class portraits with nomip/nolod flags<br></ul> ]]>
</content:encoded>
</item>
<item rdf:about="https://store.steampowered.com/news/136511/">
<title>
<![CDATA[ Team Fortress 2 Update Released ]]>
</title>
<link>
<![CDATA[ https://store.steampowered.com/news/136511/ ]]>
</link>
<guid isPermaLink="true">
<![CDATA[ https://store.steampowered.com/news/136511/ ]]>
</guid>
<comments>https://steamcommunity.com//discussions/</comments>
<pubDate>Tue, 29 Mar 2022 09:58:00 -0700</pubDate>
<author>Valve</author>
<category>Valve news update</category>
<dc:subject>Valve news update</dc:subject>
<dc:creator>Valve</dc:creator>
<dc:date>2022-03-29T09:58:00-0700</dc:date>
<content:encoded>
<![CDATA[ An update to Team Fortress 2 has been released. The update will be applied automatically when you restart Team Fortress 2. The major changes include:<br/><br><ul style="padding-bottom: 0px; margin-bottom: 0px;" ><li>Fixed particle effects regression when players are killed<br><li>Updated/Added some tournament medals<br><li>Updated the localization files<br></ul> ]]>
</content:encoded>
</item>
    """
}
