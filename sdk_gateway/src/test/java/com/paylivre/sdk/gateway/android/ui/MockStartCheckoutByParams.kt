package com.paylivre.sdk.gateway.android.ui

import com.paylivre.sdk.gateway.android.StartCheckoutByParams
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.domain.model.DataStartCheckout
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.utils.BASE_URL_ENVIRONMENT_DEV
import java.util.*


class MockStartCheckoutByParams(
    request_code: Int? = null,
    merchant_id: Int = 100,
    gateway_token: String = "",
    operation: Int = Operation.DEPOSIT.code,
    merchant_transaction_id: String = "1234asd",
    amount: Int = 500,
    currency: String = Currency.BRL.currency,
    type: Int = 1,
    account_id: String = "12a3sda4s56",
    callback_url: String = "https://callback_url.com",
    email_address: String? = "test@test.com",
    document: String? = "66445843069",
    base_url : String = BASE_URL_ENVIRONMENT_DEV,
    auto_approve : Int = 1,
    logo_url : String = "https://logo_url.com/logo.png",
    pix_key_type: Int? = null,
    pix_key: String? = null,
) {

    var checkoutByParams = StartCheckoutByParams.Builder(
        request_code = request_code,
        merchant_id = merchant_id,
        gateway_token = gateway_token,
        operation = operation,
        merchant_transaction_id = merchant_transaction_id,
        amount = amount,
        currency = currency,
        type = type,
        account_id = account_id,
        callback_url = callback_url,
        base_url = base_url,
        auto_approve = auto_approve,
        email_address = email_address,
        document = document,
        logo_url = logo_url,
        pix_key_type = pix_key_type,
        pix_key = pix_key
    ).build()

    var dataCheckout = DataStartCheckout(
        merchant_id = merchant_id,
        gateway_token = gateway_token,
        operation = operation,
        merchant_transaction_id = merchant_transaction_id,
        amount = amount,
        currency = currency,
        type = type,
        account_id = account_id,
        callback_url = callback_url,
        base_url = base_url,
        auto_approve = auto_approve,
        email_address = email_address,
        document = document,
        logo_url = logo_url,
        pix_key_type = pix_key_type,
        pix_key = pix_key
    )

    val localeDefault = Locale.getDefault().toString()
    val languageDefaultFormatted = localeDefault.subSequence(0, 2).toString().lowercase()
}
