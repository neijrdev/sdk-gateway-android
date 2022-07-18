package com.paylivre.sdk.gateway.android.ui.error

import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.checkIsErrorApiToken

fun checkIsErrorKycLimits(errorMessage: String?): Boolean {
    return if (errorMessage != null) {
        when (errorMessage) {
            "The amount exceeds the limit available for your KYC Level.",
            "Kyc validation errors",
            "User Kyc Limit exceeded",
            -> true
            else -> false
        }
    } else false
}

fun getErrorScreen(error: ErrorTransaction?): Int {
    val isErrorApiToken = checkIsErrorApiToken(error)
    val isErrorKYCLimits = checkIsErrorKycLimits(error?.original_message)
    val isErrorKycUserBlocked = error?.original_message == "User is blocked by Kyc"

    when {
        //Error Invalid Api Token -> Deposit Wallet
        isErrorApiToken -> {
            return R.id.navigation_form_start_payment
        }
        //Error KYC user not limit
        isErrorKYCLimits -> {
            return R.id.navigation_error_kyc_limit
        }
        //Error KYC User Blocked
        isErrorKycUserBlocked -> {
            return R.id.navigation_error_kyc_user_blocked
        }
        else -> {
            return R.id.navigation_fatal_error
        }
    }
}