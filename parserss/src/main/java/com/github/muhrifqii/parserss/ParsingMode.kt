package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.utils.lastValue

/**
 * Base Parsing Mode Operation
 */
sealed interface ParsingModeOperation {
    fun resetObject()
}

/**
 * Get RSS version of [ParsingModeOperation]
 */
internal fun ParsingModeOperation.version(): RSSVersion {
    return when (this) {
        is RootDocument.RSS -> RSSVersion.RSS_V2
        is RootDocument.RDF -> RSSVersion.RSS_V1
        else -> RSSVersion.TBD
    }
}

/**
 * ParseRSS Stateful Parser Mode
 */
sealed class ParsingMode(val nameToken: String) : ParsingModeOperation {

    override fun resetObject() {
        // ignore
    }

    /**
     * Place to storing the rss feed
     */
    data class Channel(val rssObject: RSSFeed, val isToC: Boolean) : ParsingMode(ParseRSSKeyword.CHANNEL)

    /**
     * Item mode to parse an item element
     */
    object Item : ParsingMode(ParseRSSKeyword.ITEM) {
        var rssObject: RSSItem = RSSItemObject()
            private set

        override fun resetObject() {
            rssObject = RSSItemObject()
        }
    }

    /**
     * Image mode to parse an image object
     */
    object Image : ParsingMode(ParseRSSKeyword.IMAGE) {
        var rssObject: RSSImage = RSSImageObject()
            private set

        override fun resetObject() {
            rssObject = RSSImageObject()
        }
    }

    /**
     * MediaNS mode should and must go under [Item] mode
     */
    sealed interface MediaNS {

        object Group : ParsingMode(ParseRSSKeyword.GROUP), MediaNS {
            var rssObject: MediaEnabledObject = Item.rssObject
                private set

            override fun resetObject() {
                rssObject = Item.rssObject
            }
        }
    }

    /**
     * ParsingMode Read & Set
     */
    class Read : ParsingMode("") {

        private val modes: LinkedHashMap<String, ParsingModeOperation> = LinkedHashMap()

        operator fun plusAssign(other: ParsingMode) {
            when (other) {
                is Read -> return
                is Channel -> {
                    val rootVersion = modes[RootDocument.token] ?: throw ParseRSSException("RSS not supported")
                    other.rssObject.version = rootVersion.version()
                    modes[other.nameToken] = other
                }
                else -> {
                    other.resetObject()
                    modes[other.nameToken] = other
                }
            }
        }

        operator fun minusAssign(other: ParsingMode) {
            when (other) {
                is Read -> return
                else -> {
                    other.resetObject()
                    modes.remove(other.nameToken)
                }
            }
        }

        operator fun <T : RSSObject> set(clazz: Class<T>, valueSetter: (T?) -> Unit) {
            try {
                when (val mode = modes.lastValue()) {
                    is Read -> return
                    is MediaNS.Group -> valueSetter(clazz.cast(mode.rssObject))
                    is Image -> valueSetter(clazz.cast(mode.rssObject))
                    is Item -> valueSetter(clazz.cast(mode.rssObject))
                    is Channel -> valueSetter(clazz.cast(mode.rssObject))
                    RootDocument.RDF -> {}
                    RootDocument.RSS -> {}
                }
            } catch (err: ClassCastException) {
                throw ParseRSSException(
                    "Error on casting rss object to ${clazz.name}",
                    err
                )
            }
        }
    }
}

operator fun ParsingMode.plusAssign(other: ParsingMode) {
    when (this) {
        is ParsingMode.Read -> {
            this += other
        }
        else -> return
    }
}

operator fun ParsingMode.minusAssign(other: ParsingMode) {
    when (this) {
        is ParsingMode.Read -> {
            this -= other
        }
        else -> return
    }
}

operator fun <T : RSSObject> ParsingMode.set(field: Class<T>, valueSetter: (T?) -> Unit) {
    when (this) {
        is ParsingMode.Read -> {
            this[field] = valueSetter
        }
        else -> return
    }
}

sealed interface RootDocument : ParsingModeOperation {
    companion object {
        const val token = ">>>root"
    }

    /**
     * Mode to parse root information for RSS type (v2)
     */
    object RSS : ParsingMode(token), RootDocument

    /**
     * Mode to parse root information for RDF type (v1)
     */
    object RDF : ParsingMode(token), RootDocument
}
