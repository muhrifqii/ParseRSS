package com.github.muhrifqii.parserss.xml

/**
 * Enumeration of all even types reported by PullParser (android pull parser, ktxml & kxml2).
 * Some event types are only reported when calling nextToken() (opposed to next()).
 * This matches the order of all event type, starting with START_DOCUMENT = 0.
 */
enum class PullParserEventType {

    /**
     * The parser is at the very beginning of the document and nothing was read yet.
     *
     * This event type can only be observed by reading eventType before the first call to next(),
     * nextToken(), or nextTag()).
     */
    START_DOCUMENT,

    /**
     * Logical end of the xml document.
     *
     * Returned from eventType, next() and nextToken() when the end of the input document has
     * been reached.
     *
     * **NOTE:** calling next() or nextToken() again will result in exception being thrown.
     */
    END_DOCUMENT,

    /**
     * Returned from eventType, next() and nextToken() when a start tag was read.
     *
     * The name of start tag is available via the name property, its namespace and prefix are
     * available via the corresponding properties if namespaces are enabled.
     *
     * - See getAttribute* methods to retrieve element attributes.
     * - See getNamespace* methods to retrieve newly declared namespaces.
     */
    START_TAG,

    /**
     * Returned from eventType, next(), or nextToken() when an end tag was read.
     * The name of the start tag, its namespace and prefix are available via the properties
     * with these names.
     */
    END_TAG,

    /**
     * Character data was read and will is available via the text property.
     *
     * **Please note:** next() will accumulate multiple
     * events into one TEXT event, skipping IGNORABLE_WHITESPACE,
     * PROCESSING_INSTRUCTION and COMMENT events.
     *
     * In contrast, nextToken() will stop reading
     * text when any other event is observed.
     *
     * Also, when the state was reached by calling next(), the text value will
     * be normalized, whereas it will return unnormalized content in the case of nextToken().
     * This allows an exact roundtrip without changing line ends when examining low
     * level events, whereas for high level applications the text is
     * normalized appropriately.
     */
    TEXT,

    // ----------------------------------------------------------------------------
    // additional events exposed by lower level nextToken()
    /**
     * A CDATA sections was just read;
     * this token is available only from calls to nextToken().
     * A call to next() will accumulate various text events into a single event
     * of type TEXT. The text contained in the CDATA section is available via the text property.
     */
    CDSECT,

    /**
     * An entity reference was just read.
     *
     * This token is available from nextToken() only. The entity name is available via the name
     * property. If available, the replacement text can be obtained by via the text property;
     * otherwise, the user is responsible for resolving the entity reference.
     *
     * This event type is never returned from next(); next() will accumulate the replacement
     * text and other text events to a single TEXT event.
     */
    ENTITY_REF,

    /**
     * Ignorable whitespace was just read.
     *
     * This token is available only from nextToken(). For non-validating parsers, this event is
     * only reported by nextToken() when outside the root element.
     *
     * Validating parsers may be able to detect ignorable whitespace at other locations.
     * The ignorable whitespace string is available via the text property.
     *
     * **NOTE:** this is different from accessing isWhitespace, since text content
     * may be whitespace but not ignorable.
     *
     * Ignorable whitespace is skipped by next() automatically; this event
     * type is never returned from next().
     */
    IGNORABLE_WHITESPACE,

    /**
     * An XML processing instruction declaration was just read.
     *
     * This event type is available only via nextToken().
     * The text property will return text that is inside the processing instruction.
     * Calls to next() will skip processing instructions automatically.
     */
    PROCESSING_INSTRUCTION,

    /**
     * An XML comment was just read.
     *
     * This event type is this token is available via nextToken() only;
     * calls to next() will skip comments automatically.
     * The content of the comment can be accessed using the getText()
     * method.
     */
    COMMENT,

    /**
     * An XML document type declaration was just read.
     *
     * This token is available from nextToken() only. The unparsed text inside the doctype is
     * available via the `text` property.
     */
    DOCDECL,

    /** XML Declaration */
    XML_DECL
}
