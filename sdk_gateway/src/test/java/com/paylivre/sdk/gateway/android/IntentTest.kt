package com.paylivre.sdk.gateway.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.domain.model.DataStartCheckout
import com.paylivre.sdk.gateway.android.utils.FormDataExtra
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.utils.setDataPaymentIntent
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class IntentTest {
    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Test setDataPaymentIntent`() {
        //GIVEN
        val mockIntent = Intent()
        val mockFormDataExtra = FormDataExtra()
        val mockDataStartCheckout = DataStartCheckout(
            merchant_id = 0,
            gateway_token = "",
            operation = 0,
            merchant_transaction_id = "",
            amount = 0,
            currency = "",
            type = 0,
            account_id = "",
            callback_url = "",
            email_address = "test@test.com",
            document = "12345612312",
            base_url = "",
            auto_approve = 0,
            redirect_url = "https://test.com.br",
            logo_url = "https://test.com.br",
            signature = "sdfhasdfjkhasdlkfjhasdlfkjadshf",
            url = null,
            pix_key_type = 1,
            pix_key = "12345678912"
        )

        setDataPaymentIntent(mockIntent,
            TypesStartCheckout.BY_PARAMS.code,
            mockDataStartCheckout,
            mockFormDataExtra,
            0)

        //WHEN
        //check the data returned to activity via extras
        val extrasIntentExpected = Bundle()
        extrasIntentExpected.putString("type_start_checkout",
            TypesStartCheckout.BY_PARAMS.code.toString())
        extrasIntentExpected.putString("emailInputText", mockFormDataExtra.emailInputText)
        extrasIntentExpected.putString("base_url", mockDataStartCheckout.base_url)
        extrasIntentExpected.putString("logoResId", "0")
        extrasIntentExpected.putString("gateway_token", mockDataStartCheckout.gateway_token)
        extrasIntentExpected.putString("amount", mockDataStartCheckout.amount.toString())
        extrasIntentExpected.putString("account_id", mockDataStartCheckout.account_id)
        extrasIntentExpected.putString("dataStartCheckout", Gson().toJson(mockDataStartCheckout))
        extrasIntentExpected.putString("pix_key", mockDataStartCheckout.pix_key)
        extrasIntentExpected.putString("pixKeyValue", mockFormDataExtra.pixKeyValue)
        extrasIntentExpected.putString("type", mockDataStartCheckout.type.toString())
        extrasIntentExpected.putString("email", mockDataStartCheckout.email_address.toString())
        extrasIntentExpected.putString("btn_number_type_selected",
            mockFormDataExtra.btnNameTypeSelected.toString())
        extrasIntentExpected.putString("pix_key_type",
            mockDataStartCheckout.pix_key_type.toString())
        extrasIntentExpected.putString("merchant_id", mockDataStartCheckout.merchant_id.toString())
        extrasIntentExpected.putString("currency", mockDataStartCheckout.currency)
        extrasIntentExpected.putString("btnPixKeyTypeSelected",
            mockFormDataExtra.btnPixKeyTypeSelected.toString())
        extrasIntentExpected.putString("document", mockDataStartCheckout.document)
        extrasIntentExpected.putString("redirect_url", mockDataStartCheckout.redirect_url)
        extrasIntentExpected.putString("signature", mockDataStartCheckout.signature)
        extrasIntentExpected.putString("password", mockFormDataExtra.password)
        extrasIntentExpected.putString("auto_approve",
            mockDataStartCheckout.auto_approve.toString())
        extrasIntentExpected.putString("merchant_transaction_id",
            mockDataStartCheckout.merchant_transaction_id)
        extrasIntentExpected.putString("operation", mockDataStartCheckout.operation.toString())
        extrasIntentExpected.putString("documentInputText", mockFormDataExtra.documentInputText)
        extrasIntentExpected.putString("emailWallet", mockFormDataExtra.emailWallet)
        extrasIntentExpected.putString("logo_url", mockDataStartCheckout.logo_url)
        extrasIntentExpected.putString("callback_url", mockDataStartCheckout.callback_url)


        //THEN
        Assert.assertEquals(extrasIntentExpected.toString(), mockIntent.extras.toString())

    }

    @Test
    fun `Test TypesStartCheckout`() {
        Assert.assertEquals(TypesStartCheckout.BY_PARAMS, TypesStartCheckout.fromInt(0))
        Assert.assertEquals(TypesStartCheckout.BY_URL, TypesStartCheckout.fromInt(1))
    }
}