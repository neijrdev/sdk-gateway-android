package com.paylivre.sdk.gateway.android.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import java.lang.Exception
import com.paylivre.sdk.gateway.android.R


data class DataMakeBold(
    val label: String,
    val value: String,
    val bgColorId: Int = R.color.grey_color_paylivre_sdk,
)

fun TextView.makeBold(vararg texts: DataMakeBold) {
    try {
        val spannableString = SpannableString(this.text)
        for (prop in texts) {
            var boldStyle = StyleSpan(Typeface.BOLD);
            val startIndexOfLabel = this.text.toString().indexOf(prop.label)
            if(startIndexOfLabel!=-1){
                spannableString.setSpan(
                    boldStyle, startIndexOfLabel, startIndexOfLabel + prop.label.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }

        }
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    } catch (error: Exception) {
        println("error: $error")
    }
}
