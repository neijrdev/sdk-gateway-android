package com.paylivre.sdk.gateway.android.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Exception

enum class MaskType(val mask: String) {
    CPF("###.###.###-##"),
    CNPJ("##.###.###/####-##"),
}

fun unmask(s: String): String {
    return s.replace("[^0-9]*".toRegex(), "")
}

fun getDefaultMask(str: String): String {
    return if (str.length <= 11) {
        MaskType.CPF.mask
    } else {
        MaskType.CNPJ.mask
    }
}


object MaskDocumentUtil {
    fun insert(
        editText: EditText,
        maskType: MaskType?,
    ): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var oldValue = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {

                val value = unmask(s.toString())

                val mask: String = when (maskType) {
                    MaskType.CPF -> MaskType.CPF.mask
                    MaskType.CNPJ -> MaskType.CNPJ.mask
                    else -> getDefaultMask(value)
                }

                var maskAux = ""

                if (isUpdating) {
                    oldValue = value
                    isUpdating = false
                    return
                }

                if (value.length > 11 && value == oldValue) {
                    return
                }


                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && value.length > oldValue.length ||
                        m != '#' && value.length < oldValue.length && value.length != i
                    ) {
                        maskAux += m
                        continue
                    }
                    maskAux += try {
                        value[i]
                    } catch (e: Exception) {
                        println(e)
                        break
                    }
                    i++
                }
                isUpdating = true

                editText.setText(maskAux)
                editText.setSelection(maskAux.length)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }
    }
}