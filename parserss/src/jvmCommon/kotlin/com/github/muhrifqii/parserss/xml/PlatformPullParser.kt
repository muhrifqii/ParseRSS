package com.github.muhrifqii.parserss.xml

import java.io.Reader

actual interface PlatformPullParser {
    /**
     * Set the input source for parsing
     */
    fun setInput(reader: Reader)
}
