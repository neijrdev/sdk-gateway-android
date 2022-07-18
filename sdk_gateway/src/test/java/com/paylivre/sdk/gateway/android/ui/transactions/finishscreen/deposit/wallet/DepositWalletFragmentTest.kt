package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wallet

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.*
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.testutil.CheckBottomComponentsTestUtils
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class DepositWalletFragmentTest {

    private fun getMockStatusTransactionResponseSuccess(): StatusTransactionResponse {
        val responseCommonTransactionData =
            getResponseCommonTransactionDataByFile("response_deposit_wallet_success.json")

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
    fun `CASE 01, Check render components is DepositWalletFragment success`() {
        val depositInstances =
            createDepositInstances(
                statusTransactionResponse = getMockStatusTransactionResponseSuccess(),
                checkStatusDepositResponse = getMockCheckStatusDepositResponse(
                    transaction_status_id = null,
                    deposit_status_id = null),
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositWalletFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositWalletFragment ->
            //THENde
            checkHeaderDeposit(depositWalletFragment)

            //check Type deposit
            checkTextView(depositWalletFragment.view?.findViewById(R.id.TitlePix),
                "Carteira Paylivre")

            checkStatusDeposit(depositWalletFragment,
                "Completo!",
                "Operação finalizada com sucesso.",
                R.drawable.ic_completed)

            //transaction data
            checkTransactionData(depositWalletFragment,
                "58665", "R$ 5,00", "R$ 0,00", "",
                "R$ 5,00", "R$ 99.999.988.070,89"
            )

            //check terms
            checkTerms(depositWalletFragment)

            //check button back to merchant
            checkButtonBackToMerchant(depositWalletFragment)

            //check instructions deposit wallet
            checkTextView(depositWalletFragment.view?.findViewById(R.id.textViewTitleInstructionsWallet),
                "Instruções de Saldo Paylivre")
            checkTextView(depositWalletFragment.view?.findViewById(R.id.textInstructionWallet1),
                "Para essa transação foi utilizado o saldo da carteira Paylivre!")

            //Bottom components
            CheckBottomComponentsTestUtils(depositWalletFragment.view)

            verify {
                depositInstances.mockMainViewModel.checkStatusTransaction(58665)
            }

        }
    }

    @Test
    fun `CASE 02, checkStatusTransactionLoading is true`() {
        //GIVEN
        val depositInstances =
            createDepositInstances(
                checkStatusTransactionLoading = true
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositWalletFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositWalletFragment ->
            //THEN
            Assert.assertEquals(
                View.VISIBLE,
                depositWalletFragment.view?.findViewById<LinearLayout>(R.id.containerLoadingStatusDeposit)
                    ?.visibility
            )

            Assert.assertEquals(
                View.GONE,
                depositWalletFragment.view?.findViewById<FragmentContainerView>(R.id.fragmentDepositStatus)
                    ?.visibility
            )
        }
    }

    @Test
    fun `CASE 03, data in checkStatusTransactionResponse is null`() {
        //GIVEN
        val depositInstances =
            createDepositInstances(
                checkStatusTransactionResponse = CheckStatusTransactionResponse(
                    status = "",
                    status_code = null,
                    message = null,
                    data = null
                )
            )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<DepositWalletFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { depositWalletFragment ->
            //THEN
            Assert.assertEquals(
                View.GONE,
                depositWalletFragment.view?.findViewById<LinearLayout>(R.id.containerLoadingStatusDeposit)
                    ?.visibility
            )

            Assert.assertEquals(
                View.VISIBLE,
                depositWalletFragment.view
                    ?.findViewById<ConstraintLayout>(R.id.containerBackMerchantAndInstructions)
                    ?.visibility
            )
        }
    }
}