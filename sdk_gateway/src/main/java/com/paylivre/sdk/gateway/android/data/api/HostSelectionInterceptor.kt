package com.paylivre.sdk.gateway.android.data.api

import com.paylivre.sdk.gateway.android.App
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.utils.API_HOST_ENVIRONMENT_PRODUCTION
import okhttp3.Interceptor

class HostSelectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()

        val host: String = if (App.getHostAPI()
                .isNullOrEmpty()
        ) API_HOST_ENVIRONMENT_PRODUCTION else App.getHostAPI().toString()

        val newUrl = request.url.newBuilder()
            .host(host)
            .build()

        request = request
            .newBuilder()
            .url(newUrl)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "SdkGatewayPaylivre/Android_v${BuildConfig.VERSION_NAME}")
            .build()

        return chain.proceed(request)
    }

}