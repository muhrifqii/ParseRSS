package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.parser.executeRssParser
import com.github.muhrifqii.parserss.xml.AndroidPullParserFactory
import java.io.Reader

internal actual fun <R : RSSFeed> parseFn(
    reader: Reader,
    nsAware: Boolean,
    feed: R,
) = executeRssParser(
    feed = feed,
    factory = AndroidPullParserFactory()
        .apply { isNamespaceAware = nsAware }
) {
    parser.setInput(reader)
}
