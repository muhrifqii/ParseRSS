package com.github.muhrifqii.parserss.internal.element

interface PrefixNamedHolder {
    val prefix: String
    val name: String
    val value: String

    /**
     * Check whether it contains namespace prefix or not
     */
    fun hasNamespace(): Boolean = prefix.isNotEmpty()
}

/**
 * Hold the basic element properties: prefix and name. value always return empty
 */
data class ParseRSSElement(override val prefix: String, override val name: String) :
    PrefixNamedHolder {
    override val value: String
        get() = ""
}

/**
 * Hold the basic attribute properties: prefix, name, and value
 */
data class ParseRSSAttribute(
    override val prefix: String,
    override val name: String,
    override val value: String
) :
    PrefixNamedHolder
