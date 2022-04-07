package com.paylivre.sdk.gateway.android.utils

import android.content.res.Resources
import android.util.TypedValue

fun dpToPx(resources: Resources, valuePX: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        valuePX,
        resources.displayMetrics
    )
}