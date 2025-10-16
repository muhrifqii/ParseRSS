package com.github.muhrifqii.parserss.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

/**
 * Android's XmlPullParser implementation of PullParser
 */
internal class AndroidPullParser(
    private val parser: XmlPullParser,
    override val isNamespaceAware: Boolean
) : PullParser {
    override fun setInput(reader: Reader) {
        parser.setInput(reader)
    }

    override val eventType: PullParserEventType
        get() = parser.eventType.intoEvent()

    override fun next(): PullParserEventType = parser.next().intoEvent()

    override fun nextToken(): PullParserEventType = parser.nextToken().intoEvent()

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
class AndroidPullParserFactory(
    private val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
) : PullParserFactory {

    override var isNamespaceAware: Boolean
        get() = factory.isNamespaceAware
        set(value) {
            factory.isNamespaceAware = value
        }

    override fun newPullParser(): PullParser {
        return AndroidPullParser(factory.newPullParser(), isNamespaceAware)
    }
}
