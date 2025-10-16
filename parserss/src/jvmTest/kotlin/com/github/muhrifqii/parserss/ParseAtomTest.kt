package com.github.muhrifqii.parserss

import org.junit.jupiter.api.Test


class ParseAtomTest : BaseParseAtomFromStringTest() {
    override fun parseFromString(
        xml: String,
        namespaceAware: Boolean
    ): Result<RSSFeedObject> = parseRSS(xml, namespaceAware)

    @Test
    override fun `should correctly categorized as RSS atom`() {
        super.`should correctly categorized as RSS atom`()
    }

    @Test
    override fun `should correctly parse standard feed and items`() {
        super.`should correctly parse standard feed and items`()
    }
}