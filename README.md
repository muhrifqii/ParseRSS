<p align="center">
  <h1 align="center">ParseRSS</h1>
</p>
<p align="center">
  <a href="https://jitpack.io/#muhrifqii/ParseRSS">
    <img src="https://jitpack.io/v/muhrifqii/ParseRSS.svg" />
  </a>
  <a href="https://travis-ci.org/muhrifqii/ParseRSS">
    <img src="https://travis-ci.org/muhrifqii/ParseRSS.svg?branch=master" />
  </a>
  <a href="https://github.com/muhrifqii/ParseRSS/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-BLUE.svg" />
  </a>  
  <a href="https://www.codacy.com/app/muhrifqii/ParseRSS?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=muhrifqii/ParseRSS&amp;utm_campaign=Badge_Grade">
    <img src="https://api.codacy.com/project/badge/Grade/80070eb57e6f456f9e89a4d65da0c7fd"/>
  </a>
</p>
<div align="center"><sup>RSS Parser for android<sup></div>
<br/>
<br/>

Simple, concise, and extensible RSS Parser in the entire coffee shop.
It can capture these information from the RSS article:
- [x] Title
- [x] Description
- [x] Link
- [x] Publication Date
- [x] Image
- [x] Item GUId
- [x] Language
- [ ] Media
- [ ] Author
- [ ] Categories

`ParseRSS` mainly has two main objects. `RSSFeedObject` and `RSSItemObject`.
You can create your own parsing strategy by implementing `RSSFeed` and `RSSItem`.
## Usage
ParseRSS depends on `XmlPullParser`, so feed it at least once in a lifetime.
First thing first, the initialization part. 
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
ParseRSS does not have its own networking mechanism.
Instead, it benefits from infamous networking library such as 
[Retrofit](https://square.github.io/retrofit/), and [Fuel](https://github.com/kittinunf/fuel).
By using ConverterFactory, ParseRSS is ready to ship without breaking your project design pattern.
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
```
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
