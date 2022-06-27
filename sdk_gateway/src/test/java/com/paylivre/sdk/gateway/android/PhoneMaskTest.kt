package com.paylivre.sdk.gateway.android

import android.R
import android.app.Application
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.MaskPhoneUtil
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class PhoneMaskTest {

    @Mock
    private var editText: EditText? = null
    private var context: Application? = null

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)

        val attributeSet: AttributeSet = Robolectric.buildAttributeSet()
            .addAttribute(R.attr.maxLength, "20")
            .build()
        context = ApplicationProvider.getApplicationContext()
        editText = EditText(context, attributeSet)
        editText?.addTextChangedListener(MaskPhoneUtil.insert(editText!!))
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `Test MaskPhoneUtil with 8 numbers`() {
        editText?.setText("7399999999")
        Assert.assertEquals("(73) 9999-9999", editText!!.text.toString())
    }

    @Test
    fun `Test MaskPhoneUtil with 9 numbers`() {
        editText?.setText("73999999999")
        Assert.assertEquals("(73) 99999-9999", editText!!.text.toString())
    }


}