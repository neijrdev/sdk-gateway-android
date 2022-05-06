package com.paylivre.sdk.gateway.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amplitude.api.Amplitude
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.domain.model.DataStartCheckout
import com.paylivre.sdk.gateway.android.domain.model.validateLocaleLanguage
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.utils.FormDataExtra
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.utils.setDataPaymentIntent
import io.sentry.Sentry
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class StartCheckout private constructor(
    private val requestCode: Int? = null,
    private val merchant_id: Int,
    private val gateway_token: String,
    private val operation: Int,
    private val merchant_transaction_id: String,
    private val amount: Int,
    private val currency: String,
    private val type: Int,
    private val account_id: String,
    private val callback_url: String,
    private val email_address: String? = null,//OPTIONAL
    private val document: String? = null,//OPTIONAL
    private val base_url: String,
    private val auto_approve: Int,
    private val logo_url: String? = null,//OPTIONAL
    private val pix_key_type: Int? = null,//OPTIONAL
    private val pix_key: String? = null,//OPTIONAL
) {
    private var logoResId: Int = -1
    private var language: String? = null
    private var startForCheckoutResult: ActivityResultLauncher<Intent>? = null

    fun setLogoResId(resId: Int) {
        logoResId = resId
    }

    fun setRegisterForResult(StartForCheckoutResult: ActivityResultLauncher<Intent>) {
        startForCheckoutResult = StartForCheckoutResult
    }

    fun setLanguage(languageConfig: String) {
        language = languageConfig
    }

    fun startPayment(context: Context) {
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
            email_address = email_address,
            document = document,
            base_url = base_url,
            auto_approve = auto_approve,
            logo_url = logo_url,
            pix_key_type = pix_key_type,
            pix_key = pix_key
        )

        addSentryBreadcrumb("data_start_checkout", dataCheckout.toString())
        Sentry.setExtra("language_start_checkout", language.toString())
        Sentry.setExtra("logo_res_id_start_checkout", logoResId.toString())

        //Set Log Analytics
        LogEvents.setLogEventAnalyticsWithParams(
            "StartCheckoutSDK",
            Pair("type_start_checkout", "by_params"),
            Pair("operation", dataCheckout.operation.toString()),
            Pair("data_start_checkout", dataCheckout.toString()),
            Pair("type", dataCheckout.type.toString()),
            Pair("language_start_checkout", language.toString()),
            Pair("logo_res_id_start_checkout", logoResId.toString())
        )


        startIntent(
            context,
            PaymentActivity.getIntent(context),
            dataCheckout,
            logoResId,
            requestCode,
            startForCheckoutResult
        )
    }

    private fun startIntent(
        context: Context,
        paymentIntent: Intent?,
        dataStartCheckout: DataStartCheckout,
        logoResId: Int,
        requestCode: Int? = null,
        startForCheckoutResult: ActivityResultLauncher<Intent>? = null,
    ) {
        //Insert Data to PaymentData
        println("dataStartCheckout:" + dataStartCheckout)

        val paymentIntentWithData =
            paymentIntent?.let {
                setDataPaymentIntent(
                    it,
                    TypesStartCheckout.BY_PARAMS.code,
                    dataStartCheckout,
                    FormDataExtra(),
                    logoResId
                )
            }


        @Suppress("DEPRECATION")
        fun setLocale(localeName: String?) {
            val locale = Locale(localeName)
            val res = context.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(locale)
            res.updateConfiguration(conf, dm)
        }

        //Sets the passed locale if valid or sets the default device locale
        if (validateLocaleLanguage(language)) {
            val languageFormatted = language?.subSequence(0, 2).toString().lowercase()
            setLocale(languageFormatted)
            paymentIntentWithData?.putExtra("language", languageFormatted)
        } else {
            val localeDefault = Locale.getDefault().toString()
            val languageDefaultFormatted = localeDefault.subSequence(0, 2).toString().lowercase()
            setLocale(languageDefaultFormatted)
            paymentIntentWithData?.putExtra("language", languageDefaultFormatted)
        }


        when {
            startForCheckoutResult != null -> {
                startForCheckoutResult.launch(paymentIntentWithData)
            }
            context is Activity && requestCode != null -> {
                context.startActivityForResult(paymentIntentWithData, requestCode)
            }
            else -> {
                paymentIntentWithData?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(paymentIntentWithData)
            }
        }
    }


    class Builder(
        private val request_code: Int? = null,
        private var merchant_id: Int,
        private var gateway_token: String,
        private var operation: Int,
        private var merchant_transaction_id: String,
        private var amount: Int,
        private var currency: String,
        private var type: Int,
        private var account_id: String,
        private var callback_url: String,
        private val base_url: String = "",
        private val auto_approve: Int,
        private val email_address: String? = null,
        private val document: String? = null,
        private val logo_url: String? = null,
        private val pix_key_type: Int? = null,
        private val pix_key: String? = null,
    ) {


        fun build(): StartCheckout {
            return StartCheckout(
                request_code,
                merchant_id,
                gateway_token,
                operation,
                merchant_transaction_id,
                amount,
                currency,
                type,
                account_id,
                callback_url,
                email_address,
                document,
                base_url,
                auto_approve,
                logo_url,
                pix_key_type,
                pix_key
            )
        }
    }
}



