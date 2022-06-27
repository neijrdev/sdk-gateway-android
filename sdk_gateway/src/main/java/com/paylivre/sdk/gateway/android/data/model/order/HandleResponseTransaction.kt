package com.paylivre.sdk.gateway.android.data.model.order

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.ERROR_INVALID_USER_NAME_OR_PASSWORD
import io.sentry.Sentry
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception

data class StatusWithdrawOrder(
    val withdrawal_type_id: Int?,
    val withdrawal_status_id: Int?,
    val merchant_approval_status_id: Int?,
    val order_status_id: Int?,
)

data class StatusTransactionResponse(
    val isLoading: Boolean?,
    val isSuccess: Boolean?,
    val data: ResponseCommonTransactionData?,
    val error: ErrorTransaction?,
)

data class ResponseCommonTransactionData(
    val full_name: String?,
    val document_number: String?,
    val kyc_limits: LimitsKyc?,
    val original_amount: Int?,
    val origin_amount: Int?,
    val original_currency: String?,
    val final_amount: Int?,
    val converted_amount: Int?,
    val taxes: Int? = 0,
    val receivable_url: String? = "",
    val deposit_type_id: Int?,
    val redirect_url: String? = "",
    val billet_digitable_line: String? = "",
    val billet_due_date: String? = "",
    val deposit_id: Int?,
    val order_id: Int?,
    val transaction_id: Int?,
    val withdrawal: WithdrawalData?,
    val order: OrderData?,
    val bank_accounts: List<BankAccount>?,
    val verification_token: String?,
    val token: String? = null,
    val withdrawal_type_id: Int? = null,
)

data class WithdrawalData(
    val id: Int? = null,
    val status_id: Int? = null,
    val status_name: String? = null,
)

data class OrderData(
    val id: Int? = null,
    val status_id: Int? = null,
    val status: String? = "",
    val order_type_id: Int? = null,
    val merchant_approval_status_id: Int? = null,
    val merchant_approval_status_name: String? = "",
    val type: Int? = null,
    val type_name: String? = "",
)

data class BankAccounts(
    val bank_accounts: List<BankAccount>?,
)

data class BankAccount(
    val account_name: String?,
    val account_holder_full_name: String?,
    val account_holder_document: String?,
    val hidden: Int?, //if show option
    val office_number: Int?,
    val office_digit: Int?,
    val account_number: String?,
    val account_digit: String?,
    val bank: Bank?,
    val bank_number: Int?,
)

data class Bank(
    val id: Int?,
    val country_cca3: String?,
    val name: String?,
    val number: String?,
    val website: String?,
    val display: Int?,
    val blacklisted: Int?,
)

const val GENERIC_ERROR = "invalid_data_error"

fun getResponseJson(response: Response<ResponseBody>): ResponseCommonTransactionData {
    val message = response.body()?.string()

    //Log Error Sentry
    addSentryBreadcrumb("original_response_gateway", message)
    return Gson().fromJson(message, ResponseCommonTransactionData::class.java)
}

data class Errors(
    val email: List<String>? = null,
    val amount: List<String>? = null,
    val currency: List<String>? = null,
    val document_number: List<String>? = null,
    val callback_url: List<String>? = null,
    val operation: List<String>? = null,
    val merchant_transaction_id: List<String>? = null,
    val auto_approve: List<String>? = null,
    //kyc block reasons
    val blocked_by_kyc: String? = null,
    val id: Int? = null,
    val public_reason_pt: String? = null,
    val public_reason_en: String? = null,
    val public_reason_es: String? = null,
    val internal_reason_en: String? = null,
    val internal_reason_es: String? = null,
    val internal_reason_pt: String? = null,
    val deleted_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val created_by: Int? = null,
    val is_system_reason: Boolean? = null,
    val link: String? = null,
    val type: String? = null,
    //kyc limit exceeded
    val limit_exceeded: String? = null,
    val available_limit: Int? = null,
    val kyc_level: String? = null,
    val kyc_level_name: String? = null,
    val limit: Int? = null,
    val used_limit: Int? = null,
    val free_transaction_limit: Int? = null,
)

data class ErrorTransaction(
    val message: String? = null,
    val messageDetails: String? = null,
    val error: ErrorData? = null,
    val errors: Errors? = null,
    var errorTags: String? = null,
    val original_message: String? = null,
)

data class ErrorTransactionTwo(
    val message: String? = null,
)

data class ErrorData(
    val type: String?,
)

fun checkIsErrorApiToken(error: ErrorTransaction?): Boolean {
    return if (error != null) {
        error.original_message == ERROR_INVALID_USER_NAME_OR_PASSWORD
    } else false
}

fun getErrorTags422(error: ErrorTransaction? = null): String {
    if (error?.errors != null) {
        var errorTags: MutableList<String> = mutableListOf()

        error.errors?.email?.let { errorTags.add(ErrorTags.RT001.toString()) }
        error.errors?.amount?.let { errorTags.add(ErrorTags.RT002.toString()) }
        error.errors?.currency?.let { errorTags.add(ErrorTags.RT003.toString()) }
        error.errors?.document_number?.let { errorTags.add(ErrorTags.RT004.toString()) }
        error.errors?.callback_url?.let { errorTags.add(ErrorTags.RT005.toString()) }
        error.errors?.operation?.let { errorTags.add(ErrorTags.RT006.toString()) }
        error.errors?.merchant_transaction_id?.let { errorTags.add(ErrorTags.RT007.toString()) }
        error.errors?.auto_approve?.let { errorTags.add(ErrorTags.RT008.toString()) }

        return errorTags.joinToString { it }
    } else {
        return ""
    }
}


fun getErrorResponseJson(response: Response<ResponseBody>): ErrorTransaction {
//    val messageMockInvalidPass = "{\"message\":\"Invalid username or password\",\"error\":{\"type\":\"App\\\\Exceptions\\\\Orders\\\\OrdersException\"}}"

    val message = response.errorBody()?.string()
    //Log Error Sentry
    addSentryBreadcrumb("original_response_error_gateway", message)
    return try {
        Gson().fromJson(message, ErrorTransaction::class.java)
    } catch (e: Exception) {
        val errorTransaction = Gson().fromJson(message, ErrorTransactionTwo::class.java)
        return ErrorTransaction(message = errorTransaction.message)
    } catch (e: Exception) {
        return getGenericErrorData()
    }
}


fun getErrorCode422WithTagResponseJson(response: Response<ResponseBody>): ErrorTransaction {
    val commonErrorTransaction = getErrorResponseJson(response)
    val errorTagsString = getErrorTags422(commonErrorTransaction)

    return ErrorTransaction(
        message = GENERIC_ERROR,
        null,
        commonErrorTransaction.error,
        commonErrorTransaction.errors,
        errorTagsString,
        original_message = commonErrorTransaction.message
    )
}

fun getErrorCodeWithTagResponseJson(response: Response<ResponseBody>): ErrorTransaction {
    val commonErrorTransaction = getErrorResponseJson(response)

    var errorTagsString = commonErrorTransaction.message?.let { getErrorTagResponseError(it) }

    return ErrorTransaction(
        message = GENERIC_ERROR,
        null,
        commonErrorTransaction.error,
        commonErrorTransaction.errors,
        errorTagsString,
        original_message = commonErrorTransaction.message
    )
}


fun getErrorCode400ResponseJson(response: Response<ResponseBody>): ErrorTransaction {
    val commonErrorTransaction = getErrorResponseJson(response)

    var messagesError = commonErrorTransaction.message?.let { getStringKeyResponseError(it) }

    return ErrorTransaction(
        message = messagesError?.keyMessage,
        messageDetails = messagesError?.keyMessageDetails,
        commonErrorTransaction.error,
        commonErrorTransaction.errors,
        null,
        original_message = commonErrorTransaction.message
    )
}

fun getErrorCode401ResponseJson(): ErrorTransaction {
    val errorTag = getErrorTagResponseError("401")

    return ErrorTransaction(
        message = GENERIC_ERROR,
        messageDetails = null,
        null,
        null,
        errorTag,
        null
    )
}

fun getErrorCommon(response: Response<ResponseBody>, errorStag: String? = ""): ErrorTransaction {
    val commonErrorTransaction = getErrorResponseJson(response)
    return ErrorTransaction(
        message = "title_unexpected_error",
        messageDetails = "title_unexpected_error_body",
        commonErrorTransaction.error,
        commonErrorTransaction.errors,
        errorStag,
        original_message = commonErrorTransaction.message
    )
}


fun getErrorDataByCode(response: Response<ResponseBody>): ErrorTransaction {
    val error = when (response.code()) {
        400 -> getErrorCode400ResponseJson(response)
        401 -> getErrorCode401ResponseJson()
        403 -> getErrorCodeWithTagResponseJson(response)
        404 -> getErrorCodeWithTagResponseJson(response)
        422 -> getErrorCode422WithTagResponseJson(response)
        500 -> getErrorCommon(response, ErrorTags.UX005.toString())
        else -> getErrorCommon(response, ErrorTags.UX000.toString())
    }
    Sentry.setExtra("response_error_data", error.toString())
    return error
}


fun filterPasswordFromRequest(dataRequest: OrderDataRequest): JsonObject? {
    val gson = Gson()
    val dataRequestWithoutPass = gson.toJsonTree(dataRequest).asJsonObject
    dataRequestWithoutPass.remove("password")
    dataRequestWithoutPass.addProperty("p@ssword", "field filtered")

    return dataRequestWithoutPass
}


fun handleResponseTransaction(
    dataRequest: OrderDataRequest,
    response: Response<ResponseBody>,
    onResponse: (ResponseCommonTransactionData?, ErrorTransaction?) -> Unit,
    logEventsService: LogEventsService,
) {
    try {
        if (response.isSuccessful) {
            val res: ResponseCommonTransactionData = getResponseJson(response)
            onResponse(res, null)

            //Set Log Analytics
            logEventsService.setLogEventAnalyticsWithParams(
                "SuccessTransaction",
                Pair("operation", dataRequest.operation),
                Pair("type", dataRequest.type),
                Pair("selected_type", dataRequest.selected_type),
            )
        } else {
            onResponse(null, getErrorDataByCode(response))

            //Set Log Analytics
            logEventsService.setLogEventAnalyticsWithParams(
                "ErrorTransaction",
                Pair("operation", dataRequest.operation),
                Pair("type", dataRequest.type),
                Pair("selected_type", dataRequest.selected_type),
            )

            //Sentry config
            Sentry.setExtra("request_body_json_gateway",
                filterPasswordFromRequest(dataRequest).toString())

            Sentry.configureScope { scope ->
                scope.setTag("status_code_gateway", response.code().toString())
                filterPasswordFromRequest(dataRequest)?.let {
                    scope.setContexts("request_body_gateway", it)
                }
            }

            Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway)")
        }
    } catch (err: Exception) {
        onResponse(null, getGenericErrorData())

        Sentry.setExtra("error_catch_gateway", err.message.toString())
        Sentry.setExtra("error_generic_gateway", Gson().toJson(getGenericErrorData()))
        Sentry.captureMessage("ERROR_API | status_code: ${response.code()} (api/v2/gateway)")
    }
}

//fun valueOfTypesToSelect(code: Int): Type? = Type.values().find { it.code == code }

fun getSelectedTypeByType(type: Int): String {
    return when (type) {
        TypesToSelect.PIX.code -> {
            Type.PIX.code.toString()
        }
        TypesToSelect.WALLET.code -> {
            Type.WALLET.code.toString()
        }
        TypesToSelect.WIRETRANSFER.code -> {
            Type.WIRETRANSFER.code.toString()
        }
        TypesToSelect.BILLET.code -> {
            Type.BILLET.code.toString()
        }
        else -> {
            ""
        }
    }
}

fun getDataWithOnlySelectedType(data: DataGenerateSignature): DataGenerateSignature {
    val selectedType = getSelectedTypeByType(data.type.toInt())
    return if (selectedType.isNotEmpty()) {
        data.selected_type = selectedType
        data
    } else {
        data
    }
}

fun getDataAutoRequestUrlWithSelectedType(data: DataStartCheckout): OrderDataRequest {
    val selectedType = getSelectedTypeByType(data.type)

    return OrderDataRequest(
        base_url = data.base_url,
        merchant_id = data.merchant_id,
        merchant_transaction_id = data.merchant_transaction_id,
        amount = data.amount.toString(),
        currency = data.currency,
        operation = data.operation.toString(),
        callback_url = data.callback_url,
        type = data.type.toString(),
        selected_type = selectedType,
        account_id = data.account_id,
        email = data.email_address.toString(),
        document_number = data.document.toString(),
        login_email = "",
        api_token = "",
        pix_key_type = data.pix_key_type.toString(),
        pix_key = data.pix_key,
        signature = data.signature.toString(),
        url = getUrlWithoutSignature(data.url.toString()),
        auto_approve = data.auto_approve.toString(),
        redirect_url = data.redirect_url,
        logo_url = data.logo_url
    )
}