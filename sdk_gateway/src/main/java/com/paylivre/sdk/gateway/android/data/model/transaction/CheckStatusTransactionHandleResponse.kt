package com.paylivre.sdk.gateway.android.data.model.transaction

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception

fun checkInternStatusOk(status: String): Boolean {
    return status == "success"
}

fun getResponseJson(response: Response<ResponseBody>): CheckStatusTransactionResponse {
//    val messageMock = "{\"status\":\"success\",\"status_code\":200,\"message\":\"OK\",\"data\":{\"transaction_status_id\":null,\"deposit_status_id\":2}}"
    val message = response.body()?.string()

    //Log Error Sentry
    addSentryBreadcrumb("original_response_status_transaction", message)
    return Gson().fromJson(message, CheckStatusTransactionResponse::class.java)
}

fun getErrorResponseJson(response: Response<ResponseBody>): ErrorTransaction {
    val message = response.errorBody()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_error_status_transaction", message)
    return Gson().fromJson(message, ErrorTransaction::class.java)
}


fun handleResponseTransactionCheckStatus(
    response: Response<ResponseBody>,
    onResponse: (CheckStatusTransactionResponse?, ErrorTransaction?) -> Unit,
    transactionId: Int,
) = try {
    if (response.isSuccessful) {
        val res: CheckStatusTransactionResponse = getResponseJson(response)

        if (!checkInternStatusOk(res.status)) {
            onResponse(null, getErrorResponseJson(response))
            //Sentry log errors
            Sentry.setExtra("request_url_status_transaction", "(/api/v2/transaction/status/$transactionId)")
            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/status/{id})")
        } else {
            onResponse(res, null)
        }

    } else {
        onResponse(null, getErrorResponseJson(response))
        //Sentry log errors
        Sentry.setExtra("request_url_status_transaction", "(/api/v2/transaction/status/$transactionId)")
        Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/status/{id})")
    }

} catch (err: Exception) {
    onResponse(null, getGenericErrorData())
    //Sentry log errors
    Sentry.setExtra("error_catch_status_transaction", err.message.toString())
    Sentry.setExtra("request_url_status_transaction", "(/api/v2/transaction/status/$transactionId)")
    Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/transaction/status/{id})")
}
