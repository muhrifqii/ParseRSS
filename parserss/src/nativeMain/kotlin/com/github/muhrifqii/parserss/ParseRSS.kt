package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.parser.executeRssParser
import com.github.muhrifqii.parserss.xml.NativePullParserFactory

/**
 * Parse RSS/Atom feed from a CharIterator.
 *
 * @param xml The XML CharIterator to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @param feed Custom RSSFeed implementation
 * @return Result containing the parsed feed or an error
 */
fun <T : RSSFeed> parseRSS(
    xml: CharIterator,
    namespaceAware: Boolean = false,
    feed: T
): Result<T> = runCatching {
    parseRSSFromCharIterator(xml, namespaceAware, feed)
}

/**
 * Parse RSS/Atom feed from a CharIterator.
 *
 * @param xml The XML CharIterator to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @return Result containing the parsed feed or an error
 */
fun parseRSS(
    xml: CharIterator,
    namespaceAware: Boolean = false,
): Result<RSSFeedObject> = runCatching {
    parseRSSFromCharIterator(xml, namespaceAware, RSSFeedObject())
}

internal actual fun <T : RSSFeed> parseRSSFromString(
    xml: String,
    namespaceAware: Boolean,
    feed: T
): T = parseFn(xml.iterator(), namespaceAware, feed)

internal fun <T : RSSFeed> parseRSSFromCharIterator(
    xml: CharIterator,
    namespaceAware: Boolean,
    feed: T
): T = parseFn(xml, namespaceAware, feed)

internal fun <R : RSSFeed> parseFn(
    xml: CharIterator,
    nsAware: Boolean,
    feed: R,
) = executeRssParser(
    feed = feed,
    factory = NativePullParserFactory()
        .apply { isNamespaceAware = nsAware }
) {
    parser.setInput(xml)
}
