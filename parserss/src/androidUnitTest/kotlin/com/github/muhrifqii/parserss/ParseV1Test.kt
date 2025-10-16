package com.github.muhrifqii.parserss

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class ParseV1Test : BaseParseV1FromStringTest() {
    override fun parseFromString(
        xml: String,
        namespaceAware: Boolean
    ): Result<RSSFeedObject> = parseRSS(xml, namespaceAware)

    @Test
    override fun `should correctly categorized as RSS v1`() {
        super.`should correctly categorized as RSS v1`()
    }

    @Test
    override fun `should correctly parse standard feed channel`() {
        super.`should correctly parse standard feed channel`()
    }

    @Test
    override fun `should correctly parse items`() {
        super.`should correctly parse items`()
    }
}