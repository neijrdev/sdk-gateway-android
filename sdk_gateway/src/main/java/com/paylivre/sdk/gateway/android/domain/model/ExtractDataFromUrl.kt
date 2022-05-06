package com.paylivre.sdk.gateway.android.domain.model

import android.net.Uri
import android.webkit.URLUtil
import com.paylivre.sdk.gateway.android.utils.*
import java.lang.Exception
import java.net.URLDecoder


fun getDecodedUrl(url: String): String {
    return URLDecoder.decode(url, "UTF-8")
}


fun getBaseHost(url: String): String {
    val uri: Uri = Uri.parse(url)
    val uriScheme = uri.scheme
    val urlHttpPrefix = "${uriScheme}://"
    println("uri.path: ${uri.path}")
    return if (uri.host != null) {
        urlHttpPrefix + uri.host.toString()
    } else ""
}

fun getQueryParameterUrlInt(url: String, key: String): Int {
    return try {
        val uri: Uri = Uri.parse(url)
        val queryParameter = uri.getQueryParameter(key)
        if (queryParameter != null) {
            if (isIntNumber(queryParameter)) {
                queryParameter.toInt()
            } else -1
        } else -1
    } catch (e: Exception) {
        -1
    }
}

fun getQueryParameterUrlString(url: String, key: String): String {
    return try {
        val uri: Uri = Uri.parse(url)
        return uri.getQueryParameter(key) ?: ""
    } catch (e: Exception) {
        ""
    }
}

fun getQueryParameterUrlStringOrNull(
    url: String,
    key: String,
    returnNullIfParamNotExist: Boolean
): String? {
    return try {
        val uri: Uri = Uri.parse(url)
        val paramValue = uri.getQueryParameter(key)
        return if (paramValue == null && !returnNullIfParamNotExist) {
            ""
        } else {
            paramValue
        }
    } catch (e: Exception) {
        return if (returnNullIfParamNotExist) {
            null
        } else {
            ""
        }
    }
}


fun extractDataFromUrl(url: String, returnNullIfParamNotExist: Boolean = true): DataStartCheckout {
    var dataStartCheckout = DataStartCheckout(
        merchant_id = -1, operation = -1,
        merchant_transaction_id = "", amount = -1,
        currency = "", type = -1, account_id = "", callback_url = "",
        email_address = null, document = null, base_url = "",
        auto_approve = -1, signature = "", logo_url = "", redirect_url = "",
        gateway_token = "", url = url, pix_key_type = -1, pix_key = null
    )

    val urlDecoded = getDecodedUrl(url)
    val isValidUrl = URLUtil.isValidUrl(urlDecoded)

    if (isValidUrl) {
        dataStartCheckout.merchant_id =
            getQueryParameterUrlInt(urlDecoded, "merchant_id")
        dataStartCheckout.operation =
            getQueryParameterUrlInt(urlDecoded, "operation")
        dataStartCheckout.merchant_transaction_id =
            getQueryParameterUrlString(urlDecoded, "merchant_transaction_id")
        dataStartCheckout.amount = getQueryParameterUrlInt(urlDecoded, "amount")
        dataStartCheckout.currency =
            getQueryParameterUrlString(urlDecoded, "currency")
        dataStartCheckout.type = getQueryParameterUrlInt(urlDecoded, "type")
        dataStartCheckout.account_id =
            getQueryParameterUrlString(urlDecoded, "account_id")
        dataStartCheckout.callback_url =
            getQueryParameterUrlString(urlDecoded, "callback_url")
        dataStartCheckout.email_address =
            getQueryParameterUrlStringOrNull(urlDecoded, "email", returnNullIfParamNotExist)
        dataStartCheckout.document =
            getQueryParameterUrlStringOrNull(
                urlDecoded,
                "document_number",
                returnNullIfParamNotExist
            )
        dataStartCheckout.base_url = getBaseHost(urlDecoded)
        dataStartCheckout.auto_approve =
            getQueryParameterUrlInt(urlDecoded, "auto_approve")
        dataStartCheckout.signature =
            getQueryParameterUrlString(urlDecoded, "signature")
        dataStartCheckout.logo_url =
            getQueryParameterUrlString(urlDecoded, "logo_url")
        dataStartCheckout.redirect_url =
            getQueryParameterUrlString(urlDecoded, "redirect_url")

        dataStartCheckout.pix_key_type =
            getQueryParameterUrlInt(urlDecoded, "pix_key_type")

        dataStartCheckout.pix_key =
            getQueryParameterUrlStringOrNull(urlDecoded, "pix_key", returnNullIfParamNotExist)
    }


    return dataStartCheckout
}

fun getUrlWithoutSignature(url: String): String {
    return try {
        val uri: Uri = Uri.parse(url)
        val signatureParams = "&signature=${uri.getQueryParameter("signature")}"
        if (signatureParams != null) {
            url.replace(signatureParams, "")
        } else url
    } catch (e: Exception) {
        url
    }
}