package com.paylivre.sdk.gateway.android.ui.error.fragment_fatal_error

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.ui.error.ErrorKycLimitFragment
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
class ErrorKycLimitFragmentTest {
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
    fun `Test render components`() {

        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { fragmentTest ->
            //GIVEN
            val textViewError = fragmentTest.view?.findViewById<TextView>(R.id.TextViewError)
            val textViewLimits = fragmentTest.view?.findViewById<TextView>(R.id.textViewLimits)
            val btnIncreaseLimit = fragmentTest.view?.findViewById<Button>(R.id.btnIncreaseLimit)
            val textViewMsgSubtitle3Error =
                fragmentTest.view?.findViewById<TextView>(R.id.textViewMsgSubtitle3Error)
            val textViewLinkPageSupportHelp =
                fragmentTest.view?.findViewById<TextView>(R.id.linkPageSupportHelp)
            val btnClose = fragmentTest.view?.findViewById<TextView>(R.id.btnClose)


            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "0",
                        limit = 2000000,
                        used_limit = 1012300,
                        available_limit = 987700
                    ))
            )


            //THEN
            Assert.assertEquals(
                "Veja o quanto você já usou do seu limite mensal.",
                textViewError?.text.toString()
            )
            Assert.assertEquals(
                "Limite total: R\$ 20.000,00\n" +
                        "Limite utilizado: R\$ 10.123,00\n" +
                        "Limite disponível: R\$ 9.877,00",
                textViewLimits?.text.toString()
            )
            Assert.assertEquals(
                "Quero aumentar meu limite na Paylivre",
                btnIncreaseLimit?.text.toString()
            )
            Assert.assertEquals(
                "Para saber mais sobre limites, indicamos o artigo:",
                textViewMsgSubtitle3Error?.text.toString()
            )
            Assert.assertEquals(
                "Quais são os limites?",
                textViewLinkPageSupportHelp?.text.toString()
            )
            Assert.assertEquals(
                "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-",
                fragmentTest?.urlPageSupportLimits
            )
            Assert.assertEquals(
                "Fechar",
                btnClose?.text.toString()
            )

        }

    }

    @Test
    fun `CASE 01, key_level = 0`() {
        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentTest ->
            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "0",
                    ))
            )

            //THEN
            Assert.assertEquals(
                "https://web.paylivre.com/signup",
                fragmentTest?.urlToOpen
            )
        }
    }

    @Test
    fun `CASE 02, key_level = 1`() {
        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentTest ->
            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "1",
                    ))
            )

            //THEN
            Assert.assertEquals(
                "https://web.paylivre.com/signup",
                fragmentTest?.urlToOpen
            )
        }
    }

    @Test
    fun `CASE 03, key_level = 2`() {
        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentTest ->
            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "2",
                    ))
            )

            //THEN
            Assert.assertEquals(
                "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-",
                fragmentTest?.urlToOpen
            )
        }
    }

    fun `CASE 04, key_level = 3`() {
        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentTest ->
            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "3",
                    ))
            )

            //THEN
            Assert.assertEquals(
                "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-",
                fragmentTest?.urlToOpen
            )
        }
    }

    fun `CASE 04, key_level = 4`() {
        val fragment = launchFragmentInContainer<ErrorKycLimitFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentTest ->
            //WHEN
            fragmentTest.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = "User Kyc Limit exceeded",
                    errors = Errors(
                        kyc_level = "4",
                    ))
            )

            //THEN
            Assert.assertEquals(
                "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-",
                fragmentTest?.urlToOpen
            )
        }
    }
}