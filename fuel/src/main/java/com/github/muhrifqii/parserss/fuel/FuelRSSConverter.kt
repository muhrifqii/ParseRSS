package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.ResultHandler
import com.github.kittinunf.fuel.core.await
import com.github.kittinunf.fuel.core.awaitResult
import com.github.kittinunf.fuel.core.response
import com.github.muhrifqii.parserss.RSSFeed
import com.github.muhrifqii.parserss.RSSFeedObject
import com.github.muhrifqii.parserss.parseRSS
import java.io.Reader

/**
 * Deserialize the [com.github.kittinunf.fuel.core.Response] into a [RSSFeed] using **ParseRSS**
 */
fun Request.responseRss(handler: ResultHandler<RSSFeedObject>) =
    response(parseRss(), handler)

/**
 * Deserialize the [com.github.kittinunf.fuel.core.Response] into a [RSSFeed] using **ParseRSS**
 */
suspend fun Request.awaitRss() =
    await(parseRss())

/**
 * Deserialize the [com.github.kittinunf.fuel.core.Response] into a [RSSFeed] using **ParseRSS**
 */
suspend fun Request.awaitRssResult() =
    awaitResult(parseRss())


internal fun parseRss(): ResponseDeserializable<RSSFeedObject> =
    object : ResponseDeserializable<RSSFeedObject> {
        override fun deserialize(reader: Reader): RSSFeedObject {
            return parseRSS(reader).getOrThrow()
        }
    }