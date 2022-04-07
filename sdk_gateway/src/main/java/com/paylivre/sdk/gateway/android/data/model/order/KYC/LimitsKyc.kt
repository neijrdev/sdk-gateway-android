package com.paylivre.sdk.gateway.android.data.model.order.KYC

data class LimitsKyc (
    val available_amount: Double?,
    val limit: Double?,
    val kyc_level: String?,
    val kyc_level_name: String?,
    val used_limit: Double?
)