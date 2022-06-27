package com.paylivre.sdk.gateway.android.ui.form

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.R
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

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class AcceptTermsTest {

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `test Fragment AcceptTerms`() {
        val mockDescription =
            "Ao clicar no botão CONTINUAR você aceita os nossos Termos de Uso e Termos de Privacidade."

        val fragmentArgs = Bundle().apply {
            putString("description", mockDescription)
        }
        val fragment = launchFragmentInContainer<AcceptTerms>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment() {
            val textViewTerms = it.view?.findViewById<TextView>(R.id.textViewTerms)
            Assert.assertEquals(mockDescription, textViewTerms?.text.toString())
        }
    }
}

