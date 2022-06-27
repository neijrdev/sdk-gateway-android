package com.paylivre.sdk.gateway.android.viewmodel

import com.paylivre.sdk.gateway.android.data.PaymentRepository
import com.paylivre.sdk.gateway.android.data.api.ApiService
import com.paylivre.sdk.gateway.android.data.api.RemoteDataSource
import com.paylivre.sdk.gateway.android.data.api.gson
import com.paylivre.sdk.gateway.android.data.api.services
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImpl
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImplTest
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MockMainViewModel(
    private var server: MockWebServer = MockWebServer(),
) {
    fun clientMock() =
        OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build()

    val retrofit = Retrofit.Builder().baseUrl(server.url("/"))
        .client(clientMock())
        .addConverterFactory(GsonConverterFactory.create(gson())).build()

    var LogEventsService = LogEventsServiceImpl.Companion
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    var remoteDataSource = RemoteDataSource(apiService, LogEventsService)
    var paymentRepository = PaymentRepository(remoteDataSource)
    var mainViewModel = MainViewModel(paymentRepository)

    val mockedAppModule: Module = module(override = true) {
        single<LogEventsService> {
            LogEventsService
        }
        single {
            apiService
        }
        single {
            RemoteDataSource(get(), get())
        }
        single {
            PaymentRepository(get())
        }
        viewModel {
            MainViewModel(paymentRepository)
        }
    }
}