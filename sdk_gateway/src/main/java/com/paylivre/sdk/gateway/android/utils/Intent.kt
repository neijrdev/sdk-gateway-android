package com.paylivre.sdk.gateway.android.utils

import android.content.Intent
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.DataStartCheckout
import com.paylivre.sdk.gateway.android.domain.model.DepositStatus

data class FormDataExtra(
    val btnNameTypeSelected: Int = -1,
    val emailInputText: String = "null",
    val documentInputText: String = "null",
    val password: String = "",
    val emailWallet: String = "",
    val btnPixKeyTypeSelected: Int? = null,
    val pixKeyValue: String = "",
)

enum class TypesStartCheckout(val code: Int) {
    BY_PARAMS(0),
    BY_URL(1);

    companion object {
        private val map = values().associateBy(TypesStartCheckout::code)
        fun fromInt(type: Int) = map[type]
    }
}

fun setDataPaymentIntent(
    intent: Intent,
    typeStartCheckout: Int,
    data: DataStartCheckout,
    formDataExtra: FormDataExtra,
    logoResId: Int = R.drawable.ic_logo_paylivre_blue,
): Intent? {
    //Data StartCheckout
    val dataStartCheckoutString = Gson().toJson(data)
    intent?.putExtra("dataStartCheckout", dataStartCheckoutString)

    //Type StartCheckout
    intent?.putExtra("type_start_checkout", typeStartCheckout)

    //Base url
    intent?.putExtra("base_url", data.base_url)

    //MerchantData
    intent?.putExtra("merchant_id", data.merchant_id)
    intent?.putExtra("gateway_token", data.gateway_token)
    intent?.putExtra("callback_url", data.callback_url)

    //UserData
    if (data.email_address != null) {
        intent?.putExtra("email", data.email_address)
    }

    if (data.document != null) {
        intent?.putExtra("document", data.document)
    }


    intent?.putExtra("account_id", data.account_id)

    //TransactionData
    intent?.putExtra("merchant_transaction_id", data.merchant_transaction_id)
    intent?.putExtra("amount", data.amount)
    intent?.putExtra("currency", data.currency)
    intent?.putExtra("operation", data.operation)
    intent?.putExtra("type", data.type)
    intent?.putExtra("auto_approve", data.auto_approve)


    intent?.putExtra("pix_key_type", data.pix_key_type)

    if (data.pix_key != null) {
        intent?.putExtra("pix_key", data.pix_key)
    }


    //FormDatExtra
    intent?.putExtra("btn_number_type_selected", formDataExtra?.btnNameTypeSelected)
    intent?.putExtra("emailInputText", formDataExtra?.emailInputText)
    intent?.putExtra("documentInputText", formDataExtra?.documentInputText)
    intent?.putExtra("emailWallet", formDataExtra?.emailWallet)
    intent?.putExtra("password", formDataExtra?.password)
    intent?.putExtra("btnPixKeyTypeSelected", formDataExtra?.btnPixKeyTypeSelected)
    intent?.putExtra("pixKeyValue", formDataExtra?.pixKeyValue)

    //Logo Merchant
    intent?.putExtra("logoResId", logoResId)


    //Data URL
    if (data?.signature != null) {
        intent?.putExtra("signature", data?.signature)
    }
    if (data?.redirect_url != null) {
        intent?.putExtra("redirect_url", data?.redirect_url)
    }
    if (data?.logo_url != null) {
        intent?.putExtra("logo_url", data?.logo_url)
    }

    return intent
}