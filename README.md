<h1 align="center">ParseRSS</h1>
<p align="center">
  <a href="https://jitpack.io/#muhrifqii/ParseRSS">
    <img src="https://jitpack.io/v/muhrifqii/ParseRSS.svg" />
  </a>
  <a href="http://kotlinlang.org">
    <img src="https://img.shields.io/badge/kotlin-2.2.20-yellow"/>
  </a>
  <a href="https://github.com/muhrifqii/ParseRSS/actions/workflows/verification.yml">
    <img src="https://github.com/muhrifqii/ParseRSS/actions/workflows/verification.yml/badge.svg" />
  </a>
  <a href="https://github.com/muhrifqii/ParseRSS/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-blue" />
  </a>  
  <a href="https://www.codacy.com/gh/muhrifqii/ParseRSS/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=muhrifqii/ParseRSS&amp;utm_campaign=Badge_Grade">
    <img src="https://app.codacy.com/project/badge/Grade/78c8325c65d649719dc627c1e803e647"/>
  </a>
  <a href="https://www.codefactor.io/repository/github/muhrifqii/parserss/overview/master"><img src="https://www.codefactor.io/repository/github/muhrifqii/parserss/badge/master" alt="CodeFactor" /></a>
</p>
<p align="center"><sup>RSS Parser for android</sup></p>
<br/>
<br/>

Simple, concise, and extensible RSS Parser in the entire coffee shop. It can capture these information from the RSS
article:

- RSS Version based on https://validator.w3.org/feed/docs/
  - [x] [Atom](https://xml2rfc.tools.ietf.org/public/rfc/html/rfc4287.html)
  - [x] [RSS V1](https://validator.w3.org/feed/docs/rss1.html#s5.2)
  - [x] [RSS V0.91](https://www.rssboard.org/rss-specification)
  - [x] [RSS V0.92](https://www.rssboard.org/rss-specification)
  - [x] [RSS V2](https://www.rssboard.org/rss-specification)
  - [x] Specific Version Handling
- RSS Namespace Checking
  - [x] Atom
  - [ ] DC
  - [x] Media
  - [x] RDF
  - [ ] SY
  - [ ] Specific namespaces
- Channel
  - [x] Title `<title>`
  - [x] Description `<description>`
  - [x] Link `<link>`
  - [x] Publication Date `<pubDate>`
  - [x] Image `<image>`
  - [x] Language `<language>`
  - [x] Copyright `<copyright>`
  - [x] Rights `<rights>`
  - [x] Last Build Date `<lastBuildDate>`
  - [x] Atom Link `<atom:link>`
  - [ ] TimeToLive `<ttl>`
  - [ ] SkipHours `<skipHours>`
  - [ ] SkipDays `<skipDays>`
  - [ ] Managing Editor `<managingEditor`

- Items Element
  - [x] Title `<title>`
  - [x] Description `<description>`
  - [x] Link `<link>`
  - [x] Item GUId `<guid>`
  - [x] Media Content _(NYT)_ `<media:content>`
  - [x] Media Credit _(NYT)_ `<media:credit>`
  - [x] Media Description _(NYT)_ `<media:description>`
  - [x] Publication Date `<pubDate>`
  - [x] Author `<author>`
  - [x] Categories `<category>`
  - [ ] Source `<source>`
  - [ ] Enclosure `<enclosure>`
  - [x] Atom Link `<atom:link>`
  - [ ] DC Creator _(NYT)_ `<dc:creator>`
  - [x] Comments `<comments>`

`ParseRSS` mainly has two main objects. `RSSFeedObject` and `RSSItemObject`. You can create your own parsing strategy by
implementing `RSSFeed` and `RSSItem`.

## Usage

ParseRSS depends on `XmlPullParser`, so feed it at least once in a lifetime. First thing first, the initialization part.
You can put it on your Application onCreate function.

```kotlin
ParseRSS.init(XmlPullParserFactory.newInstance())
```

Be aware that XmlPullParser could throw an exception even in initialization step.

Next, feed the RSS string into ParseRSS

```kotlin
val feed: RSSFeedObject = ParseRSS.parse(xml)
feed.items.forEach {
  print(it.title)
}
```

### ParseRSS as a Converter

ParseRSS does not have its own networking mechanism. Instead, it benefits from infamous networking library such as
[Retrofit](https://square.github.io/retrofit/), and [Fuel](https://github.com/kittinunf/fuel). By using
ConverterFactory, ParseRSS is ready to ship without breaking your project design pattern.

#### Fuel

Convert Fuel Response into RSSFeed by using `responseRss` function.

```kotlin
Fuel.get(URL).responseRss<RSSFeedObject> { result ->
  result.fold({ feed ->
    feed.items.forEach {
      print(it.title)
    }
  }, { error ->
    print(error)
  })
}
```

#### Retrofit

Convert Retrofit Response into RSSFeed by using `ParseRSSConverterFactory`

```kotlin
interface MyRssService {
    @GET("rss")
    fun rss(): Call<RSSFeedObject>
}
```

```kotlin
val retrofit = Retrofit.Builder()
    .addConverterFactory(ParseRSSConverterFactory.create<RSSFeedObject>())
    .baseUrl(BASE_URL)
    .build()

val rssService = retrofit.create(MyRssService::class.java)
rssService.rss().enqueue(object : Callback<RSSFeedObject> {
    override fun onFailure(call: Call<RSSFeedObject>, t: Throwable) {
    }
    override fun onResponse(call: Call<RSSFeedObject>, response: Response<RSSFeedObject>) {
    }
})
```

## Gradle Dependency

Add jitpack repository in your **root** `build.gradle` at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Used on Fuel

```gradle
// ParseRSS as Fuel Converter Factory
implementation "com.github.muhrifqii.ParseRSS:parserss:$version"
implementation "com.github.muhrifqii.ParseRSS:fuel:$version"
```

### Used on Retrofit

```gradle
// ParseRSS as Retrofit Converter Factory
implementation "com.github.muhrifqii.ParseRSS:parserss:$version"
implementation "com.github.muhrifqii.ParseRSS:retrofit:$version"
```

## License

```text
Copyright (c) 2019 Muhammad Rifqi Fatchurrahman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
