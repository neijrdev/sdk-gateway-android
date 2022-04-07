package com.paylivre.sdk.gateway.android.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormStartPaymentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is VerifuUser ViewModel"
    }
    val text: LiveData<String> = _text
}