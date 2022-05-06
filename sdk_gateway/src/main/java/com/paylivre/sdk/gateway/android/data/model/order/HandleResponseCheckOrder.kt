package com.paylivre.sdk.gateway.android.data.model.order

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception


data class CheckStatusOrderDataRequest(
    val order_id: Int,
    val token: String,
)

data class CheckStatusOrderDataResponse(
    var error: ErrorTransaction? = null,
    val operation: Int? = null,
    val operation_name: String? = null,
    val full_name: String? = null,
    val document_number: String? = null,
    val final_amount: Int? = null,
    val original_amount: Int? = null,
    val original_currency: String? = null,
    val currency: String? = null,
    val converted_amount: Int? = null,
    val kyc_limits: LimitsKyc? = null,
    val order: Order? = null,
    val withdrawal_type_id: Int? = null,
    val withdrawal: Withdraw? = null,
)

data class CheckStatusOrderResponse(
    val isLoading: Boolean? = null,
    val isSuccess: Boolean? = null,
    val data: CheckStatusOrderDataResponse? = null,
    val error: ErrorTransaction? = null,
)


data class Withdraw(
    val id: Int?,
    val status_id: Int?,
    val status_name: String?,
)

data class Order(
    val id: Int?,
    val type: Int?,
    val type_name: String?,
    val status: String?,
    val status_id: Int?,
    val merchant_approval_status_id: Int?,
    val merchant_approval_status_name: String?,
)

fun getResponseJsonCheckStatusOrder(response: Response<ResponseBody>): CheckStatusOrderDataResponse {
    val message = response.body()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_gateway", message)
    return Gson().fromJson(message, CheckStatusOrderDataResponse::class.java)
}


fun handleResponseCheckStatusOrder(
    dataRequest: CheckStatusOrderDataRequest,
    response: Response<ResponseBody>,
    onResponse: (CheckStatusOrderDataResponse?, ErrorTransaction?) -> Unit,
) {
    try {
        if (response.isSuccessful) {
            val res: CheckStatusOrderDataResponse = getResponseJsonCheckStatusOrder(response)
            onResponse(res, null)


            //Set Log Analytics
            LogEvents.setLogEventAnalyticsWithParams(
                "SuccessCheckStatusOrder",
                Pair("order_id", dataRequest.order_id.toString()),
                Pair("order_type", res.order?.type.toString()),
                Pair("order_type_name", res.order?.type_name.toString()),
                Pair("order_status_id", res.order?.status_id.toString()),
                Pair("order_status", res.order?.status.toString()),
                Pair("merchant_approval_status_id",
                    res.order?.merchant_approval_status_id.toString()),
                Pair("merchant_approval_status_name",
                    res.order?.merchant_approval_status_name.toString()),
            )
        } else {
            onResponse(null, getErrorDataByCode(response))

            //Set Log Analytics
            LogEvents.setLogEventAnalyticsWithParams(
                "ErrorTransaction",
                Pair("order_id", dataRequest.order_id.toString()),
            )

            //Sentry config
            Sentry.setExtra("request_body_json_gateway", dataRequest.toString())

            Sentry.configureScope { scope ->
                scope.setTag("status_code_gateway", response.code().toString())
                dataRequest?.let {
                    scope.setContexts("request_body_gateway", it)
                }
            }

            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway/status)")
        }
    } catch (err: Exception) {
        onResponse(null, getGenericErrorData())

        Sentry.setExtra("error_catch_gateway", err.message.toString())
        Sentry.setExtra("error_generic_gateway", Gson().toJson(getGenericErrorData()))
        Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway)")
    }
}