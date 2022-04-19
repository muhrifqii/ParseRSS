package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.samples.Feed
import org.junit.Assert
import org.junit.Test
import java.io.StringReader

class ExceptionTest : AbstractTest() {
    @Test()
    fun parseRSSMissingFactoryShouldThrow() {
        Assert.assertThrows(ParseRSSException::class.java) {
            ParseRSS.release()
            ParseRSS.parse<RSSFeedObject>(Feed.rssV2EnUS)
        }
    }

    @Test
    fun parseRSSIncompleteNamespaceUsingStrictModeShouldThrow() {
        Assert.assertThrows(ParseRSSException::class.java) {
            ParseRSS.parse(StringReader(Feed.rssV2EnUS), true) {
                RSSFeedObject()
            }
        }
    }

    override fun configure() {
        // ignore
    }
}
