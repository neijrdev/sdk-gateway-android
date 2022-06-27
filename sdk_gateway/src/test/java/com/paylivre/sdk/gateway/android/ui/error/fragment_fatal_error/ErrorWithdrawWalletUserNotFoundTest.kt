package com.paylivre.sdk.gateway.android.ui.error.fragment_fatal_error

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.ui.error.FragmentFatalError
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
class ErrorWithdrawWalletUserNotFoundTest {

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
    fun `FragmentFatalError, case - Withdraw Wallet, request error, User not found`(){
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FragmentFatalError>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)
        fragment.onFragment { fragmentFatalError ->
            //GIVEN
            fragmentFatalError.mainViewModel.setLogoUrl("https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true")
            fragmentFatalError.mainViewModel.setLogoResId(R.drawable.ic_logo_paylivre_blue)
            fragmentFatalError.mainViewModel.setIsFatalError(
                isError = true,
                messageError = "invalid_data_error"
            )
            fragmentFatalError.mainViewModel.setMessageDetailsError("user_not_found_with_document_number")

            //THEN
            val textTitleError =
                fragmentFatalError.view?.findViewById<TextView>(R.id.TextViewError)

            val textSubtitleError =
                fragmentFatalError.view?.findViewById<TextView>(R.id.TextViewMsgSubtitleError)

            val textSubtitleError3 =
                fragmentFatalError.view?.findViewById<TextView>(R.id.textViewMsgSubtitle3Error)

            //THEN
            Assert.assertEquals(
                "Erro",
                textTitleError?.text.toString()
            )

            Assert.assertEquals(
                "Erro na verificação dos dados informados.",
                textSubtitleError?.text.toString()
            )

            Assert.assertEquals(
                "Não existe uma conta cadastrada com esse documento na Paylivre.\n" +
                        "\n" +
                        "O Saque não pode ser processado.\n" +
                        "\n" +
                        "Para maiores informações cadastre-se em web.paylivre.com.",
                textSubtitleError3?.text.toString()
            )
        }
    }

}