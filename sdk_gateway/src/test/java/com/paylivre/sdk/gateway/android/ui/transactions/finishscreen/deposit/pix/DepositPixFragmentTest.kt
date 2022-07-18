package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.pix

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.domain.model.DepositStatus
import com.paylivre.sdk.gateway.android.services.countdowntimer.MockCountDownTimerGivenHelper
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.*
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet.DepositBilletFragment
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.testutil.CheckBottomComponentsTestUtils
import io.mockk.coEvery
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class DepositPixFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()

    private var clipboardManager: ClipboardManager? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        clipboardManager = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private fun getMockStatusTransactionResponseSuccess(): StatusTransactionResponse {
        val responseCommonTransactionData =
            getResponseCommonTransactionDataByFile("response_deposit_pix_success.json")

        return StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = responseCommonTransactionData,
            error = null
        )
    }

    private fun getMockCheckStatusDepositResponse(
        transaction_status_id: Int? = null,
        deposit_status_id: Int? = 0,
    ): CheckStatusDepositResponse {
        return CheckStatusDepositResponse("success",
            status_code = 200,
            "ok",
            data = DataStatusDeposit(
                transaction_status_id = transaction_status_id,
                deposit_status_id = deposit_status_id
            ))
    }

    @Test
    fun `CASE 01, Check render components is DepositPixFragment success and the countdown started`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = null),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnTick(1572231)

            //THEN
            checkHeaderDeposit(depositPixFragment)

            //check Type deposit
            checkTextView(depositPixFragment.view?.findViewById(R.id.TitlePix), "PIX")

            //check text code pix
            Assert.assertEquals(depositPixFragment.view?.findViewById<TextView>(R.id.textCodePix)?.text.toString(),
                "00020101021226580014BR.GOV.BCB.PIX0136b46359c8-3eaa-4b42-a3c6-0972a9c4a2265204000053039865406111.115802BR5912Pay Livrepix6014Belo Horizonte610830380403620605029X6304BFA4"
            )

            //check button copy code pix
            val btnCopyCodePix = depositPixFragment.view?.findViewById<Button>(R.id.btnCopyCodePix)
            Assert.assertEquals(View.VISIBLE, btnCopyCodePix?.visibility)
            Assert.assertEquals("Copiar Chave Pix", btnCopyCodePix?.text)
            btnCopyCodePix?.isEnabled?.let { Assert.assertTrue(it) }

            //check text timer to expires
            checkTextView(depositPixFragment.view?.findViewById(R.id.textViewLabelTimePix),
                "Esta chave PIX expira em:")
            checkTextView(depositPixFragment.view?.findViewById(R.id.textViewTimeExpirePix),
                "26:12")

            //check status last check deposit status
            checkTextView(depositPixFragment.view?.findViewById(R.id.textLastStatusDepositPix), "")

            //deposit status is not visible
            Assert.assertEquals(View.GONE,
                depositPixFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)?.visibility)

            //transaction data
            checkTransactionData(depositPixFragment,
                "14371", "R$ 5,00", "R$ 0,00", "",
                "R$ 5,00", "R$ 99.999.988.799,66"
            )

            //check terms
            checkTerms(depositPixFragment)

            //check button back to merchant
            checkButtonBackToMerchant(depositPixFragment)

            //check instructions deposit PIX
            checkTextView(depositPixFragment.view?.findViewById(R.id.textViewTitleInstructionsPix),
                "Instruções para fazer PIX")
            checkTextView(depositPixFragment.view?.findViewById(R.id.textInstructionPix1),
                "Para pagar click no botão \"Copiar Chave Pix\", abra o aplicativo do seu banco e procure a opção de pagamento através do Pix e cole a chave PIX no campo referente.")
            checkDrawable(
                depositPixFragment,
                R.id.iconSmartphone,
                R.drawable.icon_smartphone)

            //Bottom components
            CheckBottomComponentsTestUtils(depositPixFragment.view)
        }
    }

    @Test
    fun `CASE 02, Test to copy pix code when clicking on the Copy Code PIX button`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = null),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            val btnCopyCodePix = depositPixFragment.view?.findViewById<Button>(R.id.btnCopyCodePix)

            //WHEN
            btnCopyCodePix?.performClick()

            //THEN
            Assert.assertEquals("00020101021226580014BR.GOV.BCB.PIX0136b46359c8-3eaa-4b42-a3c6-0972a9c4a2265204000053039865406111.115802BR5912Pay Livrepix6014Belo Horizonte610830380403620605029X6304BFA4",
                clipboardManager?.text.toString())
            Assert.assertEquals("Código PIX copiado.", ShadowToast.getTextOfLatestToast())
            verify {
                depositInstances.logEventsServiceImpl.setLogEventAnalytics("Btn_CopyCodePix")
            }
        }
    }

    @Test
    fun `CASE 03, Test to copy pix code when clicking on the Text Code PIX button`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = null),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            val textCodePix = depositPixFragment.view?.findViewById<TextView>(R.id.textCodePix)

            //WHEN
            textCodePix?.performClick()

            //THEN
            Assert.assertEquals("00020101021226580014BR.GOV.BCB.PIX0136b46359c8-3eaa-4b42-a3c6-0972a9c4a2265204000053039865406111.115802BR5912Pay Livrepix6014Belo Horizonte610830380403620605029X6304BFA4",
                clipboardManager?.text.toString())
            Assert.assertEquals("Código PIX copiado.", ShadowToast.getTextOfLatestToast())
            verify {
                depositInstances.logEventsServiceImpl.setLogEventAnalytics("Click_Text_CopyCodePix")
            }

        }
    }

    @Test
    fun `CASE 04, given dispatchCheckStatusDepositPix`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = null),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnTick(60000)

            //THEN
            checkTextView(
                depositPixFragment.view?.findViewById(R.id.textViewLabelTimePix),
                "Esta chave PIX expira em:")
            checkTextView(
                depositPixFragment.view?.findViewById(R.id.textViewTimeExpirePix),
                "01:00")
            verify {
                depositInstances.mockMainViewModel.checkStatusDeposit(14371)
            }
        }
    }

    @Test
    fun `CASE 05, given finished countDownTimer to expire PIX Code`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = 0),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->

            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnFinish()

            //To run childFragmentManager transactions
            depositPixFragment.childFragmentManager.executePendingTransactions()

            //THEN
            Assert.assertTrue(mockCountDownTimerGivenHelper.calledCancel)

            Assert.assertEquals(View.GONE,
                depositPixFragment.view?.findViewById<ConstraintLayout>(R.id.containerCodePixAndCheckStatus)?.visibility)

            Assert.assertEquals(View.VISIBLE,
                depositPixFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)?.visibility)

            checkStatusDeposit(depositPixFragment,
                "Novo!",
                "Aguarde o processamento da operação.",
                R.drawable.ic_new)

        }

    }

    @Test
    fun `CASE 06, gcheck log analytics when deposit status is COMPLETED`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = DepositStatus.COMPLETED.code),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnFinish()

            verify {
                depositInstances.logEventsServiceImpl.setLogEventAnalyticsWithParams("TransactionStatus",
                    Pair("type_status", "deposit_status_id"),
                    Pair("status_id", "2"))
            }
        }
    }

    @Test
    fun `CASE 07, check log analytics when deposit status is CANCELLED`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = DepositStatus.CANCELLED.code),
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnFinish()

            verify {
                depositInstances.logEventsServiceImpl.setLogEventAnalyticsWithParams("TransactionStatus",
                    Pair("type_status", "deposit_status_id"),
                    Pair("status_id", "3"))
            }
        }
    }

    @Test
    fun `CASE 08, given checkStatusDepositLoading is true`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = null,
                mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper,
                checkStatusDepositLoading = true
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositPixFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //THEN
            Assert.assertEquals(View.GONE,
                depositPixFragment.view?.findViewById<TextView>(R.id.textLastStatusDepositPix)?.visibility)

            //Check is show loading components
            Assert.assertEquals(View.VISIBLE,
                depositPixFragment.view?.findViewById<LinearLayout>(R.id.containerCheckingStatus)?.visibility)
            checkTextView(depositPixFragment.view?.findViewById(R.id.textViewMessageLoading),
                "Verificando pagamento...")
            Assert.assertEquals(View.VISIBLE,
                depositPixFragment.view?.findViewById<CircularProgressIndicator>(R.id.loadingBar)?.visibility)
        }
    }

    @Test
    fun `CASE 09, when the fragment state to Destroyed`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()
        createDepositInstances(
            mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper,
        )

        val fragment = launchFragmentInContainer<DepositPixFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositPixFragment ->
            //WHEN
            depositPixFragment.onDestroy()

            //THEN
            Assert.assertTrue(mockCountDownTimerGivenHelper.calledCancel)
        }
    }


}