package com.github.muhrifqii.parserss.internal.xml

import java.io.Reader

/**
 * Abstraction interface for XML parsing to enable platform-independent implementations.
 * This allows swapping between different XML parser implementations (e.g., XmlPullParser, ktxml).
 */
interface PullParser {
    /**
     * Set the input source for parsing
     */
    fun setInput(reader: Reader)

    /**
     * Get the current event type
     */
    val eventType: Int

    /**
     * Move to the next parsing event
     */
    fun next(): Int

    /**
     * Get the name of the current element
     */
    val name: String?

    /**
     * Get the prefix of the current element
     */
    val prefix: String?

    /**
     * Get the number of attributes on the current element
     */
    val attributeCount: Int

    /**
     * Get attribute name at the specified index
     */
    fun getAttributeName(index: Int): String?

    /**
     * Get attribute prefix at the specified index
     */
    fun getAttributePrefix(index: Int): String?

    /**
     * Get attribute value at the specified index
     */
    fun getAttributeValue(index: Int): String?

    /**
     * Get attribute value by namespace and name
     */
    fun getAttributeValue(namespace: String?, name: String): String?

    /**
     * Get the text content of the current element
     */
    fun nextText(): String
}

/**
 * Factory interface for creating PullParser instances
 */
interface PullParserFactory {
    /**
     * Set whether the parser should be namespace aware
     */
    var isNamespaceAware: Boolean

    /**
     * Create a new parser instance
     */
    fun newPullParser(): PullParser
}

