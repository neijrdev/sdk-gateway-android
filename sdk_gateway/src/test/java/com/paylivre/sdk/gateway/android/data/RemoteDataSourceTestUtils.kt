package com.paylivre.sdk.gateway.android.data

import com.paylivre.sdk.gateway.android.data.api.ApiService
import com.paylivre.sdk.gateway.android.data.api.RemoteDataSource
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.services.log.*

class RemoteDataSourceTestUtils(
    private val mockApiService: ApiService = ApiServiceMock(),
    private val logEventsServiceImpl: LogEventsService = LogEventsServiceImplTest(),
    private val logErrorService: LogErrorService = LogErrorServiceImplTest(),
) {
    fun callGetPixApprovalTime(): Pair<PixApprovalTimeResponse?, Throwable?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: PixApprovalTimeResponse? = null
        var getPixApprovalTimeFailureReceived: Throwable? = null

        fun onResponse(response: PixApprovalTimeResponse?, error: Throwable?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.getPixApprovalTime(::onResponse)

        return Pair(getPixApprovalTimeSuccessReceived, getPixApprovalTimeFailureReceived)
    }

    fun callGetServicesStatus(): Pair<ServiceStatusResponseAdapter?, Throwable?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: ServiceStatusResponseAdapter? = null
        var getPixApprovalTimeFailureReceived: Throwable? = null

        fun onResponse(response: ServiceStatusResponseAdapter?, error: Throwable?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.getServicesStatus(::onResponse)

        return Pair(getPixApprovalTimeSuccessReceived, getPixApprovalTimeFailureReceived)
    }

    fun callNewTransaction(dataRequest: OrderDataRequest): Triple<OrderDataRequest, ResponseCommonTransactionData?, ErrorTransaction?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: ResponseCommonTransactionData? = null
        var getPixApprovalTimeFailureReceived: ErrorTransaction? = null

        fun onResponse(response: ResponseCommonTransactionData?, error: ErrorTransaction?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.newTransaction(dataRequest, ::onResponse)

        return Triple(dataRequest,
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived)
    }

    fun callCheckStatusDeposit(depositId: Int): Triple<Int, CheckStatusDepositResponse?, ErrorTransaction?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: CheckStatusDepositResponse? = null
        var getPixApprovalTimeFailureReceived: ErrorTransaction? = null

        fun onResponse(response: CheckStatusDepositResponse?, error: ErrorTransaction?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.checkStatusDeposit(depositId, ::onResponse)

        return Triple(depositId,
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived)
    }

    fun callCheckStatusTransaction(transactionId: Int): Triple<Int, CheckStatusTransactionResponse?, ErrorTransaction?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: CheckStatusTransactionResponse? = null
        var getPixApprovalTimeFailureReceived: ErrorTransaction? = null

        fun onResponse(response: CheckStatusTransactionResponse?, error: ErrorTransaction?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.checkStatusTransaction(transactionId, ::onResponse)

        return Triple(transactionId,
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived)
    }

    fun callTransferProof(dataRequest: InsertTransferProofDataRequest)
            : Triple<InsertTransferProofDataRequest, InsertTransferProofDataResponse?, Throwable?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: InsertTransferProofDataResponse? = null
        var getPixApprovalTimeFailureReceived: Throwable? = null

        fun onResponse(response: InsertTransferProofDataResponse?, error: Throwable?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.transferProof(dataRequest, ::onResponse)

        return Triple(dataRequest,
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived)
    }

    fun callCheckStatusOrder(dataRequest: CheckStatusOrderDataRequest)
            : Triple<CheckStatusOrderDataRequest, CheckStatusOrderDataResponse?, ErrorTransaction?> {
        val remoteDataSource = RemoteDataSource(
            apiService = mockApiService,
            logEventsService = logEventsServiceImpl,
            logErrorService = logErrorService
        )

        var getPixApprovalTimeSuccessReceived: CheckStatusOrderDataResponse? = null
        var getPixApprovalTimeFailureReceived: ErrorTransaction? = null

        fun onResponse(response: CheckStatusOrderDataResponse?, error: ErrorTransaction?) {
            getPixApprovalTimeSuccessReceived = response
            getPixApprovalTimeFailureReceived = error
        }

        remoteDataSource.checkStatusOrder(dataRequest, ::onResponse)

        return Triple(dataRequest,
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived)
    }


    fun getListOfCapturedMessages(vararg messages: String): MutableList<String> {
        val listOfCapturedMessages = mutableListOf<String>()
        for (message in messages) {
            listOfCapturedMessages.add(message)
        }
        return listOfCapturedMessages
    }

    fun getListOfCapturedExtras(vararg extras: Pair<String, String>): MutableList<Pair<String, String>> {
        val listOfCapturedMessages = mutableListOf<Pair<String, String>>()
        for (extra in extras) {
            listOfCapturedMessages.add(Pair(extra.first, extra.second))
        }
        return listOfCapturedMessages
    }

    fun getListOfAddedSentryBreadcrumb(vararg sentryBreadcrumbs: Pair<String, String>)
            : MutableList<Pair<String, String>> {
        val listOfCapturedMessages = mutableListOf<Pair<String, String>>()
        for (sentryBreadcrumb in sentryBreadcrumbs) {
            listOfCapturedMessages.add(Pair(sentryBreadcrumb.first, sentryBreadcrumb.second))
        }
        return listOfCapturedMessages
    }

}