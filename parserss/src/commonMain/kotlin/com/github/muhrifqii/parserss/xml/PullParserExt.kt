package com.github.muhrifqii.parserss.xml

import com.github.muhrifqii.parserss.element.ParseRSSAttribute
import com.github.muhrifqii.parserss.element.ParseRSSElement
import com.github.muhrifqii.parserss.element.toPrefixNamedElement

/**
 * Utility function to return a next text with trimmed text
 */
internal fun PullParser.nextTextTrimmed() = nextText().trim()

/**
 * Utility function to return a lowercase rss name, or empty string if name returns null
 */
internal fun PullParser.lowercaseName() = (name ?: "").lowercase()

/**
 * Utility function to return a lowercase rss prefix ns, or empty string if prefix returns null
 */
internal fun PullParser.lowercasePrefix() = (prefix ?: "").lowercase()

/**
 * Utility function to combine and return rss element name and prefix
 */
internal fun PullParser.getRSSElement(nsAware: Boolean = false): ParseRSSElement {
    return if (nsAware) {
        ParseRSSElement(lowercasePrefix(), lowercaseName())
    } else {
        lowercaseName().toPrefixNamedElement() as ParseRSSElement
    }
}

/**
 * Utility function to combine and return rss attribute name and prefix
 */
internal fun PullParser.getRSSAttributeElement(
    index: Int,
    nsAware: Boolean = false
): ParseRSSAttribute {
    val fxPrefix: () -> String = { (getAttributePrefix(index) ?: "").lowercase() }
    val fxName: () -> String = { (getAttributeName(index) ?: "").lowercase() }
    val fxValue: () -> String = { (getAttributeValue(index) ?: "") }
    return if (nsAware) {
        ParseRSSAttribute(fxPrefix(), fxName(), fxValue())
    } else {
        val holder = fxName().toPrefixNamedElement()
        ParseRSSAttribute(holder.prefix, holder.name, fxValue())
    }
}

internal fun Int.intoEvent(): PullParserEventType =
    PullParserEventType.entries[this]
