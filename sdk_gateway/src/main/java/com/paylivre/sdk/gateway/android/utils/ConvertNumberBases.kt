package com.paylivre.sdk.gateway.android.utils

fun convertBinaryToDecimal(num: Long): Int {
    var num = num
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}

fun convertDecimalToBinary(n: Int): Long {
    var n = n
    var binaryNumber: Long = 0
    var remainder: Int
    var i = 1
    var step = 1

    while (n != 0) {
        remainder = n % 2
        n /= 2
        binaryNumber += (remainder * i).toLong()
        i *= 10
    }
    return binaryNumber
}
