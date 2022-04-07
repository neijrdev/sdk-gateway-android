package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.data.model.order.getErrorTags422
import org.junit.Assert
import org.junit.Test

class GetErrorTags422Test {

    @Test
    fun `Valid getErrorTags422 with invalid email`() {
        Assert.assertEquals(
            "RT001",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(email = listOf("The email field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid amount`() {
        Assert.assertEquals(
            "RT002",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(amount = listOf("The amount field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid currency`() {
        Assert.assertEquals(
            "RT003",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(currency = listOf("The currency field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid document_number`() {
        Assert.assertEquals(
            "RT004",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(document_number = listOf("The document number field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid callback_url`() {
        Assert.assertEquals(
            "RT005",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(callback_url = listOf("The callback url field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid operation`() {
        Assert.assertEquals(
            "RT006",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(operation = listOf("The operation field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid merchant_transaction_id`() {

        Assert.assertEquals(
            "RT007",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(
                        merchant_transaction_id =
                        listOf("The merchant transaction id has already been taken.")
                    )
                )
            )
        )


        Assert.assertEquals(
            "RT007",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(
                        merchant_transaction_id =
                        listOf("The merchant transaction id field is required.")
                    )
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid auto_approve`() {
        Assert.assertEquals(
            "RT008",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(auto_approve = listOf("The auto approve field is required."))
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid many data 1`() {
        Assert.assertEquals(
            "RT001, RT002, RT008",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(
                        email = listOf("The email field is required."),
                        amount = listOf("The amount field is required."),
                        auto_approve = listOf("The auto approve field is required.")
                    )
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid many data 2`() {
        Assert.assertEquals(
            "RT001, RT002, RT003, RT008",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(
                        email = listOf("The email field is required."),
                        amount = listOf("The amount field is required."),
                        currency = listOf("The currency field is required."),
                        auto_approve = listOf("The auto approve field is required.")
                    )
                )
            )
        )
    }

    @Test
    fun `Valid getErrorTags422 with invalid all data`() {
        Assert.assertEquals(
            "RT001, RT002, RT003, RT004, RT005, RT006, RT007, RT008",
            getErrorTags422(
                ErrorTransaction(
                    "The given data was invalid.", null,
                    errors = Errors(
                        email = listOf("The email field is required."),
                        amount = listOf("The amount field is required."),
                        currency = listOf("The currency field is required."),
                        document_number = listOf("The document number field is required."),
                        callback_url = listOf("The callback_url field is required."),
                        operation = listOf("The operation field is required."),
                        merchant_transaction_id = listOf(
                            "The merchant transaction id field is required."
                        ),
                        auto_approve = listOf("The auto approve field is required.")
                    )
                )
            )
        )
    }

}