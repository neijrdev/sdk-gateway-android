package com.paylivre.sdk.gateway.android.utils

import android.util.Base64

fun encodeToBase64(input: String): String {
    return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
}
