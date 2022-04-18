package com.github.muhrifqii.parserss

import org.xmlpull.v1.XmlPullParser

/**
 * Utility function to return a lowercase rss name, or empty string if name returns null
 */
fun XmlPullParser.lowerCaseName() = (name ?: "").lowercase()

/**
 * Utility function to return a lowercase rss prefix ns, or empty string if prefix returns null
 */
fun XmlPullParser.lowerCasePrefix() = (prefix ?: "").lowercase()

/**
 * Utility function to return a next text with trimmed text
 */
fun XmlPullParser.nextTextTrimmed() = nextText().trim()

/**
 * Utility function to get last value of a [LinkedHashMap]
 */
fun <K, T> LinkedHashMap<K, T>.lastValue() = values.last()

/**
 * Combine current namespace string with supplied local name
 */
fun String.buildRSSName(localName: String) = "$this:$localName"
