package com.paylivre.sdk.gateway.android.utils

fun cellPhoneValidator(phone: String): Boolean {
    val phoneNumber = phone.replace(("[^\\d ]").toRegex(), "").replace(" ", "")
    return phoneNumber.length in 10..11 &&
            phoneNumber[0] != '0' &&
            phoneNumber[1] != '0' &&
            phoneNumber[2].toString().toInt() >= 8
}

