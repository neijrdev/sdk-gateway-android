package com.paylivre.sdk.gateway.android.domain.model

import org.junit.Assert
import org.junit.Test

class ErrorTagsMessageTest {

    @Test
    fun `test getStringKeyResponseError, given message error contain "User not found with document number"`() {
        val stringKeyDataExpected = getStringKeyResponseError("User not found with document number")
        Assert.assertEquals("invalid_data_error", stringKeyDataExpected.keyMessage)
        Assert.assertEquals("user_not_found_with_document_number", stringKeyDataExpected.keyMessageDetails)
    }
}