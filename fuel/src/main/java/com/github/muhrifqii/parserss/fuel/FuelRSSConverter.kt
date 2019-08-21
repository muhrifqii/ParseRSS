package com.github.muhrifqii.parserss.fuel

import com.github.kittinunf.fuel.core.*
import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeed
import java.io.Reader

fun Request.responseRss() = response(parseRss())

inline fun <reified T : RSSFeed> Request.responseRss(handler: ResponseHandler<T>) =
    response(parseRss(), handler)

inline fun <reified T : RSSFeed> Request.responseRss(noinline handler: ResultHandler<T>) =
    response(parseRss(), handler)

inline fun <reified T : RSSFeed> Request.responseRss(noinline handler: ResponseResultHandler<T>) =
    response(parseRss(), handler)

inline fun <reified T : RSSFeed> parseRss(): ResponseDeserializable<T> =
    object : ResponseDeserializable<T> {
        override fun deserialize(content: String): T? {
            // TODO: ParseRSS param as Reader
            return ParseRSS.parse(content)
        }

        override fun deserialize(reader: Reader): T? {
            return ParseRSS.parse(reader)
        }
    }