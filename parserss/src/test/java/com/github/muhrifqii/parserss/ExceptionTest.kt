package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import org.junit.Assert
import org.junit.Test

class ExceptionTest : AbstractTest() {
    @Test()
    fun parseRSSThrowException() {
        Assert.assertThrows(ParseRSSException::class.java) {
            ParseRSS.release()
            ParseRSS.parse<RSSFeedObject>(Feed.rssV2EnUS)
        }
    }

    override fun configure() {
        // ignore
    }
}
