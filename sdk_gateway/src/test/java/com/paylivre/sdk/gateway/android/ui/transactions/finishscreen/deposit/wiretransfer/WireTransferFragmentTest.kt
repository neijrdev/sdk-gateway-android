package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.domain.model.DepositStatus
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.testutil.CheckBottomComponentsTestUtils
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.RuntimeException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class WireTransferFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()


    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    private fun getMockStatusTransactionResponseSuccess(): StatusTransactionResponse {
        val responseExpectedString =
            fileTestsUtils.loadJsonAsString("res_deposit_wiretransfer_success.json")

        val expectedDataResponse = Gson().fromJson(
            responseExpectedString, ResponseCommonTransactionData::class.java
        )

        return StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = expectedDataResponse,
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
    fun `Test Show render components, before select bank account`() {

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        //GIVEN
        val mockStatusTransactionResponse = getMockStatusTransactionResponseSuccess()
        val mockCheckStatusDepositResponse = getMockCheckStatusDepositResponse()


        fragment.onFragment { wireTransferFragment ->
            //WHEN
            wireTransferFragment.mainViewModel.setStatusTransactionResponse(
                mockStatusTransactionResponse
            )

            wireTransferFragment.mainViewModel.checkStatusDepositSuccess(
                mockCheckStatusDepositResponse
            )

            //To run childFragmentManager transactions
            wireTransferFragment.childFragmentManager.executePendingTransactions()

            //THEN
            //Title
            Assert.assertEquals(
                "Transferência Bancária",
                wireTransferFragment.view?.findViewById<TextView>(R.id.TitlePix)?.text.toString())

            //Status Deposit
            Assert.assertEquals(
                "Novo!",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewTitle)?.text.toString())
            Assert.assertEquals(
                "Aguarde o processamento da operação.",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewBody)?.text.toString())
            Assert.assertEquals(
                View.VISIBLE,
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)?.visibility)


            //Data Deposit
            Assert.assertEquals(
                "13441",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textIdValue)?.text.toString())
            Assert.assertEquals(
                "R$ 5,00",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textOriginalAmountValue)?.text.toString())
            Assert.assertEquals(
                "R$ 0,00",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textTaxAmountValue)?.text.toString())
            Assert.assertEquals(
                "R$ 5,00",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textTotalAmountValue)?.text.toString())
            Assert.assertEquals(
                "R$ 99.999.999.735,88",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textLimitAmountValue)?.text.toString())


            //Select Bank Account without having selected any items
            Assert.assertEquals(
                "Informações bancárias Paylivre",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textTitle)?.text.toString())
            Assert.assertEquals(
                "Selecionar...",
                wireTransferFragment.view?.findViewById<AutoCompleteTextView>(R.id.editSpinnerBanks)?.text.toString())


            //Terms
            Assert.assertEquals(
                "Ao solicitar uma transação você aceita os nossos Termos de Uso e Termos de Privacidade.",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewTerms)?.text.toString())


            //Button BackToMerchant
            Assert.assertEquals(
                "Retornar para o merchant",
                wireTransferFragment.view?.findViewById<Button>(R.id.btnBackAppMerchant)?.text.toString())


            //Instructions
            Assert.assertEquals(
                "Instruções de Transferência",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewTitleInstructions)?.text.toString())
            Assert.assertEquals(
                wireTransferFragment.getString(R.string.deposit_transfer_instructions_1),
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewInstruction1)?.text.toString())
            Assert.assertEquals(
                wireTransferFragment.getString(R.string.deposit_transfer_instructions_2),
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewInstruction2)?.text.toString())
            Assert.assertEquals(
                wireTransferFragment.getString(R.string.deposit_transfer_instructions_3),
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewInstruction3)?.text.toString())
            Assert.assertEquals(
                wireTransferFragment.getString(R.string.deposit_transfer_instructions_5),
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewInstruction4)?.text.toString())

            //Check bottom components
            val checkBottomComponentsTestUtils =
                CheckBottomComponentsTestUtils(wireTransferFragment.view)
            checkBottomComponentsTestUtils.assertCheckData()
        }
    }

    @Test
    fun `CASE 01, in WireTransferFragment given selectedBankAccountWireTransfer with invalid bank`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->
            val containerInsertProof =
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.containerInsertProof)

            //WHEN -> setSelectedBankAccountWireTransfer given invalid bank
            wireTransferFragment.mainViewModel.setSelectedBankAccountWireTransfer(null)
            //THEN
            Assert.assertEquals(View.GONE, containerInsertProof?.visibility)
        }
    }

    @Test
    fun `CASE 02, selected bank, but did not insert the proof`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->
            //GIVEN
            val mockStatusTransactionResponse = getMockStatusTransactionResponseSuccess()
            wireTransferFragment.mainViewModel.setStatusTransactionResponse(
                mockStatusTransactionResponse
            )
            //WHEN
            //Selected bank
            val mockBankPosition = 0
            val banksList = mockStatusTransactionResponse.data?.bank_accounts
            val selectedBankItem = banksList?.elementAt(mockBankPosition)
            val containerInsertProof =
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.containerInsertProof)


            //WHEN -> setSelectedBankAccountWireTransfer given valid bank
            wireTransferFragment.mainViewModel.setSelectedBankAccountWireTransfer(
                selectedBankItem
            )
            //THEN
            //Check data containerInsertProof
            Assert.assertEquals(View.VISIBLE, containerInsertProof?.visibility)
            Assert.assertEquals("Comprovante de depósito",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textTitleDepositProof)?.text.toString())
            Assert.assertEquals("Nenhum arquivo selecionado",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textNameSelectedFile)?.text.toString())
            Assert.assertEquals(View.VISIBLE,
                wireTransferFragment.view?.findViewById<ConstraintLayout>(R.id.btnChooseFile)?.visibility)
            Assert.assertEquals("Escolher arquivo",
                wireTransferFragment.view?.findViewById<TextView>(R.id.txtChooseFile)?.text.toString())
            Assert.assertEquals("O arquivo deve ser do tipo: jpg, png, jpeg e tamanho máximo de 5MB.",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textInstructionFile)?.text.toString())

            //Button submit
            Assert.assertEquals(View.VISIBLE,
                wireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.visibility)
            Assert.assertEquals(false,
                wireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.isEnabled)
            Assert.assertEquals("Enviar",
                wireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.text.toString())
        }
    }

    @Test
    fun `CASE 03, selected bank, and inserted the proof is success`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->

            wireTransferFragment.mainViewModel.insertTransferProofSuccess(
                InsertTransferProofDataResponse(
                    id = 123456,
                    proof = null,
                    wallet_id = 1,
                    user_id = 2,
                    deposit_status_id = DepositStatus.PENDING.code,
                )
            )


            //To run childFragmentManager transactions
            wireTransferFragment.childFragmentManager.executePendingTransactions()

            //Status Deposit
            Assert.assertEquals(
                "Pendente!",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewTitle)?.text.toString())
            Assert.assertEquals(
                "Aguardando confirmação de pagamento.",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textViewBody)?.text.toString())
            Assert.assertEquals(
                View.VISIBLE,
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)?.visibility)

        }
    }

    @Test
    fun `CASE 04, selected bank, and inserted the proof is failure`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->

            wireTransferFragment.mainViewModel.insertTransferProofFailure(
                RuntimeException("error_request_not_connect_server")
            )

            //To run childFragmentManager transactions
            wireTransferFragment.childFragmentManager.executePendingTransactions()

            //Status Deposit
            Assert.assertEquals(
                "Erro no upload do comprovante, por favor, certifique-se de estar enviando o arquivo no formato correto.",
                wireTransferFragment.view?.findViewById<TextView>(R.id.textErrorUploadProofFile)?.text.toString())

        }
    }

    @Test
    fun `CASE 05, selected bank, and inserted the proof is loading`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->

            //WHEN
            wireTransferFragment.mainViewModel.insertTransferProofLoading(true)

            //To run childFragmentManager transactions
            wireTransferFragment.childFragmentManager.executePendingTransactions()

            //THEN
            Assert.assertEquals(
                View.VISIBLE,
                wireTransferFragment.view?.findViewById<LinearLayout>(R.id.containerLoadingStatusDeposit)?.visibility)

            Assert.assertEquals(
                View.GONE,
                wireTransferFragment.view?.findViewById<TextView>(R.id.textErrorUploadProofFile)?.visibility)

            Assert.assertEquals(
                View.GONE,
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.containerInsertProof)?.visibility)

            Assert.assertEquals(
                View.GONE,
                wireTransferFragment.view?.findViewById<ConstraintLayout>(R.id.containerSelectBank)?.visibility)

            Assert.assertEquals(
                View.GONE,
                wireTransferFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)?.visibility)

        }
    }

    @Test
    fun `Test ClearFocus selectBank`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { wireTransferFragment ->
            val container =
                wireTransferFragment.view?.findViewById<ConstraintLayout>(R.id.container)
            val cardView = wireTransferFragment.view?.findViewById<CardView>(R.id.CardView)

            container?.performClick()
            cardView?.performClick()

            Assert.assertEquals(true,
                wireTransferFragment.mainViewModel.clearAllFocus.getOrAwaitValueTest())

        }
    }

}