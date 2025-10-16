package com.github.muhrifqii.parserss

/**
 * Parse RSS/Atom feed from a String.
 *
 * @param xml The XML string to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @param feed Custom RSSFeed implementation
 * @return Result containing the parsed feed or an error
 */
fun <T : RSSFeed> parseRSS(
    xml: String,
    namespaceAware: Boolean = false,
    feed: T
): Result<T> = runCatching {
    parseRSSFromString(xml, namespaceAware, feed)
}

/**
 * Parse RSS/Atom feed from a String.
 *
 * @param xml The XML string to parse
 * @param namespaceAware Enable strict namespace checking (default: false)
 * @return Result containing the parsed feed or an error
 */
fun parseRSS(
    xml: String,
    namespaceAware: Boolean = false,
): Result<RSSFeedObject> = runCatching {
    parseRSSFromString(xml, namespaceAware, RSSFeedObject())
}

/**
 * Extension function to parse an XML string as RSS/Atom feed.
 */
inline fun <reified T : RSSFeed> String.parseAsRSS(
    namespaceAware: Boolean = false,
    feed: T = RSSFeedObject() as T
): Result<T> = parseRSS(this, namespaceAware, feed)

/**
 * Use [parseRSS] instead
 */
internal expect fun <T : RSSFeed> parseRSSFromString(
    xml: String,
    namespaceAware: Boolean,
    feed: T
): T
