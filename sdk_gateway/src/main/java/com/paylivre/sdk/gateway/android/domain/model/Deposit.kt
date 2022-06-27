package com.paylivre.sdk.gateway.android.domain.model

enum class DepositStatus(val code: Int) {
    NEW(0),
    PENDING(1),
    COMPLETED(2),
    CANCELLED(3),
    EXPIRED(4),
    REVIEWED(5),
    REFUND_PENDING(6),
    REFUND_COMPLETE(7);

    companion object {
        private val map = values().associateBy(DepositStatus::code)
        fun fromInt(type: Int) = map[type]
    }
}

enum class DepositTypes(val code: Int) {
    WIRE_TRANSFER(0),
    BILLET(1),
    CRYPTOCURRENCY(2),
    DEBIT(3),
    PIX(4),
    WALLET (5);

    companion object {
        private val map = values().associateBy(DepositTypes::code)
        fun fromInt(type: Int) = map[type]
    }
}

enum class OriginTypeInsertProof(val code: Int) {
    GALLERY(0),
    CAMERA(1),
}

