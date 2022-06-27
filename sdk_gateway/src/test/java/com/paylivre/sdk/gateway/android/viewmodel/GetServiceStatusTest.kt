package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetServiceStatusTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    @Test
    fun `test mainViewModel getPixApprovalTime success`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        val responseExpectedString = fileTestsUtils
            .loadJsonAsString("response_get_services_status_success.json")

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseExpectedString)
        )
        val mockMainViewModel = MockMainViewModel(server)


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.getServicesStatus()

        //THEN
        val request1 = server.takeRequest()


        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway/deposit-status", request1.path)


        //check the value returned to livedata
        Assert.assertEquals(
            MainViewModel.ServicesStatusSuccess(
                isSuccess = true,
                typeStatusServices = 14
            ),
            mockMainViewModel.mainViewModel.getServicesStatusSuccess.getOrAwaitValueTest()
        )

        server.shutdown()
    }


    @Test
    fun `test mainViewModel getServicesStatus error api generic`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse()
                .setResponseCode(400)
        )
        val mockMainViewModel = MockMainViewModel(server)


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.getServicesStatus()

        //THEN
        val request1 = server.takeRequest()


        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway/deposit-status", request1.path)


        //check the value returned to livedata
        Assert.assertEquals(
            MainViewModel.ServicesStatusSuccess(
                isSuccess = false,
                typeStatusServices = 0
            ),
            mockMainViewModel.mainViewModel.getServicesStatusSuccess.getOrAwaitValueTest()
        )

        server.shutdown()
    }
}