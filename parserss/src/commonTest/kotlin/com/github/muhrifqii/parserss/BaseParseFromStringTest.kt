package com.github.muhrifqii.parserss

abstract class BaseParseFromStringTest {
    protected abstract fun parseFromString(
        xml: String,
        namespaceAware: Boolean = false
    ): Result<RSSFeedObject>

}