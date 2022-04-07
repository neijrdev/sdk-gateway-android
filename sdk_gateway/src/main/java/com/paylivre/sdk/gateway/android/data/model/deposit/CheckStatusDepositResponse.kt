package com.paylivre.sdk.gateway.android.data.model.deposit


data class CheckStatusDepositResponse(
    val status: String,
    val status_code: Int,
    val message: String,
    val data: DataStatusDeposit?
)


data class DataStatusDeposit(
    val transaction_status_id: Int?,
    val deposit_status_id: Int?,
)