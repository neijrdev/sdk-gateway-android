package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.domain.model.ErrorTags.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
        NewOrderGatewayFailureUtil(
            getResponseString("amount").messageResponse, 422,
            ErrorTransaction(
                message = "invalid_data_error",
                errors = Errors(amount = listOf(getResponseString("amount").errorLabel)),
                errorTags = RT002.toString(),
                original_message = "The given data was invalid.")
        ).testMainViewModelNewOrderGatewayFailure()
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
    fun `test mainViewModel newOrderGateway failure code 422, invalid document_number`() = runBlocking {
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
    fun `test mainViewModel newOrderGateway failure code 422, invalid callback_url`() = runBlocking {
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
    fun `test mainViewModel newOrderGateway failure code 422, invalid merchant_transaction_id`() = runBlocking {
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
    fun `test mainViewModel newOrderGateway failure code 422, invalid auto_approve`() = runBlocking {
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