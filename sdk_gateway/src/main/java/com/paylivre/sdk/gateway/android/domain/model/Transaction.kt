package com.paylivre.sdk.gateway.android.domain.model

enum class TransactionStatus(val code: Int) {
    NEW(0),
    PENDING(1),
    COMPLETED(2),
    CANCELLED(3),
    EXPIRED(4),
    REVIEWED(5),
    REFUND_PENDING(6),
    REFUND_COMPLETE(7);

    companion object {
        private val map = values().associateBy(TransactionStatus::code)
        fun fromInt(type: Int) = map[type]
    }
}
