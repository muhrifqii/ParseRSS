package com.github.muhrifqii.parserss.xml

/**
 * Abstraction interface for XML parsing to enable platform-independent implementations.
 * This allows swapping between different XML parser implementations (e.g., XmlPullParser, ktxml).
 */
interface PullParser : PlatformPullParser {

    /**
     * Get the current event type
     */
    val eventType: PullParserEventType

    /**
     * Move to the next parsing event
     */
    fun next(): PullParserEventType

    /**
     * Move to the next parsing event in a token level
     */
    fun nextToken(): PullParserEventType

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

    /**
     * Is aware of namespace when parsing the element
     */
    val isNamespaceAware: Boolean
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
