package com.paylivre.sdk.gateway.android.utils

import android.content.Context
import androidx.core.content.ContextCompat

fun getColorById (context: Context, colorId: Int): Int {
    return ContextCompat.getColor(context, colorId)
}