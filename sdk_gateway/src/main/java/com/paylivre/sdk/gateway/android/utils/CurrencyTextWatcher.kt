package com.paylivre.sdk.gateway.android.utils


import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Exception

class CurrencyTextWatcher(
    private val editText: EditText
) : TextWatcher {

    private var currentValue: String = ""

    private var updating = false

    override fun afterTextChanged(s: Editable?) {
        if (updating) return
        val stringValue = if (s.isNullOrEmpty()) "0,00" else s.toString();
        if (currentValue != stringValue) {
            updating = true
            try {
                val doubleValue = stringValue.fromCurrency()
                val formatted = getFormattedValue(doubleValue)
                updateValue(formatted)
            } catch (err: Exception) { updating = false }

        }
    }

    private fun getFormattedValue(value: Double): String = if (value == 0.0) {
        "R$ 0,00"
    } else value.toCurrencyBRL()

    private fun updateValue(formatted: String) {
        editText.setText(formatted)
        editText.setSelection(formatted.length)

        updating = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no used
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // not used
    }
}