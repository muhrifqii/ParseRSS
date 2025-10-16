package com.github.muhrifqii.parserss

import java.io.Reader
import java.io.StringReader

/**
 * Parse RSS/Atom feed from a Reader.
 *
 * @param xml The XML Reader to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @param feed Custom RSSFeed implementation (default: RSSFeedObject)
 * @return Result containing the parsed feed or an error
 */
fun <T : RSSFeed> parseRSS(
    xml: Reader,
    namespaceAware: Boolean = false,
    feed: T
): Result<T> = runCatching {
    parseRSSFromReader(xml, namespaceAware, feed)
}

/**
 * Parse RSS/Atom feed from a Reader.
 *
 * @param xml The XML Reader to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @return Result containing the parsed feed or an error
 */
fun parseRSS(
    xml: Reader,
    namespaceAware: Boolean = false,
): Result<RSSFeedObject> = runCatching {
    parseRSSFromReader(xml, namespaceAware, RSSFeedObject())
}

internal actual fun <T : RSSFeed> parseRSSFromString(
    xml: String,
    namespaceAware: Boolean,
    feed: T
): T = parseFn(StringReader(xml), namespaceAware, feed)

internal fun <T : RSSFeed> parseRSSFromReader(
    xml: Reader,
    namespaceAware: Boolean,
    feed: T
): T = parseFn(xml, namespaceAware, feed)

internal expect fun <R : RSSFeed> parseFn(
    reader: Reader,
    nsAware: Boolean = false,
    feed: R,
): R
