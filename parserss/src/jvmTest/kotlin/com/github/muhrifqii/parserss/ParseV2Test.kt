package com.github.muhrifqii.parserss

import org.junit.jupiter.api.Test

class ParseV2Test : BaseParseV2FromStringTest() {

    override fun parseFromString(
        xml: String,
        namespaceAware: Boolean
    ): Result<RSSFeedObject> =
        parseRSS(xml, namespaceAware)

    @Test
    override fun `should correctly categorized as RSS v2`() {
        super.`should correctly categorized as RSS v2`()
    }

    @Test
    override fun `should correctly parse standard feed channel`() {
        super.`should correctly parse standard feed channel`()
    }

    @Test
    override fun `should correctly parse items`() {
        super.`should correctly parse items`()
    }

    @Test
    override fun `should able to parse description inside image tag`() {
        super.`should able to parse description inside image tag`()
    }
}