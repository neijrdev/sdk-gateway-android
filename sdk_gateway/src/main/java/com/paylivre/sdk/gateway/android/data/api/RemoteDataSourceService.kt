package com.paylivre.sdk.gateway.android.data.api

import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse

interface RemoteDataSourceService {
    fun getPixApprovalTime(onResponse: (PixApprovalTimeResponse?, Throwable?) -> Unit)
    fun getServicesStatus(onResponse: (ServiceStatusResponseAdapter?, Throwable?) -> Unit)
    fun newTransaction(
        dataRequest: OrderDataRequest,
        onResponse: (ResponseCommonTransactionData?, ErrorTransaction?) -> Unit,
    )
    fun checkStatusDeposit(
        depositId: Int,
        onResponse: (CheckStatusDepositResponse?, ErrorTransaction?) -> Unit,
    )
    fun checkStatusTransaction(
        transactionId: Int,
        onResponse: (CheckStatusTransactionResponse?, ErrorTransaction?) -> Unit,
    )
    fun transferProof(
        dataRequest: InsertTransferProofDataRequest,
        onResponse: (InsertTransferProofDataResponse?, Throwable?) -> Unit,
    )
    fun checkStatusOrder(
        dataRequest: CheckStatusOrderDataRequest,
        onResponse: (CheckStatusOrderDataResponse?, ErrorTransaction?) -> Unit,
    )
}