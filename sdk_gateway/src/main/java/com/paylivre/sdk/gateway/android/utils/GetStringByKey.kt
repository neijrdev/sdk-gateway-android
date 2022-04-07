package com.paylivre.sdk.gateway.android.utils

import android.app.Activity
import android.content.Context
import java.lang.Exception

fun getStringByKey(ctx: Context?, key: String): String {
    return try {
        if (ctx is Activity && key.isNotEmpty()) {
            val exampleGetIdByString =
                ctx.resources.getIdentifier(key, "string", ctx.packageName)
            if (exampleGetIdByString != 0) {
                ctx.resources.getString(exampleGetIdByString)
            } else {
                return ""
            }
        } else {
            ""
        }
    } catch (err: Exception) {
        ""
    }
}

fun getStringIdByKey(ctx: Context?, key: String): Int {
    return if (ctx is Activity) {
        ctx.resources.getIdentifier(key, "string", ctx.packageName)
    } else {
        0
    }
}