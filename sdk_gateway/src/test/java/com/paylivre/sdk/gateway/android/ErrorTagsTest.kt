package com.paylivre.sdk.gateway.android


import com.paylivre.sdk.gateway.android.domain.model.KeysResponseError
import com.paylivre.sdk.gateway.android.domain.model.getErrorTagResponseError
import com.paylivre.sdk.gateway.android.domain.model.getStringKeyResponseError
import org.junit.Assert
import org.junit.Test

class ErrorTagsTest {

    @Test
    fun `Test getErrorTagResponseError`() {
        Assert.assertEquals("UX005", getErrorTagResponseError("Request failed with status code 500"))
        Assert.assertEquals("UC001", getErrorTagResponseError("Merchant not found."))
        Assert.assertEquals("UC002", getErrorTagResponseError("Invalid signature."))
        Assert.assertEquals("UB104", getErrorTagResponseError("401"))
        Assert.assertEquals("UA113", getErrorTagResponseError("Something went wrong while creating your order receivable."))
        Assert.assertEquals("UC003", getErrorTagResponseError("This action is unauthorized."))
        Assert.assertEquals("UD001", getErrorTagResponseError("Deposit not found."))
        Assert.assertEquals("UA515", getErrorTagResponseError("Partner Wallet not found or insufficient funds."))
        Assert.assertEquals("UC004", getErrorTagResponseError("Could not verify the merchant signature."))
        Assert.assertEquals("UX000", getErrorTagResponseError("unknown string"))
    }


    @Test
    fun `Test getStringKeyResponseError`() {
        val mockGenericError = "invalid_data_error"
        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "document_not_match"
        ), getStringKeyResponseError("The given document number does not match the document number associated with user with email"))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "validation_service_error"
        ), getStringKeyResponseError("Our document validation service could not approve your document validation."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_3_orders_with_equal_amount"
        ), getStringKeyResponseError("The user has reached the limit of 3 orders with equal amount in the last 24 hours."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_insufficient_wallet_funds"
        ), getStringKeyResponseError("Insufficient Wallet funds."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "document_match_another_user"
        ), getStringKeyResponseError("The given document number is already associated to another user at Paylivre."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "document_not_match"
        ), getStringKeyResponseError("The given document number does not match the user's document."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "document_not_match"
        ), getStringKeyResponseError("The given document number does not match"))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_withdrawal_amount_between"
        ), getStringKeyResponseError("The withdrawal amount must be a value between 500 and 10000000"))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_deposit_min_amount"
        ), getStringKeyResponseError("Invalid deposit amount. The minimum amount allowed is 500"))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "validation_service_error"
        ), getStringKeyResponseError("Our document validation service could not approve your document validation."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_insufficient_wallet_funds"
        ), getStringKeyResponseError("Could not complete your transfer due to insufficient funds."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = "exceeded_withdrawal_limit_title",
            keyMessageDetails = "exceeded_withdrawal_limit_value"
        ), getStringKeyResponseError("The requested withdrawal exceeds the maximum amount allowed."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_pix_key_cpf_divergent"
        ), getStringKeyResponseError("The informed PIX key CPF must be the same as the CPF from user acccount."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = "error_pix_key_cnpj_divergent"
        ), getStringKeyResponseError("The informed PIX key CNPJ must be the same as the CNPJ from user acccount."))

        Assert.assertEquals(KeysResponseError(
            keyMessage = mockGenericError,
            keyMessageDetails = ""
        ), getStringKeyResponseError("unknown string"))

    }
}