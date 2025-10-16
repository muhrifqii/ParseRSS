package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import org.junit.jupiter.api.Test
import org.xmlpull.v1.XmlPullParserException
import kotlin.test.assertFailsWith

class ExceptionTest {
    @Test
    fun `should throw on namespace aware mode parsing`() {
        assertFailsWith<XmlPullParserException> {
            parseRSS(Feed.rssV2EnUS, true).getOrThrow()
        }
    }
}