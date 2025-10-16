package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ExceptionTest {
    @Test
    fun parseRSSIncompleteNamespaceUsingStrictModeShouldThrow() {
        assertFailsWith<Exception> {
            parseRSS(Feed.rssV2EnUS, true).getOrThrow()
        }
    }
}
