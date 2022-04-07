package com.paylivre.sdk.gateway.android

import android.os.Build
import com.paylivre.sdk.gateway.android.domain.model.validateBaseUrl
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers="pt-port")
class ValidateBaseUrlTest {
    @Test
    fun `Test validate base url DEV`() {
        val mockBaseUrl = "https://dev.gateway.paylivre.com"
        Assert.assertEquals(
            true,
            validateBaseUrl(
                mockBaseUrl,
            ).isValid
        )
    }

    @Test
    fun `Test validate base url PLAYGROUND`() {
        val mockBaseUrl = "https://playground.gateway.paylivre.com"
        Assert.assertEquals(
            true,
            validateBaseUrl(
                mockBaseUrl,
            ).isValid
        )
    }

    @Test
    fun `Test validate base url PRODUCTION`() {
        val mockBaseUrl = "https://app.gateway.paylivre.com"
        Assert.assertEquals(
            true,
            validateBaseUrl(
                mockBaseUrl,
            ).isValid
        )
    }


    @Test
    fun `Test validate base url invalid - not https`() {
        val mockBaseUrl = "https:app.gateway.paylivre.com"
        Assert.assertEquals(
            false,
            validateBaseUrl(
                mockBaseUrl,
            ).isValid
        )
        Assert.assertEquals(
            "RP001",
            validateBaseUrl(
                mockBaseUrl,
            ).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `Test validate base url invalid`() {
        val mockUrlValid = "https://paylivre.com"
        Assert.assertEquals(
            false,
            validateBaseUrl(
                mockUrlValid,
            ).isValid
        )
        Assert.assertEquals(
            "RP001",
            validateBaseUrl(
                mockUrlValid,
            ).errorTags?.joinToString { it }
        )
    }
}