package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class SelectOriginImportProofFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `CASE 01, test SelectOriginImportProofFragment`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<SelectOriginImportProofFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { selectOriginImportProofFragment ->
            val view = selectOriginImportProofFragment.view
            val btnOpenGallery = view?.findViewById<Button>(R.id.btnOpenGallery)
            val btnOpenCamera = view?.findViewById<Button>(R.id.btnOpenCamera)
            val btnCloseModal = view?.findViewById<Button>(R.id.btnCloseModal)
            val txtTitleSelectImportProof = view?.findViewById<TextView>(R.id.txtTitleSelectImportProof)

            btnOpenGallery?.performClick()
            btnOpenCamera?.performClick()

            Assert.assertEquals(
                "Selecione uma das opções abaixo",
                txtTitleSelectImportProof?.text.toString()
            )
            Assert.assertEquals(
                "Tira foto",
                btnOpenCamera?.text.toString()
            )
            Assert.assertEquals(
                "Escolher na galeria",
                btnOpenGallery?.text.toString()
            )
            Assert.assertEquals(
                "Fechar",
                btnCloseModal?.text.toString()
            )
        }
    }
}