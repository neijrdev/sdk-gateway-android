package com.paylivre.sdk.gateway.android.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.paylivre.sdk.gateway.android.R
import java.lang.Exception

fun showToast(message: String?, context: Context, duration: Int = Toast.LENGTH_SHORT) {
    val messageFailure = message ?: context.resources.getString(R.string.msg_error_copy_default)
    var toast: Toast = Toast.makeText(context, messageFailure, duration)
    toast.show();
}

fun copyToClipboard(
    context: Context,
    text: String?,
    msgSuccess: String? = null, msgFailure: String? = null,
) {
    try {

        val myClipboard: ClipboardManager? = context?.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager?

        val myClip: ClipData = ClipData.newPlainText("text", text)
        myClipboard!!.setPrimaryClip(myClip)

        val messageSuccess =
            msgSuccess ?: context.resources.getString(R.string.msg_copy_default_success)
        var toast: Toast = Toast.makeText(context, messageSuccess, Toast.LENGTH_SHORT
        )
        toast.show();
    } catch (e: Exception) { showToast(msgFailure ?: context.resources.getString(R.string.msg_error_copy_default), context) }
}