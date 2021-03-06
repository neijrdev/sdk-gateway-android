package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
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
class StartPaymentByUrlTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    @Test
    fun `test mainViewModel startPaymentByUrl success`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        val responseExpectedString = fileTestsUtils
            .loadJsonAsString("response_deposit_pix_success.json")

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseExpectedString)
        )
        val mockMainViewModel = MockMainViewModel(server)

        val requestExpected =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")
        val expectedRequestBody = Gson().fromJson(requestExpected, OrderDataRequest::class.java)

        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.startPaymentByURL(expectedRequestBody)

        //THEN
        val request1 = server.takeRequest()
        val requestBody = Gson().fromJson(request1.body.readUtf8(), OrderDataRequest::class.java)
        val responseDataExpected =
            Gson().fromJson(responseExpectedString, ResponseCommonTransactionData::class.java)

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = responseDataExpected,
            error = null,
        )

        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway", request1.path)

        //check Request data is correct
        Assert.assertEquals(expectedRequestBody, requestBody)


        //Add timeout to wait mocked request data
        TimeUnit.SECONDS.sleep(1);

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mockMainViewModel.mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )

        server.shutdown()
    }
}