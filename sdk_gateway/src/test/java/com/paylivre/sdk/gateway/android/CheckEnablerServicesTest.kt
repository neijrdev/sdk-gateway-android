package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.CheckEnablerServices
import org.junit.Assert
import org.junit.Test

class CheckEnablerServicesTest {
    @Test
    fun `Test CheckEnablerServices`() {
        val checkEnablerServices = CheckEnablerServices(
            messageError = "messageError",
            messageErrorDetails = "messageErrorDetails",
            errorTagsMessage = "errorTagsMessage",
        )
        Assert.assertEquals("messageError", checkEnablerServices.messageError)
        Assert.assertEquals("messageErrorDetails", checkEnablerServices.messageErrorDetails)
        Assert.assertEquals("errorTagsMessage", checkEnablerServices.errorTagsMessage)
    }
}