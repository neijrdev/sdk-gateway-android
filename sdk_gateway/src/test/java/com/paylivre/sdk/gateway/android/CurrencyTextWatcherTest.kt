package com.paylivre.sdk.gateway.android

import android.app.Application
import android.os.Build
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.CurrencyTextWatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class CurrencyTextWatcherTest {

    @Mock
    private var editText: EditText? = null
    private var context: Application? = null

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        editText = EditText(context)
        editText?.addTextChangedListener(CurrencyTextWatcher(editText!!))
    }

    @Test
    fun `Test CurrencyTextWatcher`() {
        editText?.setText("1000")
        Assert.assertEquals("R$ 10,00", editText!!.text.toString())
    }
}