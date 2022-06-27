package com.paylivre.sdk.gateway.android

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.copyToClipboard
import com.paylivre.sdk.gateway.android.utils.showToast
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
import org.robolectric.shadows.ShadowToast


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class CopyClipboardTest {

    private var clipboardManager: ClipboardManager? = null

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Before
    @Throws(Exception::class)
    fun setUp() {
        clipboardManager = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Test
    fun `test copyToClipboard`() {
        val contextMock = ApplicationProvider.getApplicationContext<Context>()
        copyToClipboard(contextMock, "text to copy", "success", "failure")
        Assert.assertEquals("text to copy", clipboardManager?.text.toString())
        Assert.assertEquals("success", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun `test copyToClipboard error`() {
        val contextMock = ApplicationProvider.getApplicationContext<Context>()
        copyToClipboard(contextMock, null, "success", "failure")
    }

    @Test
    fun `test showToast`() {
        val contextMock = ApplicationProvider.getApplicationContext<Context>()
        showToast("show toast", contextMock)
        Assert.assertEquals("show toast", ShadowToast.getTextOfLatestToast())
    }




}