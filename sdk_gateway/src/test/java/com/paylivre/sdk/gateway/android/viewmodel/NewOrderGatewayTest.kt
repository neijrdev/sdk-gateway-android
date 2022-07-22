package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.ApiServiceMock
import com.paylivre.sdk.gateway.android.data.MockCallResponseBody
import com.paylivre.sdk.gateway.android.data.PaymentRepository
import com.paylivre.sdk.gateway.android.data.api.RemoteDataSource
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImpl
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class NewOrderGatewayTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    @Test
    fun `mainViewModel newOrderGateway success`() = runBlocking {
        //GIVEN
        val responseExpectedString = fileTestsUtils
            .loadJsonAsString("res_check_status_deposit_success.json")

        var logEventsService = LogEventsServiceImpl.Companion
        val mockCallResponseBody =
            MockCallResponseBody(code = 200, rawResponseBodySuccess = responseExpectedString)
        val apiService = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)

        var remoteDataSource = RemoteDataSource(apiService, logEventsService)
        var paymentRepository = PaymentRepository(remoteDataSource)
        var mainViewModel = MainViewModel(paymentRepository)


        val requestExpected =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")
        val expectedRequestBody = Gson().fromJson(requestExpected, OrderDataRequest::class.java)


        //WHEN
        //Check Deposit Status
        mainViewModel.newOrderGateway(expectedRequestBody)

        //THEN
        val responseDataExpected =
            Gson().fromJson(responseExpectedString, ResponseCommonTransactionData::class.java)

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = responseDataExpected,
            error = null,
        )

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )
    }
}