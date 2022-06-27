package com.paylivre.sdk.gateway.android.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paylivre.sdk.gateway.android.utils.API_HOST_ENVIRONMENT_DEV
import io.sentry.android.okhttp.SentryOkHttpInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun client() =
    OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HostSelectionInterceptor())
        .addInterceptor(SentryOkHttpInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

fun gson(): Gson = GsonBuilder().create()

const val stringPrefixUrl = "https://"


fun retrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(stringPrefixUrl + API_HOST_ENVIRONMENT_DEV)
    .client(client())
    .addConverterFactory(GsonConverterFactory.create(gson()))
    .build()

fun services(): ApiService =
    retrofit().create(ApiService::class.java)
