package com.github.muhrifqii.parserss.xml

internal fun Int.intoEvent(): PullParserEventType =
    PullParserEventType.entries[this]
