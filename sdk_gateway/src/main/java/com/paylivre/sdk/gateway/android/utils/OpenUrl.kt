package com.paylivre.sdk.gateway.android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri


fun openUrl(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
}
