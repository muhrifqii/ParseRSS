package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.ParseRSS
import com.github.muhrifqii.parserss.RSSFeed
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class ParseRSSConverter<R : RSSFeed> : Converter<ResponseBody, R> {
    override fun convert(value: ResponseBody): R {
        return ParseRSS.parse(value.charStream())
    }
}

/** Creates [Converter] instances based on a type and target usage. */
class ParseRSSConverterFactory<R : RSSFeed> private constructor() : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {

        return ParseRSSConverter<R>()
    }

    companion object {
        /**
         * create [ParseRSS] converter factory
         */
        fun <R : RSSFeed> create() = ParseRSSConverterFactory<R>()
    }
}
