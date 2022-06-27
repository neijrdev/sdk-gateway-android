package com.paylivre.sdk.gateway.android

import android.app.Application
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.DataMakeBold
import com.paylivre.sdk.gateway.android.utils.makeBold
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class TestViewSetStylesTest {
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
    fun `test makeBold`() {
        textView?.text = "Link1"

        textView?.makeBold(
            DataMakeBold(
               "Link1",
                "Link2",
            )
        )

        Assert.assertEquals("Link1", textView?.text.toString())
    }


}