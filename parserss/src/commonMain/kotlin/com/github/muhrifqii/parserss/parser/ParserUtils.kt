package com.github.muhrifqii.parserss.parser

import com.github.muhrifqii.parserss.RSSFeed
import com.github.muhrifqii.parserss.element.ParseRSSElement
import com.github.muhrifqii.parserss.element.ParseRSSKeyword
import com.github.muhrifqii.parserss.element.RSSVersion

/**
 * Utility functions for RSS parsing
 */

private fun deduceDefaultNSParsingMode(root: RSSFeed, element: ParseRSSElement) =
    when (element.name) {
        RSSVersion.RSS_V2.elementName -> RootDocument.RSS
        ParseRSSKeyword.CHANNEL -> ParsingMode.Channel(root, false)
        ParseRSSKeyword.ITEM -> ParsingMode.Item {
            root.items.add(it)
        }

        ParseRSSKeyword.IMAGE -> ParsingMode.Image {
            root.image = it
        }

        RSSVersion.RSS_ATOM.elementName -> RootDocument.Atom + ParsingMode.Channel(root, false)
        ParseRSSKeyword.ENTRY -> ParsingMode.Item {
            root.items.add(it)
        }

        ParseRSSKeyword.AUTHOR -> ParsingMode.Author()
        else -> ParsingMode.Read()
    }

private fun deduceRdfNSParsingMode(root: RSSFeed, element: ParseRSSElement) =
    when (element.name) {
        RSSVersion.RSS_V1.elementName -> RootDocument.RDF
        ParseRSSKeyword.CHANNEL -> ParsingMode.Channel(root, true)
        else -> ParsingMode.Read()
    }

private fun deduceMediaNSParsingMode(element: ParseRSSElement) = when (element.name) {
    ParseRSSKeyword.GROUP -> ParsingMode.MediaNS.Group()
    else -> ParsingMode.Read()
}

/** Deduce mode based on given name */
internal fun deduceParsingMode(root: RSSFeed, element: ParseRSSElement) = when (element.prefix) {
    ParseRSSKeyword.DEFAULT_NS -> deduceDefaultNSParsingMode(root, element)
    ParseRSSKeyword.RDF_NS -> deduceRdfNSParsingMode(root, element)
    ParseRSSKeyword.MEDIA_NS -> deduceMediaNSParsingMode(element)
    else -> ParsingMode.Read()
}
