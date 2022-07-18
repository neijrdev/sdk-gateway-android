package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class ButtonBankInfoRowTest {

    private var clipboardManager: ClipboardManager? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        clipboardManager = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Test
    fun `CASE 01, test render components from ButtonBankInfoRow and Clipboard function`() {
        val fragmentArgs = Bundle()
        fragmentArgs.putString("label", "Test")
        fragmentArgs.putString("value", "123456")
        fragmentArgs.putBoolean("isShowDividerLine", true)

        val fragment = launchFragmentInContainer<ButtonBankInfoRow>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { buttonBankInfoRow ->
            val textViewBankDataInfo =
                buttonBankInfoRow.view?.findViewById<TextView>(R.id.textViewBankDataInfo)
            val dividerLineEnd = buttonBankInfoRow.view?.findViewById<View>(R.id.dividerLineEnd)
            val containerButton =
                buttonBankInfoRow.view?.findViewById<LinearLayout>(R.id.containerButton)

            Assert.assertEquals("Test: 123456", textViewBankDataInfo?.text.toString())
            Assert.assertEquals(View.VISIBLE, textViewBankDataInfo?.visibility)
            Assert.assertEquals(View.VISIBLE, dividerLineEnd?.visibility)

            containerButton?.performClick()

            Assert.assertEquals("123456", clipboardManager?.text.toString())
        }
    }

    @Test
    fun `CASE 02, isShowDividerLine is false`() {
        val fragmentArgs = Bundle()
        fragmentArgs.putString("label", "Test")
        fragmentArgs.putString("value", "123456")
        fragmentArgs.putBoolean("isShowDividerLine", false)

        val fragment = launchFragmentInContainer<ButtonBankInfoRow>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { buttonBankInfoRow ->
            val dividerLineEnd = buttonBankInfoRow.view?.findViewById<View>(R.id.dividerLineEnd)
            Assert.assertEquals(View.GONE, dividerLineEnd?.visibility)
        }
    }

}