package com.paylivre.sdk.gateway.android.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.paylivre.sdk.gateway.android.data.api.*
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImpl
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class ApiModuleTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `test ApiService`() = runBlocking {
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("")
        )

        val apiService: ApiService =
            services(
                baseUrl = server.url("/").toString(),
                isEnableHostSelectionInterceptor = false)
        var logEventsService = LogEventsServiceImpl
        var remoteDataSource = RemoteDataSource(apiService, logEventsService)
        var paymentRepository = PaymentRepository(remoteDataSource)
        var mainViewModel = MainViewModel(paymentRepository)

        mainViewModel.checkStatusTransaction(10)

        val request = server.takeRequest()
        server.shutdown()

        Assert.assertEquals(request.path, "/api/v2/transaction/status/10")
    }
}