package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import com.paylivre.sdk.gateway.android.R


enum class WithdrawTypes(val code: Int) {
    PIX(4),
    WALLET(5),
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

fun getDataStatusWithdrawById(statusId: Int?): DataStatusWithdrawResponse {
    when (statusId) {
        WithdrawStatus.NEW.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_awaiting_payment,
                R.string.status_awaiting_payment,
                R.drawable.ic_rotate_cw
            )
        }
        WithdrawStatus.APPROVED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_completed,
                R.string.msg_status_completed,
                R.drawable.ic_completed
            )
        }
        WithdrawStatus.CANCELLED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_cancelled,
                R.string.msg_status_cancelled,
                R.drawable.ic_cancelled
            )
        }
        WithdrawStatus.REFUNDED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_refunded,
                R.string.status_refunded,
                R.drawable.ic_returned_complete
            )
        }

        WithdrawStatus.ERROR.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_error,
                R.string.status_error,
                R.drawable.ic_error
            )
        }

        else -> {
            return DataStatusWithdrawResponse(
                R.string.status_incomplete,
                R.string.status_incomplete,
                R.drawable.ic_incomplete
            )
        }
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

fun getDataStatusWithdrawOrderById(statusId: Int?): DataStatusWithdrawResponse {
    when (statusId) {
        WithdrawStatusOrder.NEW.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_awaiting_payment,
                R.string.status_awaiting_payment,
                R.drawable.ic_rotate_cw
            )
        }
        WithdrawStatusOrder.PENDING.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_pending,
                R.string.msg_status_pending,
                R.drawable.ic_pending
            )
        }
        WithdrawStatusOrder.APPROVED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_completed,
                R.string.msg_status_completed,
                R.drawable.ic_completed
            )
        }
        WithdrawStatusOrder.CANCELLED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_cancelled,
                R.string.msg_status_cancelled,
                R.drawable.ic_cancelled
            )
        }
        WithdrawStatusOrder.EXPIRED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_expired,
                R.string.msg_status_expired,
                R.drawable.ic_expired
            )
        }
        WithdrawStatusOrder.INCOMPLETE.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_incomplete,
                R.string.status_incomplete,
                R.drawable.ic_incomplete
            )
        }

        else -> {
            return DataStatusWithdrawResponse(
                R.string.status_incomplete,
                R.string.status_incomplete,
                R.drawable.ic_incomplete
            )
        }
    }
}

enum class MerchantApprovalStatusOrder(val code: Int) {
    NEW(0),
    PENDING(1),
    APPROVED(2),
    CANCELLED(3),
    EXPIRED(4),
    INCOMPLETE(5);

    companion object {
        private val map = values().associateBy(MerchantApprovalStatusOrder::code)
        fun fromInt(type: Int) = map[type]
    }
}


fun getDataStatusMerchantApprovalById(statusId: Int?): DataStatusWithdrawResponse {
    when (statusId) {
        WithdrawStatusOrder.NEW.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_new,
                R.string.status_new,
                R.drawable.ic_new
            )
        }
        WithdrawStatusOrder.PENDING.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_pending,
                R.string.msg_status_pending,
                R.drawable.ic_pending
            )
        }
        WithdrawStatusOrder.APPROVED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_completed,
                R.string.msg_status_completed,
                R.drawable.ic_completed
            )
        }
        WithdrawStatusOrder.CANCELLED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_cancelled,
                R.string.msg_status_cancelled,
                R.drawable.ic_cancelled
            )
        }
        WithdrawStatusOrder.EXPIRED.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_expired,
                R.string.msg_status_expired,
                R.drawable.ic_expired
            )
        }
        WithdrawStatusOrder.INCOMPLETE.code -> {
            return DataStatusWithdrawResponse(
                R.string.status_incomplete,
                R.string.status_incomplete,
                R.drawable.ic_incomplete
            )
        }

        else -> {
            return DataStatusWithdrawResponse(
                R.string.status_incomplete,
                R.string.status_incomplete,
                R.drawable.ic_incomplete
            )
        }
    }
}

data class DataStatusWithdrawResponse(
    val title_string_id: Int,
    val body_message_string_id: Int,
    val icon_drawable_id: Int,
)

