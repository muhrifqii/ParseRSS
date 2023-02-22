package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.utils.lastValue

/**
 * Base Parsing Mode Operation
 */
sealed interface ParsingModeOperation {
    /**
     * Reset whatever the object it has
     */
    fun resetObject()
}

/**
 * Get RSS version of [ParsingModeOperation]
 */
internal fun ParsingModeOperation.version(): RSSVersion {
    return when (this) {
        is RootDocument.RSS -> RSSVersion.RSS_V2
        is RootDocument.RDF -> RSSVersion.RSS_V1
        is RootDocument.Atom -> RSSVersion.RSS_ATOM
        else -> RSSVersion.TBD
    }
}

/**
 * ParseRSS Stateful Parser Mode
 */
sealed class ParsingMode(val nameToken: String) : ParsingModeOperation {

    internal var prev: ParsingMode? = null

    override fun resetObject() {
        // ignore
    }

    /**
     * Add other mode to the queue
     * @return [other]
     */
    operator fun plus(other: ParsingMode): ParsingMode {
        other.prev = this
        return other
    }

    /**
     * Place to storing the rss feed
     */
    class Channel(val rssObject: RSSFeed, val isToC: Boolean) : ParsingMode(ParseRSSKeyword.CHANNEL)

    /**
     * Item mode to parse an item element
     */
    class Item(private val cleanupFn: ((RSSItem) -> Unit)? = null) : ParsingMode(ParseRSSKeyword.ITEM) {
        var rssObject: RSSItem = RSSItemObject()
            private set

        override fun resetObject() {
            cleanupFn?.invoke(rssObject)
            rssObject = RSSItemObject()
        }
    }

    /**
     * Image mode to parse an image object
     */
    class Image(private val cleanupFn: ((RSSImage) -> Unit)? = null) : ParsingMode(ParseRSSKeyword.IMAGE) {
        var rssObject: RSSImage = RSSImageObject()
            private set

        override fun resetObject() {
            cleanupFn?.invoke(rssObject)
            rssObject = RSSImageObject()
        }
    }

    /**
     * MediaNS mode should and must go under [Item] mode
     */
    sealed interface MediaNS {

        class Group : ParsingMode(ParseRSSKeyword.GROUP), MediaNS {
            var rssObject: MediaEnabledObject = RSSItemObject()
                internal set
        }
    }

    /**
     * Parsing Author Element. Author on atom could be on either feed or entry.
     */
    class Author : ParsingMode(ParseRSSKeyword.AUTHOR) {
        var rssObject: AuthorEnabledObject = RSSItemObject()
            internal set
    }

    /**
     * ParsingMode Read & Set
     */
    class Read : ParsingMode("") {

        private val modes: LinkedHashMap<String, ParsingModeOperation> = LinkedHashMap()

        /**
         * Add/replace mode to the stack of the reader mode
         */
        @Suppress("ThrowsCount")
        operator fun plusAssign(other: ParsingMode) {
            if (other.prev != null) {
                plusAssign(other.prev!!)
            }
            when (other) {
                is Read -> return
                is MediaNS.Group -> {
                    val item = modes.lastValue()
                    if (item !is Item) {
                        throw ParseRSSException(
                            "Error ${other.nameToken} should be under the item element",
                        )
                    }
                    other.rssObject = item.rssObject
                }
                is Author -> {
                    val authorHolder = modes.lastValue()
                    val channel = (authorHolder as? Channel)?.rssObject
                    val item = (authorHolder as? Item)?.rssObject
                    other.rssObject = item ?: channel ?: throw ParseRSSException(
                        "Error ${other.nameToken} should be under item/atom:entry or atom:feed",
                    )
                }
                is Channel -> {
                    val rootVersion = modes[RootDocument.token] ?: throw ParseRSSException("RSS not supported")
                    other.rssObject.version = rootVersion.version()
                    modes[other.nameToken] = other
                }
                else -> {
                    modes[other.nameToken] = other
                }
            }
        }

        /**
         * Remove mode from the stack of the reader mode
         */
        operator fun minusAssign(other: ParsingMode) {
            when (other) {
                is Read -> return
                else -> {
                    modes[other.nameToken]?.resetObject()
                    modes.remove(other.nameToken)
                }
            }
        }

        /**
         * Set the last mode's rss mutable object on the stack using a setter function and a class to cast to for a concrete setter
         */
        operator fun <T : RSSObject> set(clazz: Class<T>, valueSetter: (T?) -> Unit) {
            val mode = modes.lastValue() as ParsingMode
            try {
                when (mode) {
                    is Read -> return
                    is MediaNS.Group -> valueSetter(clazz.cast(mode.rssObject))
                    is Image -> valueSetter(clazz.cast(mode.rssObject))
                    is Item -> valueSetter(clazz.cast(mode.rssObject))
                    is Channel -> valueSetter(clazz.cast(mode.rssObject))
                    is Author -> valueSetter(clazz.cast(mode.rssObject))
                    else -> return
                }
            } catch (err: ClassCastException) {
                if (mode.nameToken == ParseRSSKeyword.IMAGE) {
                    if (BuildConfig.DEBUG) {
                        println("[ParseRSS] Ignored error on casting ${mode.nameToken} element to ${clazz.name} with err ${err.localizedMessage}")
                    }
                } else {
                    throw ParseRSSException(
                        "Error on casting ${mode.nameToken} element to ${clazz.name}",
                        err,
                    )
                }
            }
        }

        /**
         * Get the last rss mode's object
         */
        operator fun <T : RSSObject> ParsingMode.get(clazz: Class<T>): T? {
            try {
                return when (val mode = modes.lastValue()) {
                    is MediaNS.Group -> clazz.cast(mode.rssObject)
                    is Image -> clazz.cast(mode.rssObject)
                    is Item -> clazz.cast(mode.rssObject)
                    is Channel -> clazz.cast(mode.rssObject)
                    else -> null
                }
            } catch (err: ClassCastException) {
                throw ParseRSSException(
                    "Error on casting rss object to ${clazz.name}",
                    err,
                )
            }
        }

        /**
         * Check if current mode contains [other] mode
         */
        fun contains(other: ParsingMode): Boolean = if (other is RootDocument) {
            modes.containsValue(other)
        } else {
            modes.contains(other.nameToken)
        }
    }
}

/**
 * Add more mode to the current mode. Only useful for [ParsingMode.Read] mode
 */
operator fun ParsingMode.plusAssign(other: ParsingMode) {
    when (this) {
        is ParsingMode.Read -> {
            this += other
        }
        else -> return
    }
}

/**
 * Remove any mode to the current mode. Only useful for [ParsingMode.Read] mode
 */
operator fun ParsingMode.minusAssign(other: ParsingMode) {
    when (this) {
        is ParsingMode.Read -> {
            this -= other
        }
        else -> return
    }
}

/**
 * Set a mutable setter to a mode. Only useful for [ParsingMode.Read] mode
 */
operator fun <T : RSSObject> ParsingMode.set(clazz: Class<T>, valueSetter: (T?) -> Unit) {
    when (this) {
        is ParsingMode.Read -> {
            this[clazz] = valueSetter
        }
        else -> return
    }
}

/**
 * Get the last rss mode's object
 */
operator fun <T : RSSObject> ParsingMode.get(clazz: Class<T>): T? = when (this) {
    is ParsingMode.Read -> {
        this[clazz]
    }
    else -> null
}

/**
 * Check if current mode contains [other] mode
 */
fun ParsingMode.contains(other: ParsingMode): Boolean {
    return when (this) {
        is ParsingMode.Read -> {
            contains(other)
        }
        else -> false
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

    /**
     * Mode to parse root information for atom type
     */
    object Atom : ParsingMode(token), RootDocument
}
