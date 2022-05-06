package com.paylivre.sdk.gateway.android.data.model.transaction


data class CheckStatusTransactionResponse(
    val status: String,
    val status_code: Int?,
    val message: String?,
    val data: DataStatusTransaction?
)


data class DataStatusTransaction(
    val transaction_status_id: Int?,
)