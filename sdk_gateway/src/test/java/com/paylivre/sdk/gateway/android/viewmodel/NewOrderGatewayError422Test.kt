package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.ApiServiceMock
import com.paylivre.sdk.gateway.android.data.MockCallResponseBody
import com.paylivre.sdk.gateway.android.data.PaymentRepository
import com.paylivre.sdk.gateway.android.data.api.RemoteDataSource
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.domain.model.ErrorTags.*
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
class NewOrderGatewayError422Test {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    data class GetResponseString(
        val messageResponse: String,
        val errorLabel: String,
    )

    private fun getResponseString(label: String, errorType: String = "invalid"): GetResponseString {
        return GetResponseString(
            messageResponse = "{\"message\": \"The given data was invalid.\",\"errors\": {\"${label}\": [\"The $label field is $errorType.\"]}}",
            errorLabel = "The $label field is $errorType."
        )
    }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid email`() = runBlocking {
        NewOrderGatewayFailureUtil(
            getResponseString("email").messageResponse, 422,
            ErrorTransaction(
                message = "invalid_data_error",
                errors = Errors(email = listOf(getResponseString("email").errorLabel)),
                errorTags = RT001.toString(),
                original_message = "The given data was invalid.")
        ).testMainViewModelNewOrderGatewayFailure()
    }


    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid amount`() = runBlocking {
        //GIVEN
        var logEventsService = LogEventsServiceImpl.Companion
        val mockCallResponseBody =
            MockCallResponseBody(code = 422,
                rawResponseBodyError = getResponseString("amount").messageResponse)
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
        val errorExpected = ErrorTransaction(
            message = "invalid_data_error",
            errors = Errors(amount = listOf(getResponseString("amount").errorLabel)),
            errorTags = RT002.toString(),
            original_message = "The given data was invalid.")

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = false,
            data = null,
            error = errorExpected,
        )

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )
    }


    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid currency`() = runBlocking {
        NewOrderGatewayFailureUtil(
            getResponseString("currency").messageResponse, 422,
            ErrorTransaction(
                message = "invalid_data_error",
                errors = Errors(currency = listOf(getResponseString("currency").errorLabel)),
                errorTags = RT003.toString(),
                original_message = "The given data was invalid.")
        ).testMainViewModelNewOrderGatewayFailure()
    }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid document_number`() =
        runBlocking {
            NewOrderGatewayFailureUtil(
                getResponseString("document_number").messageResponse, 422,
                ErrorTransaction(
                    message = "invalid_data_error",
                    errors = Errors(document_number = listOf(getResponseString("document_number").errorLabel)),
                    errorTags = RT004.toString(),
                    original_message = "The given data was invalid.")
            ).testMainViewModelNewOrderGatewayFailure()
        }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid callback_url`() =
        runBlocking {
            NewOrderGatewayFailureUtil(
                getResponseString("callback_url").messageResponse, 422,
                ErrorTransaction(
                    message = "invalid_data_error",
                    errors = Errors(callback_url = listOf(getResponseString("callback_url").errorLabel)),
                    errorTags = RT005.toString(),
                    original_message = "The given data was invalid.")
            ).testMainViewModelNewOrderGatewayFailure()
        }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid operation`() = runBlocking {
        NewOrderGatewayFailureUtil(
            getResponseString("operation").messageResponse, 422,
            ErrorTransaction(
                message = "invalid_data_error",
                errors = Errors(operation = listOf(getResponseString("operation").errorLabel)),
                errorTags = RT006.toString(),
                original_message = "The given data was invalid.")
        ).testMainViewModelNewOrderGatewayFailure()
    }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid merchant_transaction_id`() =
        runBlocking {
            NewOrderGatewayFailureUtil(
                getResponseString("merchant_transaction_id").messageResponse, 422,
                ErrorTransaction(
                    message = "invalid_data_error",
                    errors = Errors(merchant_transaction_id = listOf(getResponseString("merchant_transaction_id").errorLabel)),
                    errorTags = RT007.toString(),
                    original_message = "The given data was invalid.")
            ).testMainViewModelNewOrderGatewayFailure()
        }

    @Test
    fun `test mainViewModel newOrderGateway failure code 422, invalid auto_approve`() =
        runBlocking {
            NewOrderGatewayFailureUtil(
                getResponseString("auto_approve").messageResponse, 422,
                ErrorTransaction(
                    message = "invalid_data_error",
                    errors = Errors(auto_approve = listOf(getResponseString("auto_approve").errorLabel)),
                    errorTags = RT008.toString(),
                    original_message = "The given data was invalid.")
            ).testMainViewModelNewOrderGatewayFailure()
        }
}