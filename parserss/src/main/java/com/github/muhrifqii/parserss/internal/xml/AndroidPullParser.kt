package com.github.muhrifqii.parserss.internal.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

/**
 * Android's XmlPullParser implementation of PullParser
 */
class AndroidPullParser(private val parser: XmlPullParser) : PullParser {
    override fun setInput(reader: Reader) {
        parser.setInput(reader)
    }

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
        parser.getAttributeValue(namespace, name)

    override fun nextText(): String = parser.nextText()
}

/**
 * XmlPullParserFactory implementation
 */
class AndroidPullParserFactory(private val factory: XmlPullParserFactory) :
    PullParserFactory {
    override var isNamespaceAware: Boolean
        get() = factory.isNamespaceAware
        set(value) {
            factory.isNamespaceAware = value
        }

    override fun newPullParser(): PullParser {
        return AndroidPullParser(factory.newPullParser())
    }
}
