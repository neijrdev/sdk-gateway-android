package com.paylivre.sdk.gateway.android.viewmodel

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert

class NewOrderGatewayFailureUtil(
    private val responseExpectedString: String,
    private val responseCode: Int,
    private val errorTransaction: ErrorTransaction?,
) {
    var fileTestsUtils = FileTestsUtils()

    fun testMainViewModelNewOrderGatewayFailure() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(responseExpectedString)
        )
        val mockMainViewModel = MockMainViewModel(server)

        val requestExpected =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")
        val expectedRequestBody = Gson().fromJson(requestExpected, OrderDataRequest::class.java)

        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.newOrderGateway(expectedRequestBody)

        //THEN
        val request1 = server.takeRequest()
        val requestBody = Gson().fromJson(request1.body.readUtf8(), OrderDataRequest::class.java)

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = false,
            data = null,
            error = errorTransaction,
        )

        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway", request1.path)

        //check Request data is correct
        Assert.assertEquals(expectedRequestBody, requestBody)

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mockMainViewModel.mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )

        server.shutdown()
    }
}