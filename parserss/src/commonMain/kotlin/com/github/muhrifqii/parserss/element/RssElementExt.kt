package com.github.muhrifqii.parserss.element

/**
 * Utility function to get last value of a [LinkedHashMap]
 */
internal fun <K, T> LinkedHashMap<K, T>.lastValue() = values.last()

/**
 * Parse Int or Zero if String is null
 */
internal fun String?.toIntOrZero() = this?.toInt() ?: 0

/**
 * Combine current namespace string with supplied local name
 */
internal fun String.buildRSSName(localName: String) = "$this:$localName"

/**
 * Combine prefix namespace (if any) with the local name
 */
internal fun ParseRSSElement.buildRSSName() = if (hasNamespace()) "$prefix:$name" else name

/**
 * Utility function to return a lowercase rss [com.github.muhrifqii.parserss.element.PrefixNamedHolder]
 */
internal fun String.toPrefixNamedElement(): PrefixNamedHolder =
    split(delimiters = arrayOf(":"), false, 2).let {
        return if (it.size > 1) {
            ParseRSSElement(it[0], it[1])
        } else {
            ParseRSSElement("", it[0])
        }
    }
