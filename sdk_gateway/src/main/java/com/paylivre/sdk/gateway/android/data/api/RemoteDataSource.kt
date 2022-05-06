package com.paylivre.sdk.gateway.android.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.handleResponseDepositCheckStatus
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.handleResponsePixApprovalTime
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.handleResponseServiceStatus
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.insertTransferProofHandleResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.handleResponseTransactionCheckStatus
import io.sentry.Sentry
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import okhttp3.MultipartBody


class RemoteDataSource(private val apiService: ApiService) {
    fun getPixApprovalTime(
        onResponse: (PixApprovalTimeResponse?, Throwable?) -> Unit,
    ) {

        apiService.getPixApprovalTime().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {

                //Trata os possíveis erros do response
                handleResponsePixApprovalTime(response, onResponse)

            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(null, RuntimeException("error_request_not_connect_server"))

                //Log error Sentry
                addSentryBreadcrumb("error_throwable_average_pix_approval_time",
                    t.message.toString())

                Sentry.setExtra("error_throwable", t.message.toString())
                Sentry.setExtra("error", "error_request_not_connect_server")
                Sentry.captureMessage("ERROR_API (api/v2/gateway/averagePixApprovalTime)")

            }

        })

    }

    fun getServicesStatus(
        onResponse: (ServiceStatusResponseAdapter?, Throwable?) -> Unit,
    ) {

        apiService.getServicesStatus().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {

                //Trata os possíveis erros do response
                handleResponseServiceStatus(response, onResponse)

            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    RuntimeException("error_request_not_connect_server")
                )
                //Log error Sentry
                Sentry.setExtra("error_throwable_gateway", t.message.toString())
                Sentry.setExtra("error_gateway", "error_request_not_connect_server")
                Sentry.captureMessage("ERROR_API (api/v2/gateway/deposit-status)")
            }

        })

    }


    fun newTransaction(
        dataRequest: OrderDataRequest,
        onResponse: (ResponseCommonTransactionData?, ErrorTransaction?) -> Unit,
    ) {

        val gson: Gson = GsonBuilder()
            .disableHtmlEscaping()
            .create()

        val body: RequestBody = gson.toJson(dataRequest)
            .toRequestBody("application/json; charset=UTF-8".toMediaType())

        apiService.newTransactionGateway(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                //Trata os possíveis erros do response
                handleResponseTransaction(dataRequest, response, onResponse)
            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    ErrorTransaction("error_request_not_connect_server", null, null)
                )
                //Log error Sentry
                Sentry.setExtra("error_throwable_gateway", t.message.toString())
                Sentry.setExtra("error_gateway", "error_request_not_connect_server")
                Sentry.captureMessage("ERROR_API (api/v2/gateway)")
            }
        })
    }

    fun checkStatusDeposit(
        depositId: Int,
        onResponse: (CheckStatusDepositResponse?, ErrorTransaction?) -> Unit,
    ) {

        apiService.checkStatusDeposit(depositId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                //Trata os possíveis erros do response
                handleResponseDepositCheckStatus(response, onResponse, depositId)
            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    ErrorTransaction("error_request_not_connect_server", null, null)
                )
                //Log error Sentry
                Sentry.setExtra("error_throwable_status_deposit", t.message.toString())
                Sentry.setExtra("error_status_deposit", "error_request_not_connect_server")
                Sentry.setExtra("request_url_status_deposit",
                    "(/api/v2/transaction/deposit/status/$depositId)")
                Sentry.captureMessage("ERROR_API (api/v2/transaction/deposit/status/{id})")
            }

        })

    }

    fun checkStatusTransaction(
        transactionId: Int,
        onResponse: (CheckStatusTransactionResponse?, ErrorTransaction?) -> Unit,
    ) {

        apiService.checkStatusTransaction(transactionId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                //Trata os possíveis erros do response
                handleResponseTransactionCheckStatus(response, onResponse, transactionId)
            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    ErrorTransaction("error_request_not_connect_server", null, null)
                )

                //Log error Sentry
                Sentry.setExtra("error_throwable_status_transaction", t.message.toString())
                Sentry.setExtra("error_status_transaction", "error_request_not_connect_server")
                Sentry.setExtra("request_url_status_transaction",
                    "(/api/v2/transaction/status/$transactionId)")
                Sentry.captureMessage("ERROR_API (api/v2/transaction/status/{id})")
            }

        })

    }


    fun transferProof(
        dataRequest: InsertTransferProofDataRequest,
        onResponse: (InsertTransferProofDataResponse?, Throwable?) -> Unit,
    ) {

        val file = dataRequest.file

        val tokenPart: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            dataRequest.token
        )

        val proofFilePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "proof",
            file.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), file)
        )

        apiService.transferProof(
            dataRequest.order_id,
            tokenPart,
            proofFilePart
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                //Trata os possíveis erros do response
                insertTransferProofHandleResponse(response, onResponse, dataRequest)
            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    RuntimeException("error_request_not_connect_server")
                )

                //Log error Sentry
                Sentry.setExtra("error_throwable_transfer-proof", t.message.toString())
                Sentry.setExtra("error_transfer-proof", "error_request_not_connect_server")
                Sentry.setExtra("request_urltransfer-proof",
                    "/api/v2/gateway/${dataRequest.order_id}/transfer-proof")
                Sentry.captureMessage("ERROR_API (/api/v2/gateway/{order_id}/transfer-proof)")
            }

        })

    }

    fun checkStatusOrder(
        dataRequest: CheckStatusOrderDataRequest,
        onResponse: (CheckStatusOrderDataResponse?, ErrorTransaction?) -> Unit,
    ) {
        apiService.checkStatusOrder(
            dataRequest.order_id,
            dataRequest.token,
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                //Trata os possíveis erros do response
                handleResponseCheckStatusOrder(dataRequest, response, onResponse)
            }

            //Geralmente Erros de Falha de conexão
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse(
                    null,
                    ErrorTransaction("error_request_not_connect_server",
                        null, null)
                )

                //Log error Sentry
                Sentry.setExtra("error_check_status_order", t.message.toString())
                Sentry.setExtra("error_check_status_order", "error_request_not_connect_server")
                Sentry.setExtra("error_check_status_order",
                    "/api/v2/gateway/status/${dataRequest.order_id}/${dataRequest.token}")
                Sentry.captureMessage("ERROR_API (/api/v2/gateway/status/{order_id}/{token})")
            }

        })
    }
}