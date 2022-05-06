package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.utils.*

data class ResponseValidData(
    val isValid: Boolean,
    val messageMainError: String?,
    val errorTags: String?,
    val messageDetailsError: String?,
)

data class DataTransaction(
    val operation: Int,
    val currency: String,
    val type: Int,
    val amount: Int,
    val merchant_transaction_id: String,
    val auto_approve: Int,
)

data class ResponseValidateDataParams(
    val isValid: Boolean,
    val errorTags: MutableList<String>? = mutableListOf(),
)

enum class Languages {
    PT,
    EN,
    ES,
}

fun getResponseValidateDataParams(
    isValid: Boolean,
    sErrorTag: String? = null,
    errorTags: MutableList<String>? = mutableListOf(),
): ResponseValidateDataParams {
    return if (isValid) {
        ResponseValidateDataParams(true)
    } else {
        if (sErrorTag != null) {
            var errorTagsCustom: MutableList<String> = mutableListOf()
            if (sErrorTag != null) {
                errorTagsCustom.add(sErrorTag)
            }
            ResponseValidateDataParams(false, errorTagsCustom)
        } else {
            ResponseValidateDataParams(false, errorTags)
        }

    }
}

fun validateLocaleLanguage(localeName: String?): Boolean {
    return if (localeName.isNullOrEmpty()) {
        false
    } else {
        when (localeName.subSequence(0, 2).toString().lowercase()) {
            Languages.EN.toString().lowercase(),
            Languages.PT.toString().lowercase(),
            Languages.ES.toString().lowercase(),
            -> true
            else -> false
        }
    }

}

fun validateDocument(document: String?): Boolean {
    return if (!document.isNullOrEmpty()) {
        val documentSizeDigits = document.filter { it.isDigit() }.length
        if (documentSizeDigits > 11) {
            document.isCnpj()
        } else {
            document.isCpf()
        }
    } else {
        false
    }
}

fun validateUserData(
    email: String?,
    document: String?,
    account_id: String,
): ResponseValidateDataParams {

    var errorTags: MutableList<String> = mutableListOf()

    var emailValidated = if (email == null) true else isEmailValid(email)
    var documentValidated = if (document == null) true else validateDocument(document)
    var accountIdValidated = !account_id.isNullOrEmpty()

    if (!emailValidated) errorTags.add(ErrorTags.RX013.toString())
    if (!documentValidated) errorTags.add(ErrorTags.RX014.toString())
    if (!accountIdValidated) errorTags.add(ErrorTags.RX006.toString())

    val isValid = emailValidated && documentValidated && accountIdValidated

    return getResponseValidateDataParams(isValid, errorTags = errorTags)
}

fun validateGatewayToken(gateway_token: String?): ResponseValidateDataParams {
    val isValid = !gateway_token.isNullOrEmpty()
    return getResponseValidateDataParams(isValid, ErrorTags.RP002.toString())
}

fun validateMerchantData(
    merchant_id: Int,
    callback_url: String,
): ResponseValidateDataParams {
    var errorTags: MutableList<String> = mutableListOf()

    var merchantIdValidated = merchant_id > 0
    var callbackUrlValidated = callback_url.isNotEmpty()

    if (!merchantIdValidated) {
        errorTags.add(ErrorTags.RX001.toString())
    }

    if (!callbackUrlValidated) {
        errorTags.add(ErrorTags.RX007.toString())
    }

    val isValid = merchantIdValidated && callbackUrlValidated

    return getResponseValidateDataParams(isValid, errorTags = errorTags)
}


fun validateOperation(operation: Int): ResponseValidateDataParams {
    val isValid = Operation.values().map { it.code }.contains(operation)
    return getResponseValidateDataParams(isValid, ErrorTags.RX002.toString())
}

fun validateCurrency(currency: String): ResponseValidateDataParams {
    val isValid = Currency.values().map { it.currency }.contains(currency)
    return getResponseValidateDataParams(isValid, ErrorTags.RX005.toString())
}

fun validateAmount(amount: Int): ResponseValidateDataParams {
    val isValid = amount >= 500
    return getResponseValidateDataParams(isValid, ErrorTags.RX004.toString())
}

fun validateAutoApprove(autoApprove: Int): ResponseValidateDataParams {
    val isValid = when (autoApprove) {
        0, 1 -> true
        else -> false
    }
    return getResponseValidateDataParams(isValid, ErrorTags.RX012.toString())
}

fun validateTypesNumber(type: Int, operation: Int): ResponseValidateDataParams {
    val isValid = if (operation == Operation.WITHDRAW.code) {
        type == TypesToSelect.PIX.code || type == TypesToSelect.WALLET.code
    } else type in 1..15
    return getResponseValidateDataParams(isValid, ErrorTags.RX011.toString())
}


fun validateDataTransaction(data: DataTransaction): ResponseValidateDataParams {
    val isValidType = validateTypesNumber(data.type, data.operation)
    val isValidOperation = validateOperation(data.operation)
    val isValidCurrency = validateCurrency(data.currency)
    val isValidAmount = validateAmount(data.amount)
    val isValidAutoApprove = validateAutoApprove(data.auto_approve)

    val isValidMerchantTransactionId = getResponseValidateDataParams(
        data.merchant_transaction_id.isNotEmpty(),
        ErrorTags.RX003.toString()
    )

    val isValid = isValidOperation.isValid &&
            isValidCurrency.isValid &&
            isValidType.isValid &&
            isValidAmount.isValid &&
            isValidMerchantTransactionId.isValid &&
            isValidAutoApprove.isValid

    val errorTags: MutableList<String> = mutableListOf()

    isValidType.errorTags?.let { errorTags.addAll(it) }
    isValidOperation.errorTags?.let { errorTags.addAll(it) }
    isValidCurrency.errorTags?.let { errorTags.addAll(it) }
    isValidAmount.errorTags?.let { errorTags.addAll(it) }
    isValidMerchantTransactionId.errorTags?.let { errorTags.addAll(it) }
    isValidAutoApprove.errorTags?.let { errorTags.addAll(it) }

    return ResponseValidateDataParams(isValid, errorTags)
}

fun validateBaseUrl(baseUrl: String?): ResponseValidateDataParams {
    val isValidUrl = when (baseUrl) {
        BASE_URL_ENVIRONMENT_DEV,
        BASE_URL_ENVIRONMENT_PLAYGROUND,
        BASE_URL_ENVIRONMENT_PRODUCTION,
        -> true
        else -> false
    }

    return getResponseValidateDataParams(
        isValidUrl,
        ErrorTags.RP001.toString()
    )
}

fun validateSignature(signature: String?): ResponseValidateDataParams {
    val isSignature = !signature.isNullOrEmpty()

    return getResponseValidateDataParams(
        isSignature,
        ErrorTags.RX008.toString()
    )
}


fun validateWithdrawDataPix(pix_key_type: Int?, pix_key: String?): ResponseValidateDataParams {
    val errorTags: MutableList<String> = mutableListOf()
    val isPixKeyTypeValid = TypePixKey.values().map { it.code }.contains(pix_key_type)
    val isPixKeyValid = getFieldStatusPixKeyValue(
        pix_key_type ?: -1, pix_key ?: "").isValidated

    if (!isPixKeyTypeValid) {
        errorTags.add(ErrorTags.RX015.toString())
    }

    if (!isPixKeyValid) {
        errorTags.add(ErrorTags.RX016.toString())
    }

    return ResponseValidateDataParams(isPixKeyTypeValid && isPixKeyValid, errorTags)
}

fun validateDataPix(
    operation: Int? = null,
    pix_key_type: Int? = null,
    pix_key: String? = null,
): Boolean {
    return when {
        operation == Operation.DEPOSIT.code -> true
        pix_key_type == -1 && pix_key == null -> true //Not enable autoWithdraw
        pix_key_type != -1 -> {
            when (pix_key_type) {
                TypePixKey.DOCUMENT.code -> validateDocument(pix_key)
                TypePixKey.EMAIL.code -> isEmailValid(pix_key)
                TypePixKey.PHONE.code -> cellPhoneValidator(pix_key ?: "")
                else -> false
            }
        }
        else -> false
    }
}

fun validateDataPayment(
    data: DataStartCheckout,
    typesStartCheckout: Int,
): ResponseValidData {

    val isValidGatewayToken = validateGatewayToken(data.gateway_token)

    val isValidMerchantData =
        validateMerchantData(data.merchant_id, data.callback_url)

    val isValidUserData = validateUserData(data.email_address, data.document, data.account_id)

    val isValidTransactionData = validateDataTransaction(
        DataTransaction(
            data.operation,
            data.currency,
            data.type,
            data.amount,
            data.merchant_transaction_id,
            data.auto_approve,
        )
    )

    val isValidDataPix = validateDataPix(
        data.operation,
        data.pix_key_type,
        data.pix_key
    )

    val isValidBaseUrl = validateBaseUrl(data.base_url)
    val isValidSignature = validateSignature(data.signature)

    var isValidData = isValidUserData.isValid &&
            isValidTransactionData.isValid &&
            isValidBaseUrl.isValid &&
            isValidMerchantData.isValid &&
            isValidDataPix

    var errorTags: MutableList<String> = mutableListOf()

    isValidUserData.errorTags?.let { errorTags.addAll(it) }
    isValidBaseUrl.errorTags?.let { errorTags.addAll(it) }
    isValidTransactionData.errorTags?.let { errorTags.addAll(it) }
    isValidMerchantData.errorTags?.let { errorTags.addAll(it) }

    if (typesStartCheckout == TypesStartCheckout.BY_URL.code) {
        isValidData = isValidData && isValidSignature.isValid
        isValidSignature.errorTags?.let { errorTags.addAll(it) }
    } else {
        isValidData = isValidData && isValidGatewayToken.isValid
        isValidGatewayToken.errorTags?.let { errorTags.addAll(it) }
    }

    val keyMsgMainError = if (isValidData) "" else "invalid_data_error"

    val messageDetailsError =
        if (!isValidDataPix) "invalid_pix_key" else "please_contact_support"

    var sErrorTags = errorTags.joinToString { it }

    return ResponseValidData(
        isValidData,
        keyMsgMainError,
        errorTags = sErrorTags,
        messageDetailsError = messageDetailsError
    )
}