package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.PaymentRepository
import com.paylivre.sdk.gateway.android.data.getOrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse


class PaymentRepositoryTestUtils(mockRemoteDataSource: RemoteDataSourceMock) {
    val paymentRepository = PaymentRepository(mockRemoteDataSource)

    fun callGetPixApprovalTime(): Pair<PixApprovalTimeResponse?, Throwable?> {
        var getPixApprovalTimeFailureReceived: Throwable? = null
        var getPixApprovalTimeSuccessReceived: PixApprovalTimeResponse? = null

        fun getPixApprovalTimeFailure(error: Throwable) {
            getPixApprovalTimeFailureReceived = error
        }

        fun getPixApprovalTimeSuccess(response: PixApprovalTimeResponse) {
            getPixApprovalTimeSuccessReceived = response
        }

        paymentRepository.getPixApprovalTime(
            ::getPixApprovalTimeSuccess,
            ::getPixApprovalTimeFailure)

        return Pair(getPixApprovalTimeSuccessReceived, getPixApprovalTimeFailureReceived)
    }

    fun callGetServiceStatus(): Pair<ServiceStatusResponseAdapter?, Throwable?> {
        var getServiceStatusFailureReceived: Throwable? = null
        var getServiceStatusSuccessReceived: ServiceStatusResponseAdapter? = null

        fun getPixApprovalTimeFailure(error: Throwable) {
            getServiceStatusFailureReceived = error
        }

        fun getPixApprovalTimeSuccess(response: ServiceStatusResponseAdapter) {
            getServiceStatusSuccessReceived = response
        }

        paymentRepository.getServiceStatus(
            ::getPixApprovalTimeSuccess,
            ::getPixApprovalTimeFailure)

        return Pair(getServiceStatusSuccessReceived, getServiceStatusFailureReceived)
    }

    fun callNewTransaction(
        orderDataRequest: OrderDataRequest = getOrderDataRequest(),
    ): Triple<OrderDataRequest, ResponseCommonTransactionData?, ErrorTransaction?> {
        var newTransactionFailureReceived: ErrorTransaction? = null
        var newTransactionSuccessReceived: ResponseCommonTransactionData? = null

        fun newTransactionFailure(error: ErrorTransaction) {
            newTransactionFailureReceived = error
        }

        fun newTransactionSuccess(response: ResponseCommonTransactionData) {
            newTransactionSuccessReceived = response
        }

        paymentRepository.newTransaction(
            orderDataRequest,
            ::newTransactionSuccess,
            ::newTransactionFailure)

        return Triple(orderDataRequest,
            newTransactionSuccessReceived,
            newTransactionFailureReceived)

    }

    fun callCheckStatusDeposit(
        depositId: Int = -1,
    ): Triple<Int, CheckStatusDepositResponse?, ErrorTransaction?> {
        var checkStatusDepositFailureReceived: ErrorTransaction? = null
        var checkStatusDepositSuccessReceived: CheckStatusDepositResponse? = null

        fun checkStatusDepositFailure(error: ErrorTransaction) {
            checkStatusDepositFailureReceived = error
        }

        fun checkStatusDepositSuccess(response: CheckStatusDepositResponse) {
            checkStatusDepositSuccessReceived = response
        }

        paymentRepository.checkStatusDeposit(
            depositId,
            ::checkStatusDepositSuccess,
            ::checkStatusDepositFailure)

        return Triple(depositId,
            checkStatusDepositSuccessReceived,
            checkStatusDepositFailureReceived)
    }

    fun callCheckStatusTransaction(
        transactionId: Int = -1,
    ): Triple<Int, CheckStatusTransactionResponse?, ErrorTransaction?> {
        var checkStatusTransactionFailureReceived: ErrorTransaction? = null
        var checkStatusTransactionSuccessReceived: CheckStatusTransactionResponse? = null

        fun checkStatusTransactionFailure(error: ErrorTransaction) {
            checkStatusTransactionFailureReceived = error
        }

        fun checkStatusTransactionSuccess(response: CheckStatusTransactionResponse) {
            checkStatusTransactionSuccessReceived = response
        }

        paymentRepository.checkStatusTransaction(
            transactionId,
            ::checkStatusTransactionSuccess,
            ::checkStatusTransactionFailure)

        return Triple(transactionId,
            checkStatusTransactionSuccessReceived,
            checkStatusTransactionFailureReceived)
    }

    fun callTransferProof(
        transferProofDataRequest: InsertTransferProofDataRequest,
    ): Triple<InsertTransferProofDataRequest, InsertTransferProofDataResponse?, Throwable?> {
        var checkTransferProofFailureReceived: Throwable? = null
        var checkTransferProofSuccessReceived: InsertTransferProofDataResponse? = null

        fun checkStatusDepositFailure(error: Throwable) {
            checkTransferProofFailureReceived = error
        }

        fun checkStatusDepositSuccess(response: InsertTransferProofDataResponse) {
            checkTransferProofSuccessReceived = response
        }

        paymentRepository.transferProof(
            transferProofDataRequest,
            ::checkStatusDepositSuccess,
            ::checkStatusDepositFailure)

        return Triple(transferProofDataRequest,
            checkTransferProofSuccessReceived,
            checkTransferProofFailureReceived)
    }

    fun callCheckStatusOrder(
        dataRequest: CheckStatusOrderDataRequest,
    ): Triple<CheckStatusOrderDataRequest, CheckStatusOrderDataResponse?, ErrorTransaction?> {
        var checkStatusOrderFailureReceived: ErrorTransaction? = null
        var checkStatusOrderSuccessReceived: CheckStatusOrderDataResponse? = null

        fun checkStatusTransactionFailure(error: ErrorTransaction) {
            checkStatusOrderFailureReceived = error
        }

        fun checkStatusTransactionSuccess(response: CheckStatusOrderDataResponse) {
            checkStatusOrderSuccessReceived = response
        }

        paymentRepository.checkStatusOrder(
            dataRequest,
            ::checkStatusTransactionSuccess,
            ::checkStatusTransactionFailure)

        return Triple(dataRequest,
            checkStatusOrderSuccessReceived,
            checkStatusOrderFailureReceived)
    }
}