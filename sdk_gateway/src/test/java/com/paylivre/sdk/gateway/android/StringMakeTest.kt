package com.paylivre.sdk.gateway.android

import android.app.Application
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.makeLinks
import com.paylivre.sdk.gateway.android.utils.setTextBackground
import com.paylivre.sdk.gateway.android.utils.setTextWithSpan
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class OpenUrl {
    fun open(url: String? = null){

    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class StringMakeTest {


    @Mock
    private var textView: TextView? = null
    private var context: Application? = null

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
        context = ApplicationProvider.getApplicationContext()
        textView = TextView(context)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test makeLinks`() {
        var openUrl: OpenUrl? = mockk()

        textView?.text = "Link1 test Link2"
        textView?.makeLinks(
            Pair("Link1", View.OnClickListener {
                openUrl?.open("https://paylivre.test.com1")
            }),
            Pair("Link2", View.OnClickListener {
                openUrl?.open("https://paylivre.test.com2")
            }),
        )

        textView?.callOnClick()

        Assert.assertEquals("Link1 test Link2", textView?.text.toString())

    }

    @Test
    fun `test setTextWithSpan`() {
        setTextWithSpan(textView!!, "All text ir here in SpanText", "SpanText", null)
        Assert.assertEquals("All text ir here in SpanText", textView?.text.toString())
    }

    @Test
    fun `test setTextBackground`() {
        val contextMock = ApplicationProvider.getApplicationContext<Context>()
        setTextBackground(
            contextMock,
            textView!!, "All text ir here in SpanText", "SpanText", R.color.dark_color_paylivre_sdk)
        Assert.assertEquals("All text ir here in SpanText", textView?.text.toString())
    }


}