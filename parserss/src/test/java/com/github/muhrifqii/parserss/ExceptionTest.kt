package com.github.muhrifqii.parserss

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ExceptionTest {
    @get:Rule
    val exception = ExpectedException.none()

    @Test
    fun ParseRSS_ThrowException() {
        exception.expect(ParseRSSException::class.java)
        exception.expectMessage("xmlPullParserFactory is null. Should call ParseRSS.init() once.")
        ParseRSS.parse<RSSFeedObject>(xml)
    }
}