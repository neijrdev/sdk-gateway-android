package com.paylivre.sdk.gateway.android.domain.model

import android.content.Context
import android.content.Intent
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.utils.getStringByKey

enum class ErrorCompletedTransaction {
    RC001 //User did not insert proof of deposit type bank transfer
}

class InsertRegisterResultData(var contextActivity: Context? = null) {
    var typeSelect: Int? = null
    var transactionStatusId: Int? = null
    var depositStatusId: Int? = null
    var transactionResponse: StatusTransactionResponse? = null
    var isGeneratedTransaction: Int? = null
    var isErrorTransaction: Int? = null
    var isErrorWalletInvalidApiToken: Boolean? = null
    var isErrorKYCLimit: Boolean? = null
    var errorTransactionCode: String? = null
    var isErrorTransactionTranslatedMessage: Boolean? = null
    var errorTransactionMessage: String? = null
    var errorTransactionMessageDetails: String? = null
    var isUserCompletedTransaction: Int? = null
    var actionNotCompletedCode: String? = null
    var actionNotCompletedMessage: String? = null
    var errorCompletedTransactionMessage: String? = null
    var currency: String? = null

    private fun getErrorMessage(message: String?): String {
        return if (isErrorTransactionTranslatedMessage == true) {
            message.toString()
        } else {
            getStringByKey(contextActivity, message ?: "-")
        }
    }

    private fun getErrorTransactionMessageFormatted(
        message: String?,
        messageDetails: String?,
    ): String? {
        return when {
            isErrorWalletInvalidApiToken == true -> {
                getStringByKey(contextActivity, "error_invalid_api_token")
            }
            isErrorKYCLimit == true -> {
                getStringByKey(contextActivity, "error_kyc_limit_title")
            }
            messageDetails != null && messageDetails != "" -> {
                "$message $messageDetails"
            }
            message == "" -> {
                null
            }
            else -> message
        }
    }


    private fun getErrorMessageDetails(keyMessage: String?, currency: String?): String? {
        val isErrorWithdrawLimit = keyMessage == "exceeded_withdrawal_limit_value"
        return if (isErrorWithdrawLimit) {
            keyMessage + currency
        } else {
            keyMessage
        }
    }

    fun setIntentWithTransactionResponse(statusTransactionResponse: StatusTransactionResponse?) {
        if (statusTransactionResponse?.isSuccess == true) {
            transactionResponse = statusTransactionResponse
            isGeneratedTransaction = 1
            isErrorTransaction = 0
            errorTransactionCode = null
            errorTransactionMessage = null
            errorTransactionMessageDetails = null
            isErrorWalletInvalidApiToken = null
            isErrorKYCLimit = null

            if (typeSelect == Type.WIRETRANSFER.code) {
                isUserCompletedTransaction = 0
                actionNotCompletedCode = ErrorCompletedTransaction.RC001.toString()
                actionNotCompletedMessage =
                    "User did not insert proof of deposit type bank transfer"
                errorCompletedTransactionMessage = null
            } else isUserCompletedTransaction = 1

        } else if (statusTransactionResponse?.isSuccess == false) {
            isGeneratedTransaction = 0
            isErrorTransaction = 1
            errorTransactionCode = statusTransactionResponse.error?.errorTags
            errorTransactionMessage = statusTransactionResponse.error?.message
            errorTransactionMessageDetails =
                getErrorMessageDetails(statusTransactionResponse.error?.messageDetails, currency)
            isErrorWalletInvalidApiToken =
                statusTransactionResponse?.isErrorWalletInvalidApiToken
            isErrorKYCLimit = statusTransactionResponse?.isErrorKYCLimit
        }
    }

    fun setIntentWithTransferInsertProofResponse(
        insertTransferProofDataResponse: InsertTransferProofDataResponse?,
    ) {
        if (insertTransferProofDataResponse?.isSuccess == true) {
            actionNotCompletedCode = null
            actionNotCompletedMessage = null
            errorCompletedTransactionMessage = null
        } else
            if (insertTransferProofDataResponse?.isSuccess != false) {
                actionNotCompletedCode =
                    ErrorCompletedTransaction.RC001.toString()
                actionNotCompletedMessage =
                    "User did not insert proof of deposit type bank transfer"
                errorCompletedTransactionMessage = insertTransferProofDataResponse?.error
            }
    }

    fun getIntentWithResultData(intent: Intent): Intent {

        intent?.putExtra("selected_type", typeSelect)

        intent?.putExtra("is_generated_transaction", isGeneratedTransaction)

        intent?.putExtra("deposit_id", transactionResponse?.data?.deposit_id)
        intent?.putExtra("transaction_id", transactionResponse?.data?.transaction_id)
        intent?.putExtra("order_id", transactionResponse?.data?.order_id)

        intent?.putExtra("transaction_status_id", transactionStatusId)
        intent?.putExtra("deposit_status_id", depositStatusId)

        intent?.putExtra("is_error_transaction", isErrorTransaction)
        intent?.putExtra("error_transaction_code", errorTransactionCode)

        intent?.putExtra(
            "error_transaction_message",
            getErrorTransactionMessageFormatted(
                getErrorMessage(errorTransactionMessage),
                getErrorMessage(errorTransactionMessageDetails)
            )
        )

        intent?.putExtra(
            "is_user_completed_transaction", isUserCompletedTransaction
        )
        intent?.putExtra("action_not_completed_code", actionNotCompletedCode)
        intent?.putExtra("action_not_completed_message", actionNotCompletedMessage)
        intent?.putExtra("error_completed_transaction_message", errorCompletedTransactionMessage)

        return intent
    }
}