package com.github.muhrifqii.parserss

interface FromStringParser {
    fun parseFromString(
        xml: String,
        namespaceAware: Boolean = false
    ): Result<RSSFeedObject>
}