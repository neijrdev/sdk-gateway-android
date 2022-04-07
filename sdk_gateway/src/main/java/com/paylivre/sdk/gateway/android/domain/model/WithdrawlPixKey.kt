package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest

fun addDdiInPixKeyCellPhoneWithdraw(orderDataRequest: OrderDataRequest): OrderDataRequest {
    return if (orderDataRequest.operation == Operation.WITHDRAW.code.toString() &&
        orderDataRequest.pix_key_type == TypePixKey.PHONE.code.toString()
    ) {
        val ddiBrlDefault = "+55"
        orderDataRequest.pix_key = ddiBrlDefault + orderDataRequest.pix_key
        orderDataRequest
    } else orderDataRequest
}