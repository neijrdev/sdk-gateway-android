package com.paylivre.sdk.gateway.android.data

import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction

fun getGenericErrorData(): ErrorTransaction {
    return ErrorTransaction(
        message = "title_unexpected_error",
        messageDetails = "title_unexpected_error_body",
        errorTags = "UX000"
    )
}