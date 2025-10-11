package com.github.muhrifqii.parserss.internal

import com.github.muhrifqii.parserss.RSSFeed
import com.github.muhrifqii.parserss.RSSObject

data class ParsingContext(
    var currentObject: RSSObject? = null,
    val objectStack: MutableList<RSSObject> = mutableListOf(),
    val feed: RSSFeed,
)
