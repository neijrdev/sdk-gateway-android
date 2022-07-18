package com.paylivre.sdk.gateway.android.ui.error

import android.content.Context
import android.view.View
import android.widget.TextView
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.domain.model.LIMIT_VALUE_WITHDRAW_BRL
import com.paylivre.sdk.gateway.android.domain.model.LIMIT_VALUE_WITHDRAW_USD
import com.paylivre.sdk.gateway.android.utils.*

fun isExceptionalCases(keyError: String?): Boolean {
    return when (keyError) {
        "exceeded_withdrawal_limit_value" -> true
        "user_not_found_with_document_number" -> true
        "withdrawal_limits_error_content" -> true
        else -> false
    }
}

fun setMessageErrorInTextView(
    messageError: String?,
    textView: TextView?,
) {
    if (!messageError.isNullOrEmpty()) {
        textView?.text = messageError
        textView?.visibility = View.VISIBLE
    }
}

fun exceptionalCasesMessageAdapter(
    context: Context?,
    keyError: String?,
    textView: TextView?,
    currency: String? = null,
) {
    when (keyError) {
        "exceeded_withdrawal_limit_value" -> {
            val message = getStringByKey(context, keyError + currency)
            setMessageErrorInTextView(message, textView)
        }
        "user_not_found_with_document_number" -> {
            val message = getStringByKey(context, keyError.toString())
            setMessageErrorInTextView(message, textView)

            textView?.makeLinks(
                Pair("web.paylivre.com", View.OnClickListener {
                    val url = "https://web.paylivre.com/signup"
                    context?.let { ctx -> openUrl(ctx, url) }
                }),
            )
        }
        "withdrawal_limits_error_content" -> {
            val withdrawLimitValueFormatted =
                if (currency == Currency.BRL.toString()) {
                    formatToCurrencyBRL(LIMIT_VALUE_WITHDRAW_BRL, 100)
                } else formatToCurrencyUSD(LIMIT_VALUE_WITHDRAW_USD, 100)
            val message = context?.resources?.getString(
                R.string.withdrawal_limits_error_content,
                withdrawLimitValueFormatted)
            setMessageErrorInTextView(message, textView)
        }
    }
}

fun messageAdapter(
    context: Context?,
    keyError: String?,
    textView: TextView?,
) {
    val message = getStringByKey(context, keyError.toString())
    setMessageErrorInTextView(message, textView)
}


fun setErrorMessage(
    context: Context?,
    keyError: String?,
    textView: TextView?,
    currency: String? = null,
) {
    if (isExceptionalCases(keyError)) {
        exceptionalCasesMessageAdapter(context, keyError, textView, currency)
    } else {
        messageAdapter(context, keyError, textView)
    }
}

fun setErrorTitleMessage(
    context: Context?,
    keyError: String?,
    textView: TextView?,
) {
    if (keyError == "withdrawal_limits_error") {
        textView?.text =  context?.resources?.getString(R.string.warning_label)
    } else {
        textView?.text =  context?.resources?.getString(R.string.error_title)
    }
}