package com.paylivre.sdk.gateway.android.domain.model

enum class OrderStatus(val code: Int) {
    NEW(0),
    PENDING(1),
    APPROVED(2),
    CANCELLED(3),
    EXPIRED(4),
    INCOMPLETE(5);

    companion object {
        private val map = values().associateBy(OrderStatus::code)
        fun fromInt(type: Int) = map[type]
    }
}

enum class OrderTypes(val code: Int) {
    WIRE_TRANSFER(0),
    BILLET(1),
    DEBIT(3),
    PIX(4),
    WITHDRAWAL(5),
    WALLET_PAYMENT(6),
    WALLET_WITHDRAWAL(7);

    companion object {
        private val map = values().associateBy(OrderTypes::code)
        fun fromInt(type: Int) = map[type]
    }
}