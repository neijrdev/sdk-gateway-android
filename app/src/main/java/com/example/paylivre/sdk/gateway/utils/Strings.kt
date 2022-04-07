package com.example.paylivre.sdk.gateway.utils

import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat

fun getStringDataFormatted(label: String, value: String?): Spanned {
    val stringStatusFull =
        "<font><b>$label =</b></font> $value"
    val stringHtmlStyle =
        HtmlCompat.fromHtml(stringStatusFull, HtmlCompat.FROM_HTML_MODE_LEGACY);
    return stringHtmlStyle
}

fun setValueTextViewWithLabelBold(textView: TextView, label: String, value: String?) {
    textView.text = getStringDataFormatted(label, value)
    textView.visibility = View.VISIBLE
}