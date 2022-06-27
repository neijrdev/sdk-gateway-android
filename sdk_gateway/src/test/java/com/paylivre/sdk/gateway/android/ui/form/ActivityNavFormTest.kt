package com.paylivre.sdk.gateway.android.ui.form

import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.PaymentActivity
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class ActivityNavFormTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var activity: PaymentActivity? = null

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Before
    fun setUp() {

        val dataStartCheckout = DataStartCheckout(
            merchant_id = 123,
            gateway_token = "12345678",
            operation = Operation.WITHDRAW.code,
            merchant_transaction_id = "123456asd",
            amount = 500,
            currency = "BRL",
            type = 1,
            account_id = "12345646",
            callback_url = "https://callback.com",
            base_url = "https://dev.gateway.paylivre.com",
            auto_approve = 1,
            email_address = "test@test.com",
            document = "61317581075",
        )

        val intent = Intent()
        intent.putExtra("type_start_checkout", TypesStartCheckout.BY_PARAMS.code)
        intent.putExtra("merchant_id", dataStartCheckout.merchant_id)
        intent.putExtra("gateway_token", dataStartCheckout.gateway_token)
        intent.putExtra("operation", dataStartCheckout.operation)
        intent.putExtra("merchant_transaction_id", dataStartCheckout.merchant_transaction_id)
        intent.putExtra("amount", dataStartCheckout.amount)
        intent.putExtra("currency", dataStartCheckout.currency)
        intent.putExtra("type", dataStartCheckout.type) //PIX
        intent.putExtra("account_id", dataStartCheckout.account_id)
        intent.putExtra("callback_url", dataStartCheckout.callback_url)
        intent.putExtra("base_url", dataStartCheckout.base_url)
        intent.putExtra("auto_approve", dataStartCheckout.auto_approve)
//        intent.putExtra("document", dataStartCheckout.document)
//        intent.putExtra("email", dataStartCheckout.email_address)

        val dataStartCheckoutString = Gson().toJson(dataStartCheckout)
        intent?.putExtra("dataStartCheckout", dataStartCheckoutString)


        activity = Robolectric.buildActivity(PaymentActivity::class.java, intent)
            .create()
            .resume()
            .get()
    }


    @Test
    fun `FormStartPaymentFragment withdraw - PIX, given invalid document and email`() {
        val textViewTitleForm = activity!!.findViewById<TextView>(R.id.textViewTitleForm)

        val editTextEmail = activity!!.findViewById<EditText>(R.id.editEmail)
        val textViewErrorEmail = activity!!.findViewById<TextView>(R.id.textViewErrorEmail)
        val editTextDocument = activity!!.findViewById<TextInputEditText>(R.id.editDocument)
        val textViewErrorDocument = activity!!.findViewById<TextView>(R.id.textViewErrorDocument)
        val btnStartPayment = activity!!.findViewById<Button>(R.id.btnStartPayment)

        editTextEmail.setText("test@")
        editTextDocument.setText("6131758107")

        btnStartPayment.performClick()

        assertEquals("Email inválido.",
            textViewErrorEmail?.text.toString())

        assertEquals("Documento inválido.",
            textViewErrorDocument?.text.toString())

        assertEquals("Preencha os campos abaixo para continuar",
            textViewTitleForm?.text.toString())

    }
}