package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.validateMerchantData
import org.junit.Assert
import org.junit.Test

class ValidateMerchantDataTest {
    @Test
    fun `Test validateMerchantData`() {
        Assert.assertEquals(
            true,
            validateMerchantData(
                132,
                "http://test.com.br"
            ).isValid
        )
        Assert.assertEquals(
            false,
            validateMerchantData(
                12,
                ""
            ).isValid
        )
        Assert.assertEquals(
            false,
            validateMerchantData(
                0,
                "http://test.com.br"
            ).isValid
        )
    }
}