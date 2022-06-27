package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.getRandomString
import org.junit.Assert
import org.junit.Test

class GetRandomStringTest {
    @Test
    fun `Test getRandomString`() {
        Assert.assertNotEquals("12dasd3456", getRandomString(10))
    }
}