package com.paylivre.sdk.gateway.android.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paylivre.sdk.gateway.android.utils.API_HOST_ENVIRONMENT_PRODUCTION
import io.sentry.android.okhttp.SentryOkHttpInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun client(isEnableHostSelectionInterceptor: Boolean = true): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(SentryOkHttpInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

    if (isEnableHostSelectionInterceptor) okHttpClient.addInterceptor(HostSelectionInterceptor())

    return okHttpClient.build()
}


fun gson(): Gson = GsonBuilder().create()

const val stringPrefixUrl = "https://"


fun retrofit(baseUrl: String, isEnableHostSelectionInterceptor: Boolean): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client(isEnableHostSelectionInterceptor))
        .addConverterFactory(GsonConverterFactory.create(gson()))
        .build()

fun services(
    baseUrl: String = stringPrefixUrl + API_HOST_ENVIRONMENT_PRODUCTION,
    isEnableHostSelectionInterceptor: Boolean = true,
): ApiService =
    retrofit(baseUrl, isEnableHostSelectionInterceptor).create(ApiService::class.java)
