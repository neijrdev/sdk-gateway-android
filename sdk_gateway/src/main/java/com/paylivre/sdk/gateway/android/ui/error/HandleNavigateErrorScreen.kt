package com.paylivre.sdk.gateway.android.ui.error

import android.view.View
import androidx.navigation.Navigation
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

fun handleNavigateToErrorScreen(view: View, error: ErrorTransaction?) {
    val isErrorApiToken = checkIsErrorApiToken(error)
    val isErrorKYCLimits = checkIsErrorKycLimits(error?.original_message)
    val isErrorKycUserBlocked = error?.original_message == "User is blocked by Kyc"

    when {
        //Error Invalid Api Token -> Deposit Wallet
        isErrorApiToken -> {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_form_start_payment)
        }
        //Error KYC user not limit
        isErrorKYCLimits -> {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_error_kyc_limit)
        }
        //Error KYC User Blocked
        isErrorKycUserBlocked -> {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_error_kyc_user_blocked)
        }
        else -> {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_fatal_error)
        }
    }
}