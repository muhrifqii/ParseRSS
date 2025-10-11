package com.github.muhrifqii.parserss.xml

import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParser

/**
 * Jvm's XmlPullParser implementation of PullParser
 */
class NativePullParser(private val parser: XmlPullParser) : PullParser {

    override val eventType: PullParserEventType
        get() = parser.eventType.mapEventType()

    override fun next(): PullParserEventType = parser.next().mapEventType()

    override fun nextToken(): PullParserEventType = parser.nextToken().mapEventType()

    override val name: String?
        get() = parser.name

    override val prefix: String?
        get() = parser.prefix

    override val attributeCount: Int
        get() = parser.attributeCount

    override fun getAttributeName(index: Int): String? = parser.getAttributeName(index)

    override fun getAttributePrefix(index: Int): String? = parser.getAttributePrefix(index)

    override fun getAttributeValue(index: Int): String? = parser.getAttributeValue(index)

    override fun getAttributeValue(namespace: String?, name: String): String? =
        parser.getAttributeValue(namespace ?: "", name)

    override fun nextText(): String = parser.nextText()
}

/**
 * Map event type
 */
private fun EventType.mapEventType() = with(this) {
    PullParserEventType.entries[ordinal]
}
