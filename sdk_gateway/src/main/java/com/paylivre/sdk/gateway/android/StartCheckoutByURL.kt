package com.paylivre.sdk.gateway.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.paylivre.sdk.gateway.android.data.api.addSentryBreadcrumb
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.FormDataExtra
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.utils.setDataPaymentIntent
import io.sentry.Sentry
import java.util.*

class StartCheckoutByURL private constructor(
    private val requestCode: Int? = null,
    private val url: String,
) {
    private var logoResId: Int = -1
    private var language: String? = null
    private var startForCheckoutResult: ActivityResultLauncher<Intent>? = null

    fun setRegisterForResult(StartForCheckoutResult: ActivityResultLauncher<Intent>) {
        startForCheckoutResult = StartForCheckoutResult
    }

    fun setLogoResId(resId: Int) {
        logoResId = resId
    }

    fun setLanguage(languageConfig: String) {
        language = languageConfig
    }

    fun startPayment(context: Context) {
        var dataCheckout = extractDataFromUrl(url)

        addSentryBreadcrumb("url_start_checkout", url)
        Sentry.setExtra("language_start_checkout", language.toString())
        Sentry.setExtra("logo_res_id_start_checkout", logoResId.toString())
        Sentry.setExtra("data_start_checkout", dataCheckout.toString())

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
        val paymentIntentWithData =
            paymentIntent?.let {
                setDataPaymentIntent(
                    it,
                    TypesStartCheckout.BY_URL.code,
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
        private var url: String,
    ) {

        fun build(): StartCheckoutByURL {
            return StartCheckoutByURL(
                request_code,
                url,
            )
        }
    }
}


