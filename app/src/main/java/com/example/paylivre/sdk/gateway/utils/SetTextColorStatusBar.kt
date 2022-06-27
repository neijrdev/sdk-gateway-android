package com.example.paylivre.sdk.gateway.utils

import android.app.Activity
import android.view.View

enum class TextStyleStatusBar {
    DARK,
    LIGHT
}

fun setTextThemeStatusBar(activity: Activity, textStyleStatusBar: String?) {
    if (textStyleStatusBar?.uppercase() == TextStyleStatusBar.LIGHT.toString()) {
        //set status text light
        activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility and
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    } else {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}