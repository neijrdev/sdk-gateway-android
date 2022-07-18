package com.paylivre.sdk.gateway.android.ui.error.fragment_fatal_error

import android.os.Build
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.ui.error.checkIsErrorKycLimits
import com.paylivre.sdk.gateway.android.ui.error.getErrorScreen
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class ErrorKycLimitExceededTest {

    @Test
    fun `test checkIsErrorKycLimits, given message error is "User Kyc Limit exceeded"`() {
        Assert.assertEquals(true, checkIsErrorKycLimits("User Kyc Limit exceeded"))
        Assert.assertEquals(true, checkIsErrorKycLimits("Kyc validation errors"))
        Assert.assertEquals(true,
            checkIsErrorKycLimits("The amount exceeds the limit available for your KYC Level."))
    }

    @Test
    fun `test getErrorScreen, given original_message error is "User Kyc Limit exceeded"`() {
        Assert.assertEquals(
            R.id.navigation_error_kyc_limit,
            getErrorScreen(ErrorTransaction(original_message = "User Kyc Limit exceeded"))
        )
    }

    @Test
    fun `getErrorScreen, original_message error is "The amount exceeds the limit available for your KYC Level"`() {

        Assert.assertEquals(
            R.id.navigation_error_kyc_limit,
            getErrorScreen(
                ErrorTransaction(
                    original_message = "The amount exceeds the limit available for your KYC Level."))
        )
    }


    @Test
    fun `getErrorScreen, original_message error is "Kyc validation errors"`() {
        Assert.assertEquals(
            R.id.navigation_error_kyc_limit,
            getErrorScreen(
                ErrorTransaction(
                    original_message = "Kyc validation errors"))
        )
    }

}