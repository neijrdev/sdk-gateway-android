package com.paylivre.sdk.gateway.android.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val onlyNumberRegex by lazy { "[^0-9 ]".toRegex() }
private const val DECIMAL_FACTOR = 100
private const val CURRENCY_PATTERN_BRL = "R$ #,###,##0.00"
private const val CURRENCY_PATTERN_USD = "US$ #,###,##0.00"

val localeDefault = Locale.getDefault().toString()
val languageDefaultFormatted = localeDefault.subSequence(0, 2).toString().lowercase()

fun String.fromCurrency(): Double = this
    .replace(onlyNumberRegex, "")
    .toDouble()
    .div(DECIMAL_FACTOR)


fun Double.toCurrencyBRL(language: String = languageDefaultFormatted): String =
    DecimalFormat(CURRENCY_PATTERN_BRL, DecimalFormatSymbols(getLocale(language)))
        .format(this)

fun getLocale(localeString: String): Locale {
    return when (localeString.lowercase()) {
        "en" -> Locale.US
        else -> Locale("pt", "BR")
    }
}

fun Double.toCurrencyUSD(language: String = languageDefaultFormatted): String =
    DecimalFormat(CURRENCY_PATTERN_USD, DecimalFormatSymbols(getLocale(language)))
        .format(this)


fun isNumber(s: String): Boolean {
    return try {
        s.toDouble()
        true
    } catch (ex: NumberFormatException) {
        false
    }
}

fun isIntNumber(s: String): Boolean {
    return try {
        s.toInt()
        true
    } catch (ex: NumberFormatException) {
        false
    }
}

fun formatToCurrencyUSD(
    value: String?,
    decimalFactor: Int = 100,
    language: String = languageDefaultFormatted
): String {
    return if (value != null) {
        if (isNumber(value)) {
            value.toDouble().div(decimalFactor).toCurrencyUSD(language)
        } else ""
    } else ""
}

fun formatToCurrencyBRL(
    value: String?,
    decimalFactor: Int = 100,
    language: String = languageDefaultFormatted
): String {
    return if (value != null) {
        if (isNumber(value)) {
            value.toDouble().div(decimalFactor).toCurrencyBRL(language)
        } else ""
    } else ""
}