package com.github.muhrifqii.parserss.retrofit

import com.github.muhrifqii.parserss.retrofit.samples.Feed
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

object TestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_2)
            .body(Feed.rssV2EnUS.toResponseBody())
            .build()
    }
}