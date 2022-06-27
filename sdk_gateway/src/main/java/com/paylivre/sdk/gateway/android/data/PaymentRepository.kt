package com.paylivre.sdk.gateway.android.data

import com.paylivre.sdk.gateway.android.data.api.RemoteDataSource
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import java.lang.Exception

class PaymentRepository(private val remoteDataSource: RemoteDataSource) {

    fun getPixApprovalTime(
        getApprovalTimeSuccess: (response: PixApprovalTimeResponse) -> Unit,
        getApprovalTimeFailure: (error: Throwable) -> Unit,
    ) {
        try {
            remoteDataSource.getPixApprovalTime { response, error ->
                if (response !== null) {
                    getApprovalTimeSuccess(response)
                } else if (error !== null) {
                    getApprovalTimeFailure(error)
                }

            }
        } catch (error: Exception) {
            getApprovalTimeFailure(error)
        }
    }

    fun getServiceStatus(
        getServiceStatusSuccess: (response: ServiceStatusResponseAdapter) -> Unit,
        getServiceStatusFailure: (error: Throwable) -> Unit,
    ) {
        try {
            remoteDataSource.getServicesStatus { response, error ->
                if (response !== null) {
                    getServiceStatusSuccess(response)
                } else if (error !== null) {
                    getServiceStatusFailure(error)
                }

            }
        } catch (error: Exception) {
            getServiceStatusFailure(error)
        }
    }


    fun newTransaction(
        orderDataRequest: OrderDataRequest,
        requestSuccess: (response: ResponseCommonTransactionData) -> Unit,
        requestFailure: (error: ErrorTransaction) -> Unit,
    ) {

        try {
            remoteDataSource.newTransaction(
                orderDataRequest
            ) { response, error ->
                if (response !== null) {
                    requestSuccess(response)
                } else if (error !== null) {
                    requestFailure(error)
                }
            }
        } catch (error: Exception) {
            requestFailure(ErrorTransaction(error.message, null, null))
        }
    }


    fun checkStatusDeposit(
        depositId: Int,
        checkStatusDepositSuccess: (response: CheckStatusDepositResponse) -> Unit,
        checkStatusDepositFailure: (error: ErrorTransaction) -> Unit,
    ) {
        try {
            remoteDataSource.checkStatusDeposit(depositId) { response, error ->
                if (response !== null) {
                    checkStatusDepositSuccess(response)
                } else if (error !== null) {
                    checkStatusDepositFailure(error)
                }

            }
        } catch (error: Exception) {
            checkStatusDepositFailure(ErrorTransaction(error.message, null, null))
        }
    }

    fun checkStatusTransaction(
        transactionId: Int,
        checkStatusTransactionSuccess: (response: CheckStatusTransactionResponse) -> Unit,
        checkStatusTransactionFailure: (error: ErrorTransaction) -> Unit,
    ) {
        try {
            remoteDataSource.checkStatusTransaction(transactionId) { response, error ->
                if (response !== null) {
                    checkStatusTransactionSuccess(response)
                } else if (error !== null) {
                    checkStatusTransactionFailure(error)
                }

            }
        } catch (error: Exception) {
            checkStatusTransactionFailure(ErrorTransaction(error.message, null, null))
        }
    }


    fun transferProof(
        transferProofDataRequest: InsertTransferProofDataRequest,
        requestSuccess: (response: InsertTransferProofDataResponse) -> Unit,
        requestFailure: (error: Throwable) -> Unit,
    ) {

        try {
            remoteDataSource.transferProof(
                transferProofDataRequest
            ) { response, error ->
                if (response !== null) {
                    requestSuccess(response)
                } else if (error !== null) {
                    requestFailure(error)
                }
            }
        } catch (error: Exception) {
            requestFailure(RuntimeException(error.message))
        }
    }

    fun checkStatusOrder(
        dataRequest: CheckStatusOrderDataRequest,
        requestSuccess: (response: CheckStatusOrderDataResponse) -> Unit,
        requestFailure: (error: ErrorTransaction) -> Unit,
    ) {
        try {
            remoteDataSource.checkStatusOrder(
                dataRequest
            ) { response, error ->
                if (response !== null) {
                    requestSuccess(response)
                } else if (error !== null) {
                    requestFailure(error)
                }
            }
        } catch (error: Exception) {
            ErrorTransaction("error_request_generic", null, null)
        }
    }

}