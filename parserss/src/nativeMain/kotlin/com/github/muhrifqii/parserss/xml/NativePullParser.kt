package com.github.muhrifqii.parserss.xml

import org.kobjects.ktxml.api.XmlPullParser

/**
 * Jvm's XmlPullParser implementation of PullParser
 */
class NativePullParser(private val parser: XmlPullParser) : PullParser {

    override val eventType: Int
        get() = parser.eventType

    override fun next(): Int = parser.next()

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
