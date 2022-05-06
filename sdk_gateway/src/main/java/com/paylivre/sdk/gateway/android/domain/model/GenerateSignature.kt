package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.data.model.order.getProcessedDataOrderRequest
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import com.paylivre.sdk.gateway.android.utils.encodeToBase64
import com.paylivre.sdk.gateway.android.utils.getRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



fun getURL(requiredDataGenerateSignature: DataGenerateSignature): String {
    val base_url = "${requiredDataGenerateSignature.base_url}?"
    val merchant_transaction_id = "merchant_transaction_id=${requiredDataGenerateSignature.merchant_transaction_id}"
    val merchant_id = "&merchant_id=${requiredDataGenerateSignature.merchant_id}"
    val account_id = "&account_id=${requiredDataGenerateSignature.account_id}"
    val amount = "&amount=${requiredDataGenerateSignature.amount}"
    val currency = "&currency=${requiredDataGenerateSignature.currency}"
    val operation = "&operation=${requiredDataGenerateSignature.operation}"
    val callback_url = "&callback_url=${requiredDataGenerateSignature.callback_url}"
    val type = "&type=${requiredDataGenerateSignature.type}"
    val auto_approve = "&auto_approve=${requiredDataGenerateSignature.auto_approve}"
    val email = if (requiredDataGenerateSignature.email.isNullOrEmpty()) "" else "&email=${requiredDataGenerateSignature.email}"
    val document_number = if (requiredDataGenerateSignature.document_number.isNullOrEmpty()) ""
    else "&document_number=${requiredDataGenerateSignature.document_number}"

    return base_url + merchant_transaction_id + merchant_id + operation + email + document_number + amount + currency + type + account_id + callback_url + auto_approve
}


data class ResponseGenerateSignature(
    val requiredDataGenerateSignature: DataGenerateSignature,
    val signature: String,
    val url: String,
)


class GenerateSignature(
    private val argon2iHash: Argon2iHash = Argon2iHash(),
    private val saltRandomString: String = getRandomString(14)
) {

    suspend fun generateSignature(
        requiredDataGenerateSignature: DataGenerateSignature,
    ): ResponseGenerateSignature {

        val processedDataOrderRequest = getProcessedDataOrderRequest(requiredDataGenerateSignature)

        val unsignedUrl = getURL(
            processedDataOrderRequest
        )

        return withContext(Dispatchers.Default) {
            val gatewayTokenUrl = requiredDataGenerateSignature.gateway_token + unsignedUrl
            val hashArgon2i = withContext(Dispatchers.Default) {
                argon2iHash.generateArgon2iHash(gatewayTokenUrl, saltRandomString)
            }
            val signatureBase64 = encodeToBase64(hashArgon2i).replace("\n", "")

            ResponseGenerateSignature(processedDataOrderRequest, signatureBase64, unsignedUrl)
        }

    }
}


