package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.ui.loading.LoadingScreenFragment
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
class SelectBankAccountFragmentTest {
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

    private fun getTextViewButtonBankField(
        viewScreenFragment: View?,
        containerButtonId: Int,
    ): TextView? {
        return viewScreenFragment?.findViewById<LinearLayout>(containerButtonId)
            ?.findViewById(R.id.textViewBankDataInfo)
    }


    @Test
    fun `CASE 01, SelectBankAccountFragment`() {

        val mockStatusTransactionResponse = getMockStatusTransactionResponseSuccess()
        val bankAccounts = mockStatusTransactionResponse.data?.bank_accounts
        val bankAccountsString = Gson().toJson(BankAccounts(bank_accounts = bankAccounts))
        val fragmentArgs = Bundle().apply {
            putString("bankAccounts", bankAccountsString)
        }

        val fragment = launchFragmentInContainer<SelectBankAccountFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { selectBankAccountFragment ->
            //GIVEN

            selectBankAccountFragment.mainViewModel.setStatusTransactionResponse(
                mockStatusTransactionResponse
            )


            //WHEN
            //Selected bank
            val mockBankPosition = 1
            val banksList = mockStatusTransactionResponse.data?.bank_accounts
            val selectedBankAccount = banksList?.elementAt(mockBankPosition)
            selectBankAccountFragment.setOnBankClick(mockBankPosition, banksList)

            //To run childFragmentManager transactions
            selectBankAccountFragment.childFragmentManager.executePendingTransactions()

            val bankNameText =
                getTextViewButtonBankField(selectBankAccountFragment.view, R.id.bankName)
            val bankOfficeText =
                getTextViewButtonBankField(selectBankAccountFragment.view, R.id.bankOffice)
            val bankAccountText =
                getTextViewButtonBankField(selectBankAccountFragment.view, R.id.bankAccount)
            val bankOwnerText =
                getTextViewButtonBankField(selectBankAccountFragment.view, R.id.bankOwner)
            val bankDocumentText =
                getTextViewButtonBankField(selectBankAccountFragment.view, R.id.bankDocument)

            //THEN
            Assert.assertEquals("Banco: 237 - Banco Bradesco S.A.", bankNameText?.text.toString())
            Assert.assertEquals("Agência: 6332", bankOfficeText?.text.toString())
            Assert.assertEquals("Conta: 9499-4", bankAccountText?.text.toString())
            Assert.assertEquals("Favorecido: PL BRASIL - CONSULTORIA EMPRESARIAL LTDA",
                bankOwnerText?.text.toString())
            Assert.assertEquals("CNPJ: 34.599.748/000.1-40", bankDocumentText?.text.toString())

        }
    }

    @Test
    fun `CASE 02, test clearAllFocus`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<SelectBankAccountFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { selectBankAccountFragment ->
            selectBankAccountFragment.mainViewModel.setClearAllFocus(true)
        }
    }
}