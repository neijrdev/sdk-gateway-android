package com.paylivre.sdk.gateway.android.data.model.pixApprovalTime

data class PixApprovalTimeResponse(
    val status: String,
    val status_code: Int,
    val message: String,
    val data: DataPixApprovalTime?
)

data class DataPixApprovalTime(
    val deposit_age_minutes : String,
    val deposit_status_id: String,
    val average_age: String,
    val level: String,
    val level_id: Int,
)