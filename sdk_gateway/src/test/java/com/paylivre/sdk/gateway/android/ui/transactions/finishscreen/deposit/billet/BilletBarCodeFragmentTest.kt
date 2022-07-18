package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class BilletBarCodeFragmentTest {
    private var clipboardManager: ClipboardManager? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        clipboardManager = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Test
    fun `CASE 01, given click in button to copy code billet`() {
        val logEventsServiceImpl = mockk<LogEventsService>(relaxed = true)
        val billetDigitableLine = "34191790010104351004791020150008887870026000"

        loadKoinModules(module {
            single {
                logEventsServiceImpl
            }
        })

        val fragmentArgs = Bundle().apply {
           putString("billet_digitable_line", billetDigitableLine)
        }

        val fragment = launchFragmentInContainer<BilletBarCodeFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { billetBarCodeFragment ->
            //GIVEN
            val btnCopyCodeBillet = billetBarCodeFragment.view?.findViewById<Button>(R.id.btnCopyCodeBillet)

            //WHEN
            btnCopyCodeBillet?.performClick()

            //THEN
            Assert.assertEquals(billetDigitableLine, clipboardManager?.text.toString())
            Assert.assertEquals("Código do boleto copiado", ShadowToast.getTextOfLatestToast())

            verify {
                logEventsServiceImpl.setLogEventAnalytics("Btn_CopyCodeBillet")
            }

        }
    }

    @Test
    fun `CASE 02, given click in button to Save billet in PDF`() {
        val receivableUrl = "https://api.sandbox.bankly.com.br/bankslip/a90d092a-f1f9-47c1-aace-aa3f1afed91f/pdf"

        val logEventsServiceImpl = mockk<LogEventsService>(relaxed = true)

        loadKoinModules(module {
            single {
                logEventsServiceImpl
            }
        })


        val fragmentArgs = Bundle().apply {
            putString("receivable_url", receivableUrl)
        }

        val fragment = launchFragmentInContainer<BilletBarCodeFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)


        fragment.onFragment { billetBarCodeFragment ->
            //GIVEN
            val btnOpenBillet = billetBarCodeFragment.view?.findViewById<Button>(R.id.btnOpenBillet)

            //WHEN
            btnOpenBillet?.performClick()

            //THEN
            verify {
                logEventsServiceImpl.setLogEventAnalytics("Btn_OpenBillet")
            }

            val expectedIntent = Intent(Intent.ACTION_VIEW, Uri.parse(receivableUrl))
            val actual: Intent = shadowOf(RuntimeEnvironment.application).nextStartedActivity

           Assert.assertEquals(expectedIntent.data, actual.data)

        }
    }


    @Test
    fun `CASE 03, given click in button to Save billet in PDF and receivable_url is null`() {
        val receivableUrl = "https://api.sandbox.bankly.com.br/bankslip/a90d092a-f1f9-47c1-aace-aa3f1afed91f/pdf"

        val logEventsServiceImpl = mockk<LogEventsService>(relaxed = true)

        loadKoinModules(module {
            single {
                logEventsServiceImpl
            }
        })


        val fragmentArgs = Bundle()

        val fragment = launchFragmentInContainer<BilletBarCodeFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)


        fragment.onFragment { billetBarCodeFragment ->
            //GIVEN
            val btnOpenBillet = billetBarCodeFragment.view?.findViewById<Button>(R.id.btnOpenBillet)

            //WHEN
            btnOpenBillet?.performClick()

            //THEN
            verify {
                logEventsServiceImpl.setLogEventAnalytics("Btn_OpenBillet")
            }
            verify {
                logEventsServiceImpl.setLogEventAnalytics("Error_OpenBillet")
            }
            Assert.assertNull(shadowOf(RuntimeEnvironment.application).nextStartedActivity)
            Assert.assertEquals("Houve um erro ao tentar abrir o link do boleto",
                ShadowToast.getTextOfLatestToast())

        }
    }
}