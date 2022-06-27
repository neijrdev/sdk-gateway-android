package com.paylivre.sdk.gateway.android.utils

import com.paylivre.sdk.gateway.android.data.model.order.Errors
import java.lang.Exception

fun getBlockReasonByLocale(errors: Errors? = null, locale: String? = null): String {
    try {
        return when (locale) {
            "pt" -> return errors?.public_reason_pt?:""
            "en" -> return errors?.public_reason_en?:""
            "es" -> return errors?.public_reason_es?:""
            else -> return errors?.public_reason_en?:""
        }
    } catch (e: Exception) {
        return ""
    }
}