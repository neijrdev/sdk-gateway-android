package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen

import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.TransactionStatus

fun getStringIdStatusTransactionById(statusId: Int): Int {
    return when (statusId) {
        TransactionStatus.NEW.code -> R.string.status_new
        TransactionStatus.PENDING.code -> R.string.status_pending
        TransactionStatus.COMPLETED.code -> R.string.status_completed
        TransactionStatus.CANCELLED.code -> R.string.status_cancelled
        TransactionStatus.EXPIRED.code -> R.string.status_expired
        TransactionStatus.REVIEWED.code -> R.string.status_reviewed
        TransactionStatus.REFUND_PENDING.code -> R.string.status_refund_pending
        TransactionStatus.REFUND_COMPLETE.code -> R.string.status_refund_complete
        else -> R.string.status_pending
    }
}

fun getDataStatusTransactionById(statusId: Int): DataStatusTransactionResponse {
    when (statusId) {
        TransactionStatus.NEW.code -> {
            return DataStatusTransactionResponse(
                R.string.status_new,
                R.string.msg_status_new,
                R.drawable.ic_new
            )
        }
        TransactionStatus.PENDING.code -> {
            return DataStatusTransactionResponse(
                R.string.status_pending,
                R.string.msg_status_pending,
                R.drawable.ic_pending
            )
        }
        TransactionStatus.COMPLETED.code -> {
            return DataStatusTransactionResponse(
                R.string.status_completed,
                R.string.msg_status_completed,
                R.drawable.ic_completed
            )
        }
        TransactionStatus.CANCELLED.code -> {
            return DataStatusTransactionResponse(
                R.string.status_cancelled,
                R.string.msg_status_cancelled,
                R.drawable.ic_cancelled
            )
        }
        TransactionStatus.EXPIRED.code -> {
            return DataStatusTransactionResponse(
                R.string.status_expired,
                R.string.msg_status_expired,
                R.drawable.ic_expired
            )
        }

        TransactionStatus.REVIEWED.code -> {
            return DataStatusTransactionResponse(
                R.string.status_reviewed,
                R.string.msg_status_reviewed,
                R.drawable.ic_reviewed
            )
        }
        TransactionStatus.REFUND_PENDING.code -> {
            return DataStatusTransactionResponse(
                R.string.status_refund_pending,
                R.string.msg_status_refund_pending,
                R.drawable.ic_returned_pending
            )
        }

        TransactionStatus.REFUND_COMPLETE.code -> {
            return DataStatusTransactionResponse(
                R.string.status_refund_complete,
                R.string.msg_status_refund_complete,
                R.drawable.ic_returned_complete
            )
        }

        else -> {
            return DataStatusTransactionResponse(
                R.string.status_pending,
                R.string.msg_status_pending,
                R.drawable.ic_pending
            )
        }
    }
}


data class DataStatusTransactionResponse(
    val title_string_id: Int,
    val body_message_string_id: Int,
    val icon_drawable_id: Int
)

