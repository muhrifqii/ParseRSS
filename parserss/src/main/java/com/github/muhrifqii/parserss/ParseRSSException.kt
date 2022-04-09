package com.github.muhrifqii.parserss

class ParseRSSException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
