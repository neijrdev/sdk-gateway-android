package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.api.RemoteDataSourceService
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse

class RemoteDataSourceMock(
    private val mockPixApprovalTimeResponse: PixApprovalTimeResponse? = null,
    private val mockServicesStatusResponse: ServiceStatusResponseAdapter? = null,
    private val mockNewTransactionResponse: ResponseCommonTransactionData? = null,
    private val mockCheckStatusDepositResponse: CheckStatusDepositResponse? = null,
    private val mockCheckStatusTransactionResponse: CheckStatusTransactionResponse? = null,
    private val mockTransferProofResponse: InsertTransferProofDataResponse? = null,
    private val mockCheckStatusOrderDataResponse: CheckStatusOrderDataResponse? = null,
    private val mockResponseThrowable: Throwable? = null,
    private val mockResponseErrorTransaction: ErrorTransaction? = null,
    private val runErrorRuntimeGeneric: Boolean = false,
    private val messageRunErrorRuntimeGeneric: String = "ErrorRuntimeGeneric!",
) : RemoteDataSourceService {
    var calledGetPixApprovalTime = false
    var calledGetServiceStatus = false
    var calledNewTransaction = false
    var orderDataRequestReceived: OrderDataRequest? = null
    var calledCheckStatusDeposit = false
    var depositIdReceived: Int? = null
    var calledCheckStatusTransaction = false
    var transactionIdReceived: Int? = null
    var calledTransferProof = false
    var transferProofDataRequestReceived: InsertTransferProofDataRequest? = null
    var calledCheckStatusOrder = false
    var checkStatusOrderDataRequestReceived: CheckStatusOrderDataRequest? = null

    override fun getPixApprovalTime(onResponse: (PixApprovalTimeResponse?, Throwable?) -> Unit) {
        calledGetPixApprovalTime = true
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockPixApprovalTimeResponse, mockResponseThrowable)
    }

    override fun getServicesStatus(onResponse: (ServiceStatusResponseAdapter?, Throwable?) -> Unit) {
        calledGetServiceStatus = true
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockServicesStatusResponse, mockResponseThrowable)
    }

    override fun newTransaction(
        dataRequest: OrderDataRequest,
        onResponse: (ResponseCommonTransactionData?, ErrorTransaction?) -> Unit,
    ) {
        calledNewTransaction = true
        orderDataRequestReceived = dataRequest
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockNewTransactionResponse, mockResponseErrorTransaction)
    }

    override fun checkStatusDeposit(
        depositId: Int,
        onResponse: (CheckStatusDepositResponse?, ErrorTransaction?) -> Unit,
    ) {
        calledCheckStatusDeposit = true
        depositIdReceived = depositId
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockCheckStatusDepositResponse, mockResponseErrorTransaction)
    }

    override fun checkStatusTransaction(
        transactionId: Int,
        onResponse: (CheckStatusTransactionResponse?, ErrorTransaction?) -> Unit,
    ) {
        calledCheckStatusTransaction = true
        transactionIdReceived = transactionId
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockCheckStatusTransactionResponse, mockResponseErrorTransaction)
    }

    override fun transferProof(
        dataRequest: InsertTransferProofDataRequest,
        onResponse: (InsertTransferProofDataResponse?, Throwable?) -> Unit,
    ) {
        calledTransferProof = true
        transferProofDataRequestReceived = dataRequest
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockTransferProofResponse, mockResponseThrowable)
    }

    override fun checkStatusOrder(
        dataRequest: CheckStatusOrderDataRequest,
        onResponse: (CheckStatusOrderDataResponse?, ErrorTransaction?) -> Unit,
    ) {
        calledCheckStatusOrder = true
        checkStatusOrderDataRequestReceived = dataRequest
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        onResponse(mockCheckStatusOrderDataResponse, mockResponseErrorTransaction)
    }
}