package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.utils.*

data class DataStartCheckout(
    var merchant_id: Int,
    val gateway_token: String?,
    var operation: Int,
    var merchant_transaction_id: String,
    var amount: Int,
    var currency: String,
    var type: Int,
    var account_id: String,
    var callback_url: String,
    var email_address: String? = null,
    var document: String? = null,
    var base_url: String,
    var auto_approve: Int,
    var redirect_url: String? = null,
    var logo_url: String? = null,
    var signature: String? = null,
    val url: String? = null,
    var pix_key_type: Int? = null,
    var pix_key: String? = null,
)


enum class Operation(val code: Int) {
    DEPOSIT(0),
    WITHDRAW(5),
}

enum class Type(val code: Int) {
    WIRETRANSFER(0),
    BILLET(1),
    PIX(4),
    WALLET(5);

    companion object {
        private val map = values().associateBy(Type::code)
        fun fromInt(type: Int) = map[type]
    }
}

enum class TypesToSelect(val code: Int) {
    PIX(1),
    BILLET(2),
    WIRETRANSFER(4),
    WALLET(8),
}


enum class TypePixKey(val code: Int) {
    DOCUMENT(0),
    PHONE(1),
    EMAIL(2),
}


enum class Currency(val currency: String) {
    BRL("BRL"),
    USD("USD"),
}

enum class CurrencyPrefix(val prefix: String) {
    BRL("R$"),
    USD("$"),
}


fun getBaseUrlByEnvironment(environment: String): String {
    return when (environment) {
        Environments.PLAYGROUND.toString() -> BASE_URL_ENVIRONMENT_PLAYGROUND
        Environments.PRODUCTION.toString() -> BASE_URL_ENVIRONMENT_PRODUCTION
        Environments.DEVELOPMENT.toString() -> BASE_URL_ENVIRONMENT_DEV
        else -> "INVALID_ENVIRONMENT"
    }
}

fun getHostApiByEnvironment(environment: String): String {
    return when (environment) {
        Environments.PLAYGROUND.toString() -> API_HOST_ENVIRONMENT_PLAYGROUND
        Environments.PRODUCTION.toString() -> API_HOST_ENVIRONMENT_PRODUCTION
        Environments.DEVELOPMENT.toString() -> API_HOST_ENVIRONMENT_DEV
        else -> "INVALID_ENVIRONMENT"
    }
}

fun getHostApiByBaseUrl(baseUrl: String): String {
    return when (baseUrl) {
        BASE_URL_ENVIRONMENT_PLAYGROUND -> API_HOST_ENVIRONMENT_PLAYGROUND
        BASE_URL_ENVIRONMENT_PRODUCTION -> API_HOST_ENVIRONMENT_PRODUCTION
        BASE_URL_ENVIRONMENT_DEV -> API_HOST_ENVIRONMENT_DEV
        else -> "INVALID_ENVIRONMENT"
    }
}