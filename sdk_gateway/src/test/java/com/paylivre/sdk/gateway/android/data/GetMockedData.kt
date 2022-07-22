package com.paylivre.sdk.gateway.android.data

import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.DataPixApprovalTime
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServicesStatus
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.DataStatusTransaction
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import java.io.File

fun getMockDataPixApprovalTime(): DataPixApprovalTime {
    return DataPixApprovalTime(
        deposit_age_minutes = "30",
        deposit_status_id = "2",
        average_age = "00:00:00",
        level = "normal",
        level_id = 1
    )
}

fun getMockPixApprovalTimeResponse(
    mockDataPixApprovalTime: DataPixApprovalTime? = getMockDataPixApprovalTime(),
): PixApprovalTimeResponse {
    return PixApprovalTimeResponse(
        status = "success",
        status_code = 200,
        message = "OK",
        data = mockDataPixApprovalTime
    )
}

fun getDataServiceStatus(): ServicesStatus {
    return ServicesStatus(
        statusPix = true,
        statusBillet = true,
        statusWallet = true,
        statusWiretransfer = true
    )
}


fun getMockServiceStatusResponse(): ServiceStatusResponseAdapter {
    return ServiceStatusResponseAdapter(
        status = "success",
        status_code = 200,
        message = "OK",
        data = getDataServiceStatus()
    )
}


fun getOrderDataRequest(
    base_url: String = "",
    merchant_id: Int = -1,
    merchant_transaction_id: String = "",
    amount: String = "",
    currency: String = "",
    operation: String = "",
    callback_url: String = "",
    type: String = "",
    selected_type: String = "",
    account_id: String = "",
    email: String = "",
    document_number: String = "",
    login_email: String? = null,
    api_token: String? = null,
    pix_key_type: String? = null,
    pix_key: String? = null,
    signature: String = "",
    url: String = "",
    auto_approve: String = "",
    redirect_url: String? = null,
    logo_url: String? = null,
): OrderDataRequest {
    return OrderDataRequest(
        base_url = base_url,
        merchant_id = merchant_id,
        merchant_transaction_id = merchant_transaction_id,
        amount = amount,
        currency = currency,
        operation = operation,
        callback_url = callback_url,
        type = type,
        selected_type = selected_type,
        account_id = account_id,
        email = email,
        document_number = document_number,
        login_email = login_email,
        api_token = api_token,
        pix_key_type = pix_key_type,
        pix_key = pix_key,
        signature = signature,
        url = url,
        auto_approve = auto_approve,
        redirect_url = redirect_url,
        logo_url = logo_url,
    )
}

fun getMockNewTransactionResponse(): ResponseCommonTransactionData {
    return ResponseCommonTransactionData(
        full_name = null,
        document_number = null,
        kyc_limits = null,
        original_amount = null,
        origin_amount = null,
        original_currency = null,
        final_amount = null,
        converted_amount = null,
        taxes = null,
        receivable_url = null,
        deposit_type_id = null,
        redirect_url = null,
        billet_digitable_line = null,
        billet_due_date = null,
        deposit_id = null,
        order_id = null,
        transaction_id = null,
        withdrawal = null,
        order = null,
        bank_accounts = null,
        verification_token = null,
        token = null,
        withdrawal_type_id = null,
    )
}

fun getMockDataStatusDeposit(): DataStatusDeposit {
    return DataStatusDeposit(
        transaction_status_id = null,
        deposit_status_id = 0
    )
}

fun getMockCheckStatusDeposit(): CheckStatusDepositResponse {
    return CheckStatusDepositResponse(
        status = "success",
        status_code = 200,
        message = "OK",
        data = getMockDataStatusDeposit()
    )
}

fun getDataStatusTransaction(): DataStatusTransaction {
    return DataStatusTransaction(
        transaction_status_id = 0
    )
}

fun getMockCheckStatusTransaction(): CheckStatusTransactionResponse {
    return CheckStatusTransactionResponse(
        status = "success",
        status_code = 200,
        message = "OK",
        data = getDataStatusTransaction()
    )
}

fun getMockInsertTransferProofDataRequest(): InsertTransferProofDataRequest {
    return InsertTransferProofDataRequest(
        file = File("123456"),
        order_id = 12345,
        token = "asd456123asd"
    )
}

fun getMockTransferProofResponse(): InsertTransferProofDataResponse {
    return InsertTransferProofDataResponse(
        id = null,
        proof = null,
        wallet_id = null,
        user_id = null,
        deposit_status_id = null,
        loading = false,
        error = null,
        isSuccess = true
    )
}

fun getMockCheckStatusOrderDataRequest(): CheckStatusOrderDataRequest {
    return CheckStatusOrderDataRequest(
        order_id = 12345,
        token = "asd456123asd"
    )
}

fun getMockCheckStatusOrderDataResponse(): CheckStatusOrderDataResponse {
    return CheckStatusOrderDataResponse()
}