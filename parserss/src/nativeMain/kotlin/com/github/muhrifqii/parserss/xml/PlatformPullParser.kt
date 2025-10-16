package com.github.muhrifqii.parserss.xml

actual interface PlatformPullParser {
    /**
     * Set the input source for parsing
     */
    fun setInput(xml: CharIterator)
}
