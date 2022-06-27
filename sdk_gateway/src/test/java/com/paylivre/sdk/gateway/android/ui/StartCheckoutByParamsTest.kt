package com.paylivre.sdk.gateway.android.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.paylivre.sdk.gateway.android.PaymentActivity
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
class StartCheckoutByParamsTest {

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `CASE 1, StartCheckoutByParams given, request_code=null, locale=null`() {
        //GIVEN
        val mockStartCheckoutByParams =  MockStartCheckoutByParams()
        val checkoutByURL = mockStartCheckoutByParams.checkoutByParams
        val activityMocked = Robolectric.setupActivity(Activity::class.java)
        val shadowActivity: ShadowActivity = shadowOf(activityMocked)
        val intentMocked = Intent().setClass(activityMocked, PaymentActivity::class.java)

        setDataPaymentIntent(
            intent = intentMocked,
            typeStartCheckout = TypesStartCheckout.BY_PARAMS.code,
            data = mockStartCheckoutByParams.dataCheckout,
            formDataExtra = FormDataExtra(),
            logoResId = -1
        )

        intentMocked.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //Set Locale Default
        intentMocked.putExtra("language", mockStartCheckoutByParams.languageDefaultFormatted)


        //Not PaymentActivity started
        Assert.assertEquals(null, shadowActivity.nextStartedActivity)

        //WHEN
        //Start PaymentActivity
        checkoutByURL.startPayment(activityMocked)

        //THEN
        //Test if PaymentActivity is started
        Assert.assertEquals(intentMocked.extras.toString(),
            shadowActivity.nextStartedActivity.extras.toString())
    }

    @Test
    fun `CASE 2, StartCheckoutByParams given, request_code=int, locale=en`() {
        //GIVEN
        val requestCode = 132
        val logoResId = R.drawable.logo_example
        val locale = "en_US"

        val mockStartCheckoutByParams =  MockStartCheckoutByParams(
            request_code = requestCode
        )
        val activityMocked = Robolectric.setupActivity(Activity::class.java)
        val shadowActivity: ShadowActivity = shadowOf(activityMocked)
        val intentActivityMocked = Intent().setClass(activityMocked, PaymentActivity::class.java)

        val checkoutByParams = mockStartCheckoutByParams.checkoutByParams

        //Set Locale Default
        intentActivityMocked.putExtra("language", "en")

        setDataPaymentIntent(
            intent = intentActivityMocked,
            typeStartCheckout = TypesStartCheckout.BY_PARAMS.code,
            data = mockStartCheckoutByParams.dataCheckout,
            formDataExtra = FormDataExtra(),
            logoResId = logoResId
        )

        //WHEN
        //Set Logo
        checkoutByParams.setLogoResId(logoResId)

        //Set Locale
        checkoutByParams.setLanguage(locale)

        //Start PaymentActivity
        checkoutByParams.startPayment(activityMocked)

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
    fun `CASE 3, StartCheckoutByParams with the setRegisterForResult configuration`() {
        //GIVEN
        val activityMocked = Robolectric.setupActivity(Activity::class.java)
        val mockActivityResultLauncher = mockk<ActivityResultLauncher<Intent>>(relaxed = true)
        val mockStartCheckoutByParams =  MockStartCheckoutByParams()

        //Config setRegisterForResult
        mockStartCheckoutByParams.checkoutByParams.setRegisterForResult(mockActivityResultLauncher)

        //WHEN
        //Start PaymentActivity
        mockStartCheckoutByParams.checkoutByParams.startPayment(activityMocked)

        //THEN
        //Test called launch
        verify {
            mockActivityResultLauncher.launch(any())
        }
    }



}