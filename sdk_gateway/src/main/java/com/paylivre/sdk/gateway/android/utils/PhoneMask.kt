package com.paylivre.sdk.gateway.android.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Exception

const val maskPhone1 = "(##) ####-####" //(99) 9999-9999
const val maskPhone2 = "(##) #####-####" //(99) 99999-9999

fun getPhoneDefaultMask(str: String): String {
    return if (str.length <= 10) {
        maskPhone1
    } else {
        maskPhone2
    }
}

object MaskPhoneUtil {
    fun insert(editText: EditText): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var oldValue = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val value = unmask(s.toString())

                val mask = getPhoneDefaultMask(value)

                var maskAux = ""

                if(value.length > 10 && value == oldValue && value.length < 12){
                    return
                }

                if (isUpdating) {
                    oldValue = value
                    isUpdating = false
                    return
                }


                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && value.length > oldValue.length ||
                        m != '#' && value.length < oldValue.length && value.length != i) {
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