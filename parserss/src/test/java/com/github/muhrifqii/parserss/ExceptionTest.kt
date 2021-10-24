package com.github.muhrifqii.parserss

import org.junit.Assert
import org.junit.Test

class ExceptionTest {
    @Test()
    fun ParseRSSThrowException() {
        Assert.assertThrows(ParseRSSException::class.java) {
            ParseRSS.release()
            ParseRSS.parse<RSSFeedObject>(xml)
        }
    }
}
