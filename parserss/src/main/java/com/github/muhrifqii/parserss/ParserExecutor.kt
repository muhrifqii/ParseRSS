package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.utils.ParseRSSElement
import com.github.muhrifqii.parserss.utils.ParserExecutorUtils
import com.github.muhrifqii.parserss.utils.getRSSAttributeElement
import com.github.muhrifqii.parserss.utils.getRSSElement
import com.github.muhrifqii.parserss.utils.nextTextTrimmed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader

class ParserExecutor<T>(
    factory: XmlPullParserFactory,
    private val input: Reader,
    strictMode: Boolean,
    feedSupplier: () -> T,
) where T : RSSFeed {

    private val parser: XmlPullParser
    private val feed: T
    private val mode: ParsingMode = ParsingMode.Read()
    private val namespaces: LinkedHashMap<String, String> = LinkedHashMap()
    private val pullParserNSAware = strictMode

    init {
        factory.isNamespaceAware = pullParserNSAware
        parser = factory.newPullParser()
        feed = feedSupplier()
    }

    /**
     * Execute the parser
     */
    fun run(): T {
        parser.setInput(input)
        loopThrough()
        return feed
    }

    private fun loopThrough() {
        var token = parser.eventType
        while (token != XmlPullParser.END_DOCUMENT) {
            when (token) {
                XmlPullParser.END_TAG -> closingTag()
                XmlPullParser.START_TAG -> openingTag()
            }
            token = parser.next()
        }
    }

    private fun collectNS(element: ParseRSSElement): ParseRSSElement {
        if (element.name != RSSVersion.RSS_V1.elementName &&
            element.name != RSSVersion.RSS_V2.elementName &&
            element.name != RSSVersion.RSS_ATOM.elementName
        ) {
            return element
        }
        val attrCount = parser.attributeCount
        for (i in 0 until attrCount) {
            val attribute = parser.getRSSAttributeElement(i, pullParserNSAware)
            namespaces[attribute.name] = attribute.value
        }
        return element
    }

    private fun openingTag() {
        var element = parser.getRSSElement(pullParserNSAware)
        mode += ParserExecutorUtils.deduceParsingMode(feed, element)
        element = collectNS(element)
        if (mode.contains(RootDocument.Atom)) {
            parseAtom(element)
        } else {
            parseV1V2(element)
        }
    }

    private fun closingTag() {
        when (parser.getRSSElement(pullParserNSAware).name) {
            ParseRSSKeyword.GROUP -> {
                mode -= ParsingMode.MediaNS.Group()
            }
            ParseRSSKeyword.IMAGE -> {
                mode -= ParsingMode.Image()
            }
            ParseRSSKeyword.ITEM -> {
                mode -= ParsingMode.Item()
            }
            ParseRSSKeyword.ENTRY -> {
                mode -= ParsingMode.Item()
            }
            ParseRSSKeyword.AUTHOR -> {
                mode -= ParsingMode.Author()
            }
        }
    }

    private fun parseNSDefault(element: ParseRSSElement) {
        try {
            when (element.name) {
                ParseRSSKeyword.TITLE -> mode[TitleEnabledObject::class.java] = {
                    it?.title = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.DESCRIPTION -> mode[DescriptionEnabledObject::class.java] = {
                    it?.description = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.LINK -> mode[LinkEnabledObject::class.java] = {
                    it?.link = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.PUBLISH_DATE -> mode[PublishDateEnabledObject::class.java] = {
                    val date = parser.nextTextTrimmed()
                    if (date.isNotEmpty()) {
                        it?.publishDate = date
                    }
                }
                ParseRSSKeyword.LAST_BUILD_DATE -> mode[LastUpdatedEnabledObject::class.java] = {
                    it?.lastUpdated = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.URL -> mode[UrlEnabledObject::class.java] = {
                    it?.url = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.LANG -> mode[LangEnabledObject::class.java] = {
                    it?.language = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.GUID -> mode[GUIdEnabledObject::class.java] = {
                    val isPermanent = (
                        parser.getAttributeValue(
                            XmlPullParser.NO_NAMESPACE,
                            ParseRSSKeyword.ATTR_PERMALINK,
                        ) ?: "true"
                        ).toBoolean()
                    it?.guId = GUId(parser.nextTextTrimmed(), isPermanent)
                }
                ParseRSSKeyword.AUTHOR -> mode[AuthorEnabledObject::class.java] = {
                    val parsed = parser.nextTextTrimmed()
                    it?.author = RSSPersonAwareObject(parsed)
                }
                ParseRSSKeyword.CATEGORY -> mode[CategoryEnabledObject::class.java] = {
                    val domain = parser.getAttributeValue(
                        XmlPullParser.NO_NAMESPACE,
                        ParseRSSKeyword.ATTR_DOMAIN,
                    )
                    it?.categories?.add(RSSCategoryObject(domain, parser.nextTextTrimmed()))
                }
                ParseRSSKeyword.COPYRIGHT -> mode[CopyrightsEnabledObject::class.java] = {
                    it?.copyright = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.COMMENTS -> mode[CommentEnabledObject::class.java] = {
                    it?.comments = parser.nextTextTrimmed()
                }
                ParseRSSKeyword.ENCLOSURE -> mode[ImageUrlEnabledObject::class.java] = {
                    val imageUrl = parser.getAttributeValue(
                        XmlPullParser.NO_NAMESPACE,
                        ParseRSSKeyword.ENCLOSURE_URL,
                    )
                    it?.imageUrls?.add(imageUrl.trim())
                }
                ParseRSSKeyword.DC_NS_DATE -> mode[PublishDateEnabledObject::class.java] = {
                    val date = parser.nextTextTrimmed()
                    if (date.isNotEmpty()) {
                        it?.publishDate = date
                    }
                }
                ParseRSSKeyword.DC_NS_TYPE -> mode[CategoryEnabledObject::class.java] = {
                    val domain = parser.getAttributeValue(
                        XmlPullParser.NO_NAMESPACE,
                        ParseRSSKeyword.ATTR_DOMAIN,
                    )
                    it?.categories?.add(RSSCategoryObject(domain, parser.nextTextTrimmed()))
                }
            }
        } catch (ignored: XmlPullParserException) {
            // Note: added try/catch to handle RSS with broken and not valid structures, so we
            //      can continue the parsing instead of return a brutal exception
            if (BuildConfig.DEBUG) { println("[ParseRSS] Ignored XmlPullParserException on parseNSDefault: $ignored") }
        } catch (ignored: Exception) {
            // Note: added try/catch to handle RSS with broken and not valid structures, so we
            //      can continue the parsing instead of return a brutal exception
            if (BuildConfig.DEBUG) { println("[ParseRSS] Ignored Exception on parseNSDefault: $ignored") }
        }
    }

    private fun parseNSMedia(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.CONTENT -> mode[MediaEnabledObject::class.java] = {
                try {
                    val media = RSSMediaObject()
                    media.url = parser.getAttributeValue(
                        XmlPullParser.NO_NAMESPACE,
                        ParseRSSKeyword.ATTR_URL,
                    )
                    media.medium = MediaType.from(
                        parser.getAttributeValue(
                            XmlPullParser.NO_NAMESPACE,
                            ParseRSSKeyword.ATTR_MEDIUM,
                        ),
                    )
                    media.width =
                        parser.getAttributeValue(
                            XmlPullParser.NO_NAMESPACE,
                            ParseRSSKeyword.ATTR_WIDTH,
                        ).toInt()
                    media.height =
                        parser.getAttributeValue(
                            XmlPullParser.NO_NAMESPACE,
                            ParseRSSKeyword.ATTR_HEIGHT,
                        ).toInt()
                    it?.medias?.add(media)
                } catch (ignored: NumberFormatException) {
                    if (BuildConfig.DEBUG) {
                        println("[ParseRSS] Ignored NumberFormatException error: ${ignored.localizedMessage}")
                    }
                } catch (ignored: Exception) {
                    // Note: added try/catch to handle RSS with broken and not valid structures, so we
                    //      can continue the parsing instead of return a brutal exception
                    if (BuildConfig.DEBUG) { println("[ParseRSS] Ignored Exception on parseNSMedia: $ignored") }
                }
            }
            ParseRSSKeyword.DESCRIPTION -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    medias.lastOrNull()?.description = parser.nextTextTrimmed()
                }
            }
            ParseRSSKeyword.CREDIT -> mode[MediaEnabledObject::class.java] = {
                it?.apply {
                    medias.lastOrNull()?.credit = parser.nextTextTrimmed()
                }
            }
        }
    }

    private fun parseNSAtom(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.TITLE -> mode[TitleEnabledObject::class.java] = {
                it?.title = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.SUBTITLE -> mode[DescriptionEnabledObject::class.java] = {
                it?.description = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.PUBLISHED -> mode[PublishDateEnabledObject::class.java] = {
                it?.publishDate = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.ID -> mode[GUIdEnabledObject::class.java] = {
                it?.guId = GUId(parser.nextTextTrimmed(), false)
            }
            ParseRSSKeyword.UPDATED -> mode[LastUpdatedEnabledObject::class.java] = {
                it?.lastUpdated = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.LINK -> mode[LinkEnabledObject::class.java] = {
                val rel = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_REL) ?: ""
                val href = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, ParseRSSKeyword.ATTR_HREF)
                if (rel == "self" && it is RSSFeed) {
                    it.link = href
                } else if (rel == "alternate" && it is RSSItem) {
                    it.link = href
                }
            }
            ParseRSSKeyword.RIGHTS -> mode[CopyrightsEnabledObject::class.java] = {
                it?.copyright = parser.nextTextTrimmed()
            }
            ParseRSSKeyword.SUMMARY -> mode[SummaryEnabledObject::class.java] = {
                it?.summary = parser.nextTextTrimmed()
            }
        }
        parseAtomPersonAwareObject(element)
    }

    private fun parseAtomPersonAwareObject(element: ParseRSSElement) {
        when (element.name) {
            ParseRSSKeyword.AUTHOR -> mode[AuthorEnabledObject::class.java] = {
                it?.author = RSSPersonAwareObject("")
            }
            ParseRSSKeyword.NAME -> mode[AuthorEnabledObject::class.java] = {
                it?.apply {
                    author?.name = parser.nextTextTrimmed()
                }
            }
            ParseRSSKeyword.URI -> mode[AuthorEnabledObject::class.java] = {
                it?.apply {
                    author?.uri = parser.nextTextTrimmed()
                }
            }
            ParseRSSKeyword.EMAIL -> mode[AuthorEnabledObject::class.java] = {
                it?.apply {
                    author?.email = parser.nextTextTrimmed()
                }
            }
        }
    }

    private fun parseV1V2(element: ParseRSSElement) {
        when (element.prefix) {
            ParseRSSKeyword.ATOM_NS -> parseAtom(element)
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia(element)
            else -> parseNSDefault(element)
        }
    }

    private fun parseAtom(element: ParseRSSElement) {
        when (element.prefix) {
            ParseRSSKeyword.MEDIA_NS -> parseNSMedia(element)
            else -> parseNSAtom(element)
        }
    }
}
