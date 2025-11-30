package ru.practicum.android.diploma.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ru.practicum.android.diploma.util.GetSystemUserAgent

class UserAgentInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val userAgent = GetSystemUserAgent.systemUserAgent(context)
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()

        return chain.proceed(requestWithUserAgent)
    }
}
