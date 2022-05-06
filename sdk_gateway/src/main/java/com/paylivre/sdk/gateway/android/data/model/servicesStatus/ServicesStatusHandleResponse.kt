package com.paylivre.sdk.gateway.android.data.model.servicesStatus

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException

fun checkInternStatusOk(status: String): Boolean {
    return status == "success"
}

fun getResponseJson(response: Response<ResponseBody>): ServiceStatusResponse {
//    val messageMock = "{\"status\":\"success\",\"status_code\":200,\"message\":\"OK\",\"data\":[{\"wire_transfer\":{\"status\":1,\"id\":1}},{\"billet\":{\"status\":1,\"id\":2}},{\"cryptocurrency\":{\"status\":0,\"id\":3}},{\"debit\":{\"status\":0,\"id\":4}},{\"pix\":{\"status\":0,\"id\":5}}]}"
    val message = response.body()?.string()

    //Log Error Sentry
    addSentryBreadcrumb("original_response_deposit_status", message)

    return Gson().fromJson(message, ServiceStatusResponse::class.java)
}

data class ServicesStatus(
    val statusWallet: Boolean? = null,
    val statusWiretransfer: Boolean? = null,
    val statusBillet: Boolean? = null,
    val statusPix: Boolean? = null,
)

data class ServiceStatusResponseAdapter(
    val status: String,
    val status_code: Int,
    val message: String,
    val data: ServicesStatus,
)

enum class ServicesTypesApi(val id: Int) {
    WIRETRANSFER(1),
    BILLET(2),
    DEBIT(2),
    PIX(5),
}

fun dataAdapter(data: List<DataServiceStatus>): ServicesStatus {
    var statusPix: Boolean? = null
    var statusBillet: Boolean? = null
    var statusWallet: Boolean? = true
    var statusWireTransfer: Boolean? = null

    data.map {
        val id = it.service.id
        val statusBoolean = it.service.status == 1
        when (id) {
            ServicesTypesApi.WIRETRANSFER.id -> statusWireTransfer = statusBoolean
            ServicesTypesApi.PIX.id -> statusPix = statusBoolean
            ServicesTypesApi.BILLET.id -> statusBillet = statusBoolean
        }
    }

    return ServicesStatus(statusWallet, statusWireTransfer, statusBillet, statusPix)
}


fun handleResponseServiceStatus(
    response: Response<ResponseBody>,
    onResponse: (ServiceStatusResponseAdapter?, Throwable?) -> Unit,
) {
    try {
        if (response.isSuccessful) {
            val res: ServiceStatusResponse = getResponseJson(response)
            val data = dataAdapter(res.data)
            val resAdapter = ServiceStatusResponseAdapter(
                res.status,
                res.status_code,
                res.message,
                data
            )

            if (!checkInternStatusOk(res.status)) {
                val message = getGenericErrorData().message
                onResponse(null, RuntimeException(message))

                //Log error Sentry
                Sentry.setExtra("request_error_deposit_status", response.errorBody().toString())
                Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway/deposit-status)")

            } else {
                onResponse(resAdapter, null)
            }

        } else {
            val message = getGenericErrorData().message
            onResponse(null, RuntimeException(message))

            //Log error Sentry
            Sentry.setExtra("request_error_deposit_status", response.errorBody().toString())
            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway/deposit-status)")

        }

    } catch (err: Exception) {
        onResponse(null, RuntimeException(getGenericErrorData().message))
        //Sentry log errors
        Sentry.setExtra("error_catch_deposit_status", err.message.toString())
        Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway/deposit-status)")
    }
}
