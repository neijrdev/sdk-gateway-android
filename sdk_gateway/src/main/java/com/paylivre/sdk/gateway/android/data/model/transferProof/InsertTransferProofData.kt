package com.paylivre.sdk.gateway.android.data.model.transferProof

import java.io.File

enum class ImageTypes(name: String) {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
}

data class InsertTransferProofDataRequest(
    val file: File,
    val order_id: Int,
    val token: String,
)

data class InsertTransferProofDataResponse(
    val id: Int?,
    val proof: Proof?,
    val wallet_id: Int?,
    val user_id: Int?,
    val deposit_status_id: Int?,
    val loading: Boolean? = null,
    val error: String? = null,
    val isSuccess: Boolean? = null,
)

data class Proof(
    val id: Int?,
    val deposit_id: Int?,
    val withdrawal_id: Int?,
    val filename: String?,
    val original_filename: String?,
    val upload_status_id: Int?,
    val amount: Int?,
    val proof_date: String?,
    val url: String?,
    val originalUrl: String?
)