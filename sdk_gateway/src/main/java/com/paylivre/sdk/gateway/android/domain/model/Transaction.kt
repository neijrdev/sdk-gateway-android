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

enum class TransactionTypes(val code: Int) {
    DEPOSIT(0),
    EXCHANGE(1),
    BROKER_DEPOSIT(2),
    BROKER_WITHDRAWAL(3),
    TRANSFER(4),
    WITHDRAWAL(5),
    BROKER_DEPOSIT_BTC(6),
    COLLECT_FEES(7),
    BROKER_PAYMENT(8),
    REFUND_NO_TAX(9),
    REFUND_WITH_TAX(10),
    REFUND_FEES(11),
    BROKER_LOAD(12),
    PAYABLE_BILLET(13),
    ECOMMERCE_DEPOSIT(14),
    REWARDS_POINTS_EXCHANGE(15),
    CASHBACK(16),
    LOSSES(17),
    RECOVERY(18);

    companion object {
        private val map = values().associateBy(TransactionTypes::code)
        fun fromInt(type: Int) = map[type]
    }
}

