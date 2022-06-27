package com.paylivre.sdk.gateway.android.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.paylivre.sdk.gateway.android.PaymentActivity
import com.paylivre.sdk.gateway.android.StartCheckoutByURL
import com.paylivre.sdk.gateway.android.domain.model.extractDataFromUrl
import com.paylivre.sdk.gateway.android.utils.FormDataExtra
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.utils.setDataPaymentIntent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity
import java.util.*
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class StartCheckoutByURLTest {

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    private var activityMocked: Activity =
        Robolectric.buildActivity(Activity::class.java).create().get()

    @Test
    fun `CASE 1, StartCheckoutByURL given, request_code=null, locale=null, url=null`() {
        //GIVEN
        val mockedUrl = ""
        val checkoutByURL = StartCheckoutByURL.Builder(
            request_code = null,
            url = mockedUrl
        ).build()
        val localeDefault = Locale.getDefault().toString()
        val languageDefaultFormatted = localeDefault.subSequence(0, 2).toString().lowercase()
        activityMocked = Robolectric.setupActivity(Activity::class.java)
        val shadowActivity: ShadowActivity = shadowOf(activityMocked)
        val intent = Intent().setClass(activityMocked, PaymentActivity::class.java)
        var dataCheckout = extractDataFromUrl(mockedUrl)
        setDataPaymentIntent(
            intent = intent,
            typeStartCheckout = TypesStartCheckout.BY_URL.code,
            data = dataCheckout,
            formDataExtra = FormDataExtra(),
            logoResId = -1
        )

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //Set Locale Default
        intent.putExtra("language", languageDefaultFormatted)


        //Not PaymentActivity started
        Assert.assertEquals(null, shadowActivity.nextStartedActivity)

        //WHEN
        //Start PaymentActi vity
        checkoutByURL.startPayment(activityMocked)

        //THEN
        //Test if PaymentActivity is started
        Assert.assertEquals(intent.extras.toString(),
            shadowActivity.nextStartedActivity.extras.toString())
    }

    @Test
    fun `CASE 2, StartCheckoutByURL given, request_code=int, locale=en, url=null`() {
        //GIVEN
        val mockedUrl = ""
        val requestCode = 132
        val logoResId = R.drawable.logo_example
        val locale = "en_US"

        val activityMocked = Robolectric.setupActivity(Activity::class.java)
        val shadowActivity: ShadowActivity = shadowOf(activityMocked)
        val intentActivityMocked = Intent().setClass(activityMocked, PaymentActivity::class.java)

        val checkoutByURL = StartCheckoutByURL.Builder(
            request_code = requestCode,
            url = mockedUrl
        ).build()


        var dataCheckout = extractDataFromUrl(mockedUrl)

        //Set Locale Default
        intentActivityMocked.putExtra("language", "en")

        setDataPaymentIntent(
            intent = intentActivityMocked,
            typeStartCheckout = TypesStartCheckout.BY_URL.code,
            data = dataCheckout,
            formDataExtra = FormDataExtra(),
            logoResId = logoResId
        )

        //WHEN
        //Set Logo
        checkoutByURL.setLogoResId(logoResId)

        //Set Locale
        checkoutByURL.setLanguage(locale)

        //Start PaymentActivity
        checkoutByURL.startPayment(activityMocked)

        val intentForResult = shadowActivity.nextStartedActivityForResult

        //THEN
        Assert.assertEquals(requestCode, intentForResult.requestCode)
        Assert.assertEquals("en", intentForResult.intent.getStringExtra("language").toString())
        Assert.assertEquals(logoResId,
            intentForResult.intent.getIntExtra("logoResId", -1))
        Assert.assertEquals(intentActivityMocked.extras.toString(),
            intentForResult.intent.extras.toString())
    }

    @Test
    fun `CASE 3, StartCheckoutByURL with the setRegisterForResult configuration`() {
        //GIVEN
        val activityMocked = Robolectric.setupActivity(Activity::class.java)
        val mockActivityResultLauncher = mockk<ActivityResultLauncher<Intent>>(relaxed = true)
        val checkoutByURL = StartCheckoutByURL.Builder(
            url = ""
        ).build()
        //Config setRegisterForResult
        checkoutByURL.setRegisterForResult(mockActivityResultLauncher)

        //WHEN
        //Start PaymentActivity
        checkoutByURL.startPayment(activityMocked)

        //THEN
        //Test called launch
        verify {
            mockActivityResultLauncher.launch(any())
        }
    }



}