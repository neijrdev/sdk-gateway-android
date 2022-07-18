package com.paylivre.sdk.gateway.android.domain.model
import com.paylivre.sdk.gateway.android.R

const val LIMIT_VALUE_WITHDRAW_USD = "100000"
const val LIMIT_VALUE_WITHDRAW_BRL = "500000"

enum class WithdrawTypes(val code: Int) {
    PIX(4),
    WALLET(5);

    companion object {
        private val map = values().associateBy(WithdrawTypes::code)
        fun fromInt(type: Int) = map[type]
    }
}

enum class WithdrawStatus(val code: Int) {
    NEW(0),
    APPROVED(1),
    CANCELLED(2),
    REFUNDED(3),
    ERROR(4);

    companion object {
        private val map = values().associateBy(WithdrawStatus::code)
        fun fromInt(type: Int) = map[type]
    }
}

fun getStringIdStatusWithdrawById(statusId: Int): Int {
    return when (statusId) {
        WithdrawStatus.NEW.code -> R.string.status_awaiting_payment
        WithdrawStatus.APPROVED.code -> R.string.status_pending
        WithdrawStatus.CANCELLED.code -> R.string.status_cancelled
        WithdrawStatus.REFUNDED.code -> R.string.status_refunded
        WithdrawStatus.ERROR.code -> R.string.status_error
        else -> R.string.status_incomplete
    }
}


enum class WithdrawStatusOrder(val code: Int) {
    NEW(0),
    PENDING(1),
    APPROVED(2),
    CANCELLED(3),
    EXPIRED(4),
    INCOMPLETE(5);

    companion object {
        private val map = values().associateBy(WithdrawStatusOrder::code)
        fun fromInt(type: Int) = map[type]
    }
}

fun getStringIdStatusWithdrawOrderById(statusId: Int): Int {
    return when (statusId) {
        WithdrawStatusOrder.NEW.code -> R.string.status_awaiting_payment
        WithdrawStatusOrder.PENDING.code -> R.string.status_pending
        WithdrawStatusOrder.APPROVED.code -> R.string.status_completed
        WithdrawStatusOrder.CANCELLED.code -> R.string.status_cancelled
        WithdrawStatusOrder.EXPIRED.code -> R.string.status_expired
        WithdrawStatusOrder.INCOMPLETE.code -> R.string.status_incomplete
        else -> R.string.status_incomplete
    }
}