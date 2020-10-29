package com.github.muhrifqii.parserss

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ExceptionTest {
    @get:Rule
    val exception = ExpectedException.none()

    @Test()
    fun ParseRSS_ThrowException() {
        exception.expect(ParseRSSException::class.java)
        try {
            ParseRSS.parse<RSSFeedObject>(xml)
        } catch (err: ParseRSSException) {
            throw ParseRSSException(err.message!!)
        }
    }
}
