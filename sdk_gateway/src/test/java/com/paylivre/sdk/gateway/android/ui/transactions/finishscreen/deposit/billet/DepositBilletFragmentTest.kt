package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.*
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.testutil.CheckBottomComponentsTestUtils
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class DepositBilletFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()

    private fun getMockStatusTransactionResponseSuccess(): StatusTransactionResponse {
        val responseCommonTransactionData =
            getResponseCommonTransactionDataByFile("res_deposit_billet_success.json")
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
    fun `CASE 01, Check render components is DepositBilletFragment success`() {
        //GIVEN
        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = 0)
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositBilletFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositBilletFragment ->
            //THEN
            checkHeaderDeposit(depositBilletFragment)
            checkStatusDeposit(depositBilletFragment,
                "Novo!",
                "Aguarde o processamento da operação.",
                R.drawable.ic_new
            )
            checkTransactionData(depositBilletFragment,
                "14311", "R$ 5,00", "R$ 3,50", "14/07/2022",
                "R$ 8,50", "R$ 99.999.989.118,66"
            )
            checkTerms(depositBilletFragment)
            checkButtonBackToMerchant(depositBilletFragment)

            verify {
                depositInstances.logEventsServiceImpl.setLogFinishScreen(Operation.DEPOSIT,
                    Types.BILLET)
            }

            //container billet code bar
            val txtCode = depositBilletFragment.view?.findViewById<TextView>(R.id.txtCode)
            Assert.assertEquals(txtCode?.text.toString(),
                "34191790010104351004791020150008887870026000")

            val btnCopyCodeBillet =
                depositBilletFragment.view?.findViewById<Button>(R.id.btnCopyCodeBillet)
            val btnOpenBillet = depositBilletFragment.view?.findViewById<Button>(R.id.btnOpenBillet)
            Assert.assertEquals(btnCopyCodeBillet?.text.toString(), "Copiar código")
            Assert.assertEquals(btnCopyCodeBillet?.visibility, View.VISIBLE)
            Assert.assertEquals(btnCopyCodeBillet?.isEnabled, true)
            Assert.assertEquals(btnOpenBillet?.text.toString(), "Salvar boleto em PDF")
            Assert.assertEquals(btnOpenBillet?.visibility, View.VISIBLE)
            Assert.assertEquals(btnOpenBillet?.isEnabled, true)

            val txtTip = depositBilletFragment.view?.findViewById<TextView>(R.id.txtTip)
            checkTextView(txtTip,
                "Dica: Pagar seu boleto até às 21h de dias úteis faz com que o pagamento seja confirmado mais rápido.")


            //check instructions
            checkTextView(depositBilletFragment.view?.findViewById(R.id.textViewTitleInstructions),
                "Atente-se aos detalhes")
            checkTextView(depositBilletFragment.view?.findViewById(R.id.textInstructionPix1),
                "Boleto (somente à vista)")
            checkTextView(depositBilletFragment.view?.findViewById(R.id.textInstructionPix2),
                "Pagamento com o boleto bancário levam até 3 dias úteis para serem compensados e então terem os produtos liberados")
            checkTextView(depositBilletFragment.view?.findViewById(R.id.textInstructionPix3),
                "Atente-se ao vencimento do boleto. Você pode pagar o boleto em qualquer banco ou casa lotérica até o dia do vencimento")

            //Bottom components
            CheckBottomComponentsTestUtils(depositBilletFragment.view)
        }
    }

    @Test
    fun `CASE 02, given the loading of the request to check the status is as true`() {
        //GIVEN
        val logEventsServiceImpl = mockk<LogEventsService>(relaxed = true)
        val mockMainViewModel = MockMainViewModel()

        loadKoinModules(module {
            single {
                logEventsServiceImpl
            }
            viewModel {
                mockMainViewModel.mainViewModel
            }
        })

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositBilletFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositBilletFragment ->
            //WHEN
            mockMainViewModel.mainViewModel.setCheckStatusDepositLoading(true)
            //To run childFragmentManager transactions
            depositBilletFragment.childFragmentManager.executePendingTransactions()

            //THEN
            val containerLoadingStatusDeposit =
                depositBilletFragment.view?.findViewById<LinearLayout>(R.id.containerLoadingStatusDeposit)
            val fragmentDepositStatus =
                depositBilletFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)

            Assert.assertEquals(View.VISIBLE, containerLoadingStatusDeposit?.visibility)
            Assert.assertEquals(View.GONE, fragmentDepositStatus?.visibility)
        }
    }
}