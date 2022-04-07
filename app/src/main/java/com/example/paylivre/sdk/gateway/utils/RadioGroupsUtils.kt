package com.example.paylivre.sdk.gateway.utils

import android.widget.RadioGroup

fun setIsEnableAllRadiosInRadioGroup(radioGroup: RadioGroup, isEnabled: Boolean) {
    for (i in 0 until radioGroup.childCount) {
        radioGroup.getChildAt(i).isEnabled = isEnabled
    }
}

fun checkAllEnablesRadiosInRadioGroup(radioGroup: RadioGroup): Boolean {
    var allEnables = true

    for (i in 0 until radioGroup.childCount) {
        if(!radioGroup.getChildAt(i).isEnabled){
            allEnables = false
        }
    }

    return allEnables
}