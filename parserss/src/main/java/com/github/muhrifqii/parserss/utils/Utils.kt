package com.github.muhrifqii.parserss.utils

import com.github.muhrifqii.parserss.internal.element.ParseRSSAttribute
import com.github.muhrifqii.parserss.internal.element.ParseRSSElement
import com.github.muhrifqii.parserss.internal.element.PrefixNamedHolder
import com.github.muhrifqii.parserss.internal.xml.PullParser

/**
 * Utility function to return a lowercase rss [com.github.muhrifqii.parserss.internal.element.PrefixNamedHolder]
 */
internal fun String.toPrefixNamedElement(): PrefixNamedHolder =
    split(delimiters = arrayOf(":"), false, 2).let {
        return if (it.size > 1) {
            ParseRSSElement(it[0], it[1])
        } else {
            ParseRSSElement("", it[0])
        }
    }

/**
 * Utility function to combine and return rss element name and prefix
 */
fun PullParser.getRSSElement(nsAware: Boolean = false): ParseRSSElement {
    return if (nsAware) {
        ParseRSSElement(lowercasePrefix(), lowercaseName())
    } else {
        lowercaseName().toPrefixNamedElement() as ParseRSSElement
    }
}

/**
 * Utility function to return a lowercase rss name, or empty string if name returns null
 */
fun PullParser.lowercaseName() = (name ?: "").lowercase()

/**
 * Utility function to return a lowercase rss prefix ns, or empty string if prefix returns null
 */
fun PullParser.lowercasePrefix() = (prefix ?: "").lowercase()

/**
 * Utility function to combine and return rss attribute name and prefix
 */
fun PullParser.getRSSAttributeElement(index: Int, nsAware: Boolean = false): ParseRSSAttribute {
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

/**
 * Utility function to return a next text with trimmed text
 */
fun PullParser.nextTextTrimmed() = nextText().trim()

/**
 * Utility function to get last value of a [LinkedHashMap]
 */
fun <K, T> LinkedHashMap<K, T>.lastValue() = values.last()

fun String?.toIntOrZero() = this?.toInt() ?: 0

/**
 * Combine current namespace string with supplied local name
 */
fun String.buildRSSName(localName: String) = "$this:$localName"

/**
 * Combine prefix namespace (if any) with the local name
 */
fun ParseRSSElement.buildRSSName() = if (hasNamespace()) "$prefix:$name" else name
