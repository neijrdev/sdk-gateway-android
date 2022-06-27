package com.paylivre.sdk.gateway.android

import android.R
import android.app.Application
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.MaskDocumentUtil
import com.paylivre.sdk.gateway.android.utils.MaskType
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
class DocumentMaskTest {
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
        editText?.addTextChangedListener(MaskDocumentUtil.insert(editText!!, null))
    }


    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `Test DocumentMask with cpf mask`() {
        editText?.setText("12345612345")

        Assert.assertEquals("123.456.123-45", editText!!.text.toString())
    }

    @Test
    fun `Test DocumentMask with cpf mask and MaskType CPF`() {
        editText?.setText("12345612345")
        editText?.addTextChangedListener(MaskDocumentUtil.insert(editText!!, MaskType.CPF))
        Assert.assertEquals("123.456.123-45", editText!!.text.toString())
    }

    @Test
    fun `Test DocumentMask with cnpj mask`() {
        editText?.setText("61311396000182")
        editText?.addTextChangedListener(MaskDocumentUtil.insert(editText!!, MaskType.CNPJ))
        Assert.assertEquals("61.311.396/0001-82", editText!!.text.toString())
    }

}