package com.github.muhrifqii.parserss

/**
 * ParseRSS Stateful Parser Mode
 */
sealed class ParsingMode(val nameToken: String) {

    open fun resetObject() {
    }

    class Channel(val rssObject: RSSFeed) : ParsingMode(ParseRSSKeyword.CHANNEL)

    /**
     *
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
    sealed class MediaNS {

        object Group : ParsingMode(ParseRSSKeyword.MEDIA_GROUP) {
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

        private val modes: LinkedHashMap<String, ParsingMode> = LinkedHashMap()

        operator fun plusAssign(other: ParsingMode) {
            when (other) {
                is Read -> return
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
