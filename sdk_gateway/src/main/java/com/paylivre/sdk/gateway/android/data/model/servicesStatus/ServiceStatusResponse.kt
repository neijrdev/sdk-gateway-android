package com.paylivre.sdk.gateway.android.data.model.servicesStatus

import com.google.gson.annotations.SerializedName

data class ServiceStatusResponse(
    val status: String,
    val status_code: Int,
    val message: String,
    val data: List<DataServiceStatus>,
)

data class DataServiceStatus(
    @SerializedName(
        value = "wire_transfer",
        alternate = [
            "billet",
            "cryptocurrency",
            "debit",
            "pix"
        ]
    )
    val service: Item,
)

data class Item(
    val status: Int,
    val id: Int,
)