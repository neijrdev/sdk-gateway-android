package com.paylivre.sdk.gateway.android.domain.model

import android.content.Context
import android.content.Intent
import com.paylivre.sdk.gateway.android.data.model.order.CheckStatusOrderResponse
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.order.checkIsErrorApiToken
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.utils.getStringByKey

enum class ErrorCompletedTransaction {
    RC001 //User did not insert proof of deposit type bank transfer
}

data class GetOrderDataResponse(
    val order_id: Int? = null,
    val order_type_id: Int? = null,
    val order_status_id: Int? = null,
)

data class GetWithdrawalDataResponse(
    val withdrawal_id: Int? = null,
    val withdrawal_type_id: Int? = null,
    val withdrawal_status_id: Int? = null,
)

fun getFirstValidInt(value1: Int? = null, value2: Int? = null): Int? {
    return value1 ?: value2
}


class InsertRegisterResultData(var contextActivity: Context? = null) {
    //Others data to return
    var typeSelect: Int? = null

    //Others data
    var operation: String? = null
    var currency: String? = null
    var isErrorWalletInvalidApiToken: Boolean? = null
    var errorTransactionCode: String? = null

    //Data transaction success to return
    //data to return in result
    var isGeneratedTransaction: Int? = null
    var transactionStatusId: Int? = null
    var depositStatusId: Int? = null

    //Others data
    var transactionResponse: StatusTransactionResponse? = null
    var orderStatusResponse: CheckStatusOrderResponse? = null


    //Data transaction failure
    //data to return in result
    var isErrorTransaction: Int? = null
    var errorTransactionMessage: String? = null
    var errorTransactionMessageDetails: String? = null

    var isErrorTransactionTranslatedMessage: Boolean? = null

    //Data user completed transaction
    //data to return in result
    var isUserCompletedTransaction: Int? = null
    var actionNotCompletedCode: String? = null
    var actionNotCompletedMessage: String? = null
    var errorCompletedTransactionMessage: String? = null


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
            messageDetails != null && messageDetails != "" -> {
                "$message $messageDetails"
            }
            message == "" -> null
            else -> message
        }
    }


    private fun getErrorMessageDetails(keyMessage: String?, currency: String?): String? {
        val isErrorWithdrawLimit = keyMessage == "exceeded_withdrawal_limit_value"
        return if (isErrorWithdrawLimit) keyMessage + currency
        else keyMessage
    }


    private fun getWithdrawalData(): GetWithdrawalDataResponse? {
        return GetWithdrawalDataResponse(
            withdrawal_id = getFirstValidInt(
                transactionResponse?.data?.withdrawal?.id,
                orderStatusResponse?.data?.withdrawal?.id
            ),
            withdrawal_type_id = getFirstValidInt(
                transactionResponse?.data?.withdrawal_type_id,
                orderStatusResponse?.data?.withdrawal_type_id
            ),
            withdrawal_status_id = getFirstValidInt(
                transactionResponse?.data?.withdrawal?.status_id,
                orderStatusResponse?.data?.withdrawal?.status_id
            ),
        )
    }


    private fun getOrderData(): GetOrderDataResponse? {
        return if (operation == Operation.WITHDRAW.code.toString()) {
            GetOrderDataResponse(
                order_id = getFirstValidInt(
                    transactionResponse?.data?.order?.id,
                    orderStatusResponse?.data?.order?.id
                ),
                order_type_id = getFirstValidInt(
                    transactionResponse?.data?.order?.type,
                    orderStatusResponse?.data?.order?.type
                ),
                order_status_id = getFirstValidInt(
                    transactionResponse?.data?.order?.status_id,
                    orderStatusResponse?.data?.order?.status_id
                ),
            )
        } else {
            GetOrderDataResponse(
                order_id = transactionResponse?.data?.order_id,
                order_type_id = transactionResponse?.data?.order?.type,
                order_status_id = transactionResponse?.data?.order?.status_id,
            )
        }
    }

    fun setIntentWithStatusOrderDataResponse(checkStatusOrderResponse: CheckStatusOrderResponse?) {
        if (checkStatusOrderResponse?.isSuccess == true) {
            isGeneratedTransaction = 1
            isErrorTransaction = 0
            errorTransactionCode = null
            errorTransactionMessage = null
            errorTransactionMessageDetails = null
            isErrorWalletInvalidApiToken = null
            orderStatusResponse = checkStatusOrderResponse
        } else if (checkStatusOrderResponse?.isSuccess == false) {
            isGeneratedTransaction = 0
            isErrorTransaction = 1
            errorTransactionCode = checkStatusOrderResponse.error?.errorTags
            errorTransactionMessage = checkStatusOrderResponse.error?.message
            errorTransactionMessageDetails =
                getErrorMessageDetails(checkStatusOrderResponse.error?.messageDetails, currency)
            isErrorWalletInvalidApiToken =
                checkIsErrorApiToken(checkStatusOrderResponse?.error)
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
                checkIsErrorApiToken(statusTransactionResponse?.error)
        }
    }

    fun setIntentWithTransferInsertProofResponse(
        insertTransferProofDataResponse: InsertTransferProofDataResponse?,
    ) {
        if (insertTransferProofDataResponse?.isSuccess == true) {
            isUserCompletedTransaction = 1
            actionNotCompletedCode = null
            actionNotCompletedMessage = null
            errorCompletedTransactionMessage = null
        } else if (insertTransferProofDataResponse?.isSuccess != true) {
            isUserCompletedTransaction = 0
            actionNotCompletedCode =
                ErrorCompletedTransaction.RC001.toString()
            actionNotCompletedMessage =
                "User did not insert proof of deposit type bank transfer"
            errorCompletedTransactionMessage = insertTransferProofDataResponse?.error
        }
    }


    fun getIntentWithResultData(intent: Intent): Intent {

        intent.putExtra("selected_type", typeSelect)
        intent.putExtra("is_generated_transaction", isGeneratedTransaction)

        intent.putExtra("order_id", getOrderData()?.order_id)
        intent.putExtra("order_type_id", getOrderData()?.order_type_id)
        intent.putExtra("order_status_id", getOrderData()?.order_status_id)

        intent.putExtra("transaction_id", transactionResponse?.data?.transaction_id)
        intent.putExtra("transaction_status_id", transactionStatusId)

        intent.putExtra("deposit_id", transactionResponse?.data?.deposit_id)
        intent.putExtra("deposit_type_id", transactionResponse?.data?.deposit_type_id)
        intent.putExtra("deposit_status_id", depositStatusId)

        intent.putExtra("withdrawal_id", getWithdrawalData()?.withdrawal_id)
        intent.putExtra("withdrawal_type_id", getWithdrawalData()?.withdrawal_type_id)
        intent.putExtra("withdrawal_status_id", getWithdrawalData()?.withdrawal_status_id)

        intent.putExtra("is_error_transaction", isErrorTransaction)
        intent.putExtra("error_transaction_code", errorTransactionCode)
        intent.putExtra(
            "error_transaction_message",
            getErrorTransactionMessageFormatted(
                getErrorMessage(errorTransactionMessage),
                getErrorMessage(errorTransactionMessageDetails)
            )
        )

        intent.putExtra("is_user_completed_transaction", isUserCompletedTransaction)
        intent.putExtra("action_not_completed_code", actionNotCompletedCode)
        intent.putExtra("action_not_completed_message", actionNotCompletedMessage)
        intent.putExtra("error_completed_transaction_message", errorCompletedTransactionMessage)

        return intent
    }
}