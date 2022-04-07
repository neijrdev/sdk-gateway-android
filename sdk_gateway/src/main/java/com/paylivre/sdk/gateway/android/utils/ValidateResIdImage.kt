package com.paylivre.sdk.gateway.android.utils

import android.content.Context
import androidx.core.content.ContextCompat
import java.lang.Exception

fun checkValidDrawableId(context: Context, LogoResId: Int): Boolean {
    if(LogoResId == -1){
        return false
    }
    return try {
        ContextCompat.getDrawable(context, LogoResId);
        true
    } catch (e: Exception) {
        println(e)
        false
    }
}