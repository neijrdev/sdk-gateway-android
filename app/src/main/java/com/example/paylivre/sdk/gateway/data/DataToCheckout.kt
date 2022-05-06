package com.example.paylivre.sdk.gateway.data

data class MerchantData(
    val merchant_id: String,
    val gateway_token: String,
)

val dataMerchantPlayground = MerchantData(
    "1508",
    "0c85yWaiBWx1Mclvink7suWrHoEQnH8Q"
)

val dataMerchantDev = MerchantData(
    "302",
    "NHsuzedl6omTPvoxc0p7gVXc7Xthhf6Y"
)

const val REDIRECT_URL = "https://www.merchant_to_you.com"
const val CALLBACK_URL = "https://www.google.com"
const val LOGO_URL =
    "https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true"
const val USER_EMAIL = "user_gateway_test@tutanota.com"
const val USER_DOCUMENT_NUMBER = "61317581075"

data class DataStartCheckoutByParams(
    val merchant_id: Int,
    val gateway_token: String,
    val operation: Int,
    val merchant_transaction_id: String,
    val amount: Int,
    val currency: String,
    val type: Int,
    val account_id: String,
    val callback_url: String,
    val base_url: String,
    val email_address: String? = null, //Optional
    val document: String? = null, //Optional
    val auto_approve: Int,
    val logo_url: String? = null,
    val pix_key_type: Int? = null,
    val pix_key: String? = null,
)