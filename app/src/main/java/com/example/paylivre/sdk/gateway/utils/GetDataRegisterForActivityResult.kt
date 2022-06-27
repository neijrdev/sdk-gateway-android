package com.example.paylivre.sdk.gateway.utils

import android.content.Intent
import com.paylivre.sdk.gateway.android.domain.model.*

data class RegisterForActivityResultData(
    val registerForActivityResult: String? = null,
    val selected_type: Int? = null,

    val is_error_transaction: Int? = null,
    val error_transaction_code: String? = null,
    val error_transaction_message: String? = null,

    val is_generated_transaction: Int? = null,
    val order_id: Int? = null,
    val order_type_id: Int? = null,
    val order_type_name: String? = null,
    val order_status_id: Int? = null,
    val order_status_name: String? = null,
    val transaction_id: Int? = null,
    val transaction_status_id: Int? = null,
    val transaction_status_name: String? = null,
    val deposit_id: Int? = null,
    val deposit_type_id: Int? = null,
    val deposit_type_name: String? = null,
    val deposit_status_id: Int? = null,
    val deposit_status_name: String? = null,
    val withdrawal_id: Int? = null,
    val withdrawal_type_id: Int? = null,
    val withdrawal_type_name: String? = null,
    val withdrawal_status_id: Int? = null,
    val withdrawal_status_name: String? = null,

    val is_user_completed_transaction: Int? = null,
    val action_not_completed_code: String? = null,
    val action_not_completed_message: String? = null,
    val error_completed_transaction_message: String? = null,
)

fun getValueIntOrNull(value: Int?): Int? {
    return if (value == -1) {
        null
    } else value
}

fun getSdkGatewayExtraData(
    resultCodeValue: String?,
    data: Intent?,
): RegisterForActivityResultData? {
    val selectedType = data!!.getIntExtra("selected_type", -1)

    val orderId = data!!.getIntExtra("order_id", -1)
    val orderTypeId = data!!.getIntExtra("order_type_id", -1)
    val orderTypeName = OrderTypes.fromInt(orderTypeId) ?: ""
    val orderStatusId = data!!.getIntExtra("order_status_id", -1)
    val orderStatusName = OrderStatus.fromInt(orderStatusId) ?: ""

    val transactionId = data!!.getIntExtra("transaction_id", -1)
    val transactionStatusId = data!!.getIntExtra("transaction_status_id", -1)
    val transactionStatusName = TransactionStatus.fromInt(transactionStatusId) ?: ""

    val depositId = data!!.getIntExtra("deposit_id", -1)
    val depositTypeId = data!!.getIntExtra("deposit_type_id", -1)
    val depositTypeName = DepositTypes.fromInt(depositTypeId) ?: ""
    val depositStatusId = data!!.getIntExtra("deposit_status_id", -1)
    val depositStatusName = DepositStatus.fromInt(depositStatusId) ?: ""

    val withdrawalId = data!!.getIntExtra("withdrawal_id", -1)
    val withdrawalTypeId = data!!.getIntExtra("withdrawal_type_id", -1)
    val withdrawalTypeName = WithdrawTypes.fromInt(withdrawalTypeId) ?: ""
    val withdrawalStatusId = data!!.getIntExtra("withdrawal_status_id", -1)
    val withdrawalStatusName = WithdrawStatus.fromInt(withdrawalStatusId) ?: ""

    val isGeneratedTransaction = data!!.getIntExtra("is_generated_transaction", -1)
    val isErrorTransaction = data!!.getIntExtra("is_error_transaction", -1)
    val errorTransactionCode = data!!.getStringExtra("error_transaction_code")
    val errorTransactionMessage = data!!.getStringExtra("error_transaction_message")

    val isUserCompletedTransaction = data!!
        .getIntExtra("is_user_completed_transaction", -1)

    val actionNotCompletedCode = data!!
        .getStringExtra("action_not_completed_code")

    val actionNotCompletedMessage = data!!
        .getStringExtra("action_not_completed_message")

    val errorCompletedTransactionMessage = data!!
        .getStringExtra("error_completed_transaction_message")

    return RegisterForActivityResultData(
        registerForActivityResult = resultCodeValue,
        selected_type = selectedType,

        is_generated_transaction = getValueIntOrNull(isGeneratedTransaction),
        order_id = getValueIntOrNull(orderId),
        order_type_id = getValueIntOrNull(orderTypeId),
        order_type_name = orderTypeName.toString(),
        order_status_id = getValueIntOrNull(orderStatusId),
        order_status_name = orderStatusName.toString(),
        transaction_id = getValueIntOrNull(transactionId),
        transaction_status_id = getValueIntOrNull(transactionStatusId),
        transaction_status_name = transactionStatusName.toString(),
        deposit_id = getValueIntOrNull(depositId),
        deposit_type_id = getValueIntOrNull(depositTypeId),
        deposit_type_name = depositTypeName.toString(),
        deposit_status_id = getValueIntOrNull(depositStatusId),
        deposit_status_name = depositStatusName.toString(),
        withdrawal_id = getValueIntOrNull(withdrawalId),
        withdrawal_type_id = getValueIntOrNull(withdrawalTypeId),
        withdrawal_type_name = withdrawalTypeName.toString(),
        withdrawal_status_id = getValueIntOrNull(withdrawalStatusId),
        withdrawal_status_name = withdrawalStatusName.toString(),

        is_error_transaction = getValueIntOrNull(isErrorTransaction),
        error_transaction_code = errorTransactionCode,
        error_transaction_message = errorTransactionMessage,

        is_user_completed_transaction = getValueIntOrNull(isUserCompletedTransaction),
        action_not_completed_code = actionNotCompletedCode,
        action_not_completed_message = actionNotCompletedMessage,
        error_completed_transaction_message = errorCompletedTransactionMessage,
    )
}