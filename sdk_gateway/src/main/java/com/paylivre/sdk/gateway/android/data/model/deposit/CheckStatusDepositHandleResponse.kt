package com.paylivre.sdk.gateway.android.data.model.deposit

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception

const val GENERIC_ERROR = "Houve um erro ao finalizar essa operação!"

fun checkInternStatusOk(status: String): Boolean {
    return status == "success"
}

fun getResponseJson(response: Response<ResponseBody>): CheckStatusDepositResponse {
//    val messageMock = "{\"status\":\"success\",\"status_code\":200,\"message\":\"OK\",\"data\":{\"transaction_status_id\":null,\"deposit_status_id\":2}}"
    val message = response.body()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_deposit_status", message)
    return Gson().fromJson(message, CheckStatusDepositResponse::class.java)
}

fun getErrorResponseJson(response: Response<ResponseBody>): ErrorTransaction {
    val message = response.errorBody()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_error_deposit_status", message)
    return Gson().fromJson(message, ErrorTransaction::class.java)
}


fun handleResponseDepositCheckStatus(
    response: Response<ResponseBody>,
    onResponse: (CheckStatusDepositResponse?, ErrorTransaction?) -> Unit,
    depositId: Int
) {
    try {
        if (response.isSuccessful) {
            val res: CheckStatusDepositResponse = getResponseJson(response)

            if (!checkInternStatusOk(res.status)) {
                onResponse(null, getErrorResponseJson(response))

                //Sentry error logs
                Sentry.setExtra("request_url_status_deposit", "(/api/v2/transaction/deposit/status/$depositId)")
                Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/deposit/status/{id})")
            } else {
                onResponse(res, null)
            }

        } else {
            onResponse(null, getErrorResponseJson(response))

            //Sentry error logs
            Sentry.setExtra("request_url_status_deposit", "(/api/v2/transaction/deposit/status/$depositId)")
            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/deposit/status/{id})")
        }

    } catch (err: Exception) {
        onResponse(null, ErrorTransaction(GENERIC_ERROR, null, null))

        //Sentry log errors
        Sentry.setExtra("error_catch_status_deposit", err.message.toString())
        Sentry.setExtra("request_url_status_deposit", "(/api/v2/transaction/deposit/status/$depositId)")
        Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/deposit/status/{id})")
    }
}
