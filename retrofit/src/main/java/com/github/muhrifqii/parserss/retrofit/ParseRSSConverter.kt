package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.RSSFeedObject
import com.github.muhrifqii.parserss.parseRSS
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class ParseRSSConverter(
    private val namespaceAware: Boolean
) : Converter<ResponseBody, RSSFeedObject> {
    override fun convert(value: ResponseBody): RSSFeedObject {
        return parseRSS(value.charStream(), namespaceAware).getOrThrow()
    }
}

/** Creates [Converter] instances based on a type and target usage. */
class ParseRSSConverterFactory(
    private val namespaceAware: Boolean
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return ParseRSSConverter(namespaceAware)
    }

    companion object {
        /**
         * create **ParseRSS** converter factory
         */
        fun create(namespaceAware: Boolean = false) =
            ParseRSSConverterFactory(namespaceAware)
    }
}
