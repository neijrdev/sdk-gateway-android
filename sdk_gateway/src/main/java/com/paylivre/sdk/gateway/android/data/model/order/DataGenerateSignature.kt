package com.paylivre.sdk.gateway.android.data.model.order

import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type


data class DataOrderRequestToHandle(
    val selected_type: String = "",
    val operation: String = "",
    val login_email: String? = "",
    val api_token: String? = "",
    val pix_key_type: String? = "",
    val pix_key: String? = "",
)

data class ResponseDataOrderRequestToHandle(
    val login_email: String? = "",
    val api_token: String? = "",
    val pix_key_type: String? = "",
    val pix_key: String? = "",
)

fun handlesDataOrderRequest(
    dataOrderRequestToHandle: DataOrderRequestToHandle,
): ResponseDataOrderRequestToHandle {
    val loginEmail =
        if (dataOrderRequestToHandle.operation == Operation.DEPOSIT.code.toString()
            && dataOrderRequestToHandle.selected_type == Type.WALLET.code.toString()
        )
            dataOrderRequestToHandle.login_email else ""

    val apiToken =
        if (dataOrderRequestToHandle.operation == Operation.DEPOSIT.code.toString()
            && dataOrderRequestToHandle.selected_type == Type.WALLET.code.toString()
        )
            dataOrderRequestToHandle.api_token else ""

    val pixKeyType =
        if (dataOrderRequestToHandle.operation == Operation.WITHDRAW.code.toString()
            && dataOrderRequestToHandle.selected_type == Type.PIX.code.toString()
        )
            dataOrderRequestToHandle.pix_key_type else ""

    val pixKey =
        if (dataOrderRequestToHandle.operation == Operation.WITHDRAW.code.toString()
            && dataOrderRequestToHandle.selected_type == Type.PIX.code.toString()
        )
            dataOrderRequestToHandle.pix_key else ""

    return ResponseDataOrderRequestToHandle(
        loginEmail, apiToken, pixKeyType, pixKey
    )
}


fun getProcessedDataOrderRequest(dataGenerateSignature: DataGenerateSignature): DataGenerateSignature {

    val handledDataOrderRequest = handlesDataOrderRequest(
        DataOrderRequestToHandle(
            selected_type = dataGenerateSignature.selected_type,
            operation = dataGenerateSignature.operation,
            login_email = dataGenerateSignature.login_email,
            api_token = dataGenerateSignature.api_token,
            pix_key_type = dataGenerateSignature.pix_key_type,
            pix_key = dataGenerateSignature.pix_key
        )
    )

    return DataGenerateSignature(
        dataGenerateSignature.base_url,
        dataGenerateSignature.merchant_id,
        dataGenerateSignature.gateway_token,
        dataGenerateSignature.merchant_transaction_id,
        dataGenerateSignature.amount,
        dataGenerateSignature.currency,
        dataGenerateSignature.operation,
        dataGenerateSignature.callback_url,
        dataGenerateSignature.type,
        dataGenerateSignature.selected_type,
        dataGenerateSignature.account_id,
        dataGenerateSignature.email,
        dataGenerateSignature.document_number,
        handledDataOrderRequest.login_email,
        handledDataOrderRequest.api_token,
        handledDataOrderRequest.pix_key_type,
        handledDataOrderRequest.pix_key,
        dataGenerateSignature.auto_approve
    )
}

data class DataGenerateSignature(
    var base_url: String,
    val merchant_id: Int,
    val gateway_token: String,
    val merchant_transaction_id: String,
    val amount: String,
    val currency: String,
    val operation: String,
    val callback_url: String,
    val type: String,
    var selected_type: String,
    val account_id: String,
    val email: String,
    val document_number: String,
    val login_email: String?,
    val api_token: String?,
    val pix_key_type: String?,
    val pix_key: String?,
    val auto_approve: Int,
)

data class OrderDataRequest(
    var base_url: String,
    val merchant_id: Int,
    val merchant_transaction_id: String,
    val amount: String,
    val currency: String,
    val operation: String,
    val callback_url: String,
    val type: String,
    val selected_type: String,
    val account_id: String,
    val email: String,
    val document_number: String,
    var login_email: String?,
    var api_token: String?,
    var pix_key_type: String?,
    var pix_key: String?,
    var signature: String = "",
    var url: String = "",
    var auto_approve: String = "",
    var redirect_url: String? = "",
    var logo_url: String? = "",
)

