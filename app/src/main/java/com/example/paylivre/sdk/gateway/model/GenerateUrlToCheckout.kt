package com.example.paylivre.sdk.gateway.model

import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import com.paylivre.sdk.gateway.android.utils.encodeToBase64
import com.paylivre.sdk.gateway.android.utils.getRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class DataGenerateUrl(
    var base_url: String,
    val merchant_id: String,
    val merchant_transaction_id: String,
    val amount: String,
    val currency: String,
    val operation: String,
    val callback_url: String,
    val type: String,
    val account_id: String,
    val email: String? = null,
    val document_number: String? = null,
    val auto_approve: String,
    val redirect_url: String? = "",
    val logo_url: String? = "",
    val gateway_token: String,
    val pix_key_type: Int? = null,
    val pix_key: String? = null,
)

enum class BasesUrl(val base_url: String) {
    BASE_URL_DEV("https://dev.gateway.paylivre.com"),
    BASE_URL_PRODUCTION("https://app.gateway.paylivre.com"),
    BASE_URL_PLAYGROUND("https://playground.gateway.paylivre.com")
}


class GenerateUrlToCheckout(
    private val dataToGenerateUrl: DataGenerateUrl,
    private val argon2iHash: Argon2iHash = Argon2iHash(),
    private val saltRandomString: String = getRandomString(14),
) {

    fun getURLToSignature(): String {

        val base_url = "${dataToGenerateUrl.base_url}?"
        val merchant_transaction_id =
            "merchant_transaction_id=${dataToGenerateUrl.merchant_transaction_id}"
        val merchant_id =
            "&merchant_id=${dataToGenerateUrl.merchant_id}"
        val account_id =
            "&account_id=${dataToGenerateUrl.account_id}"
        val amount = "&amount=${dataToGenerateUrl.amount}"
        val currency = "&currency=${dataToGenerateUrl.currency}"
        val operation = "&operation=${dataToGenerateUrl.operation}"
        val callback_url =
            "&callback_url=${dataToGenerateUrl.callback_url}"
        val pix_key_type = if (dataToGenerateUrl.pix_key_type == null) ""
        else "&pix_key_type=${dataToGenerateUrl.pix_key_type}"
        val pix_key = if (dataToGenerateUrl.pix_key == null) ""
        else "&pix_key=${dataToGenerateUrl.pix_key}"
        val type = "&type=${dataToGenerateUrl.type}"
        val auto_approve =
            "&auto_approve=${dataToGenerateUrl.auto_approve}"
        val email =
            if (dataToGenerateUrl.email == null) ""
            else "&email=${dataToGenerateUrl.email}"
        val document_number =
            if (dataToGenerateUrl.document_number == null) ""
            else "&document_number=${dataToGenerateUrl.document_number}"
        val redirect_url =
            if (dataToGenerateUrl.redirect_url == null) ""
            else "&redirect_url=${dataToGenerateUrl.redirect_url}"
        val logo_url =
            if (dataToGenerateUrl.logo_url == null) ""
            else "&logo_url=${dataToGenerateUrl.logo_url}"

        return base_url + merchant_transaction_id + merchant_id +
                operation + email + document_number + amount +
                currency + type + account_id + callback_url +
                pix_key_type + pix_key + auto_approve +
                redirect_url + logo_url
    }

    private suspend fun generateSignature(): String {
        val unsignedUrl = getURLToSignature()

        return withContext(Dispatchers.Default) {
            val gatewayTokenUrl = dataToGenerateUrl.gateway_token + unsignedUrl
            val hashArgon2i = withContext(Dispatchers.Default) {
                argon2iHash.generateArgon2iHash(
                    gatewayTokenUrl,
                    saltRandomString
                )
            }
            val signatureBase64 = encodeToBase64(hashArgon2i).replace("\n", "")

            signatureBase64
        }
    }

    suspend fun getUrlWithSignature(): String {
        return withContext(Dispatchers.Default) {
            val unsignedUrl = getURLToSignature()
            val signature = withContext(Dispatchers.Default) {
                generateSignature()
            }
            val urlWithSignature = "$unsignedUrl&signature=$signature"
            urlWithSignature
        }
    }
}