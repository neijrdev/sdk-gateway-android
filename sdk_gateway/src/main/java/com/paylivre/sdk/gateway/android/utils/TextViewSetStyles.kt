package com.paylivre.sdk.gateway.android.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import java.lang.Exception

import androidx.core.content.ContextCompat
import com.paylivre.sdk.gateway.android.R


data class DataMakeBold(
    val label: String,
    val value: String,
    val bgColorId: Int = R.color.grey,
)

fun TextView.makeBold(vararg texts: DataMakeBold) {
    try {
        val spannableString = SpannableString(this.text)
        for (prop in texts) {
            var boldStyle = StyleSpan(Typeface.BOLD);
            val backgroundColor = ContextCompat.getColor(context, prop.bgColorId)
            var colorStyle = BackgroundColorSpan(backgroundColor);
            val startIndexOfLabel = this.text.toString().indexOf(prop.label)
            if(startIndexOfLabel!=-1){
                spannableString.setSpan(
                    boldStyle, startIndexOfLabel, startIndexOfLabel + prop.label.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
//                spannableString.setSpan(
//                    colorStyle,
//                    startIndexOfLabel,
//                    startIndexOfLabel + prop.label.length + prop.value.length,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
//                )
            }

        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    } catch (error: Exception) {
        println("error: $error")
    }
}
