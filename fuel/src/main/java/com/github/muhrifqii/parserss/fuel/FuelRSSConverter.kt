package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.ResponseHandler
import com.github.kittinunf.fuel.core.ResponseResultHandler
import com.github.kittinunf.fuel.core.ResultHandler
import com.github.kittinunf.fuel.core.response
import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeed
import java.io.Reader

/**
 * Deserialize the [Response] into a [RSSFeed] using [ParseRSS]
 */
fun Request.responseRss() = response(parseRss())

/**
 * Deserialize the [Response] into a [RSSFeed] using [ParseRSS]
 */
inline fun <reified T : RSSFeed> Request.responseRss(handler: ResponseHandler<T>) =
    response(parseRss(), handler)

/**
 * Deserialize the [Response] into a [RSSFeed] using [ParseRSS]
 */
inline fun <reified T : RSSFeed> Request.responseRss(noinline handler: ResultHandler<T>) =
    response(parseRss(), handler)

/**
 * Deserialize the [Response] into a [RSSFeed] using [ParseRSS]
 */
inline fun <reified T : RSSFeed> Request.responseRss(noinline handler: ResponseResultHandler<T>) =
    response(parseRss(), handler)

/**
 * Inlined function for providing [ParseRSS] parser on to [ResponseDeserializable]
 */
inline fun <reified T : RSSFeed> parseRss(): ResponseDeserializable<T> =
    object : ResponseDeserializable<T> {
        override fun deserialize(content: String): T {
            return ParseRSS.parse(content)
        }

        override fun deserialize(reader: Reader): T {
            return ParseRSS.parse(reader)
        }
    }
