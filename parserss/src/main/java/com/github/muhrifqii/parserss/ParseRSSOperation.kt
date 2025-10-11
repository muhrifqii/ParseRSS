package com.github.muhrifqii.parserss

import java.io.IOException
import java.io.Reader

interface ParseRSSOperation {
    /**
     * Release Parser Factory
     */
    fun release()

    /**
     * Parse RSS from Reader Object
     */
    @Throws(ParseRSSException::class, IOException::class)
    fun <R : RSSFeed> parse(xml: Reader): R

    /**
     * Parse RSS from xml String
     */
    @Throws(ParseRSSException::class)
    fun <R : RSSFeed> parse(xml: String): R

    /**
     * Parse RSS customizable
     */
    @Throws(ParseRSSException::class)
    fun <T : RSSFeed> parse(
        xml: Reader,
        strictlyNamespaceChecking: Boolean,
        feedSupplier: () -> T
    ): T
}
