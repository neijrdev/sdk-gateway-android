package com.paylivre.sdk.gateway.android.data.model.transferProof

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException

fun getResponseJson(response: Response<ResponseBody>): InsertTransferProofDataResponse {
//    val messageMock = "{\"status\":\"success\",\"status_code\":200,\"message\":\"OK\",\"data\":{\"transaction_status_id\":null,\"deposit_status_id\":2}}"
    val message = response.body()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_transfer_proof", message)
    return Gson().fromJson(message, InsertTransferProofDataResponse::class.java)
}

fun insertTransferProofHandleResponse(
    response: Response<ResponseBody>,
    onResponse: (InsertTransferProofDataResponse?, Throwable?) -> Unit,
    dataRequest: InsertTransferProofDataRequest,
) {
    try {
        if (response.isSuccessful) {
            val res: InsertTransferProofDataResponse =
                getResponseJson(response)
            onResponse(res, null)

        } else {
            onResponse(null, RuntimeException(getGenericErrorData().message))
            //Sentry config
            Sentry.setExtra("request_body_json", dataRequest.toString())
            //Log Error Sentry
            addSentryBreadcrumb("original_response_error_transfer_proof",
                response.errorBody().toString())
            Sentry.configureScope { scope ->
                scope.setTag("status_code", response.code().toString())
                scope.setTag("error_message", getGenericErrorData().message.toString())
                scope.setContexts("request_body", dataRequest)
            }
            Sentry.setExtra("request_url_transfer_proof", "/api/v2/gateway/${dataRequest.order_id}/transfer-proof")
            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (/api/v2/gateway/{order_id}/transfer-proof)")

        }

    } catch (err: Exception) {
        onResponse(
            null,
            RuntimeException(getGenericErrorData().message)
        )
        Sentry.setExtra("error_catch_transfer_proof", err.message.toString())
        Sentry.setExtra("error_generic_transfer_proof", Gson().toJson(getGenericErrorData()))
        Sentry.setExtra("request_url_transfer_proof", "/api/v2/gateway/${dataRequest.order_id}/transfer-proof")
        Sentry.captureMessage("ERROR_API  | status_code: ${response.code()} (/api/v2/gateway/{order_id}/transfer-proof)")
    }
}