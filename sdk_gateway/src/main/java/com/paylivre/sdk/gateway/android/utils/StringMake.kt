package com.paylivre.sdk.gateway.android.utils

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.paylivre.sdk.gateway.android.R
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import java.lang.Exception
import android.text.SpannableStringBuilder

import android.text.style.StyleSpan





fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    try {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                val colorPrimary = getColor(context, R.color.primary_color_paylivre_sdk)
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = colorPrimary
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)

            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    } catch (error: Exception) { }

}

fun setTextWithSpan(textView: TextView, text: String, spanText: String, style: StyleSpan?) {
    val sb = SpannableStringBuilder(text)
    val start = text.indexOf(spanText)
    val end = start + spanText.length
    sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    textView.text = sb
}


fun setTextBackground(
    context: Context,
    view: TextView,
    fulltext: String,
    subtext: String,
    backgroundColor: Int,
    textColor: Int = R.color.white_color_paylivre_sdk,
) {
    view.setText(fulltext, TextView.BufferType.SPANNABLE)
    val str = view.text as Spannable
    val i = fulltext.indexOf(subtext)
    val colorBgSpan = getColor(context, backgroundColor)
    val colorTextSpan = getColor(context, textColor)
    str.setSpan(
        BackgroundColorSpan(colorBgSpan),
        i,
        i + subtext.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    str.setSpan(
        ForegroundColorSpan(colorTextSpan),
        i,
        i + subtext.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}