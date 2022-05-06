package com.paylivre.sdk.gateway.android

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.data.model.order.BankAccount
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers="pt-port")
class GetBankAccountsDataTest {
    private val mockBankAccounts = listOf(
        BankAccount(
            "Teste Account Hidden",
            null, null,
            1, null, null, null,
            null
        ),
        BankAccount(
            "Teste Account Not Hidden",
            null, null,
            null, null, null, null,
            null
        )
    )

    @Test
    fun `Test function getEnabledBanksAccounts`() {
        Assert.assertEquals(
            listOf(
                BankAccount(
                    "Teste Account Not Hidden",
                    null, null,
                    null, null, null, null,
                    null
                )
            ),
            getEnabledBanksAccounts(
                BankAccounts(
                    bank_accounts = mockBankAccounts
                )
            )
        )
    }

    @Test
    fun `Test function getNameBanksAccountsList`() {
        Assert.assertEquals(
            listOf("Teste Account Hidden", "Teste Account Not Hidden"),
            getNameBanksAccountsList(mockBankAccounts)
        )
    }


    @Test
    fun `Test getBankOffice`() {
        Assert.assertEquals(
            "0010",
            getBankOffice(10, null)
        )

        Assert.assertEquals(
            "0001",
            getBankOffice(1, null)
        )

        Assert.assertEquals(
            "0000",
            getBankOffice(0, null)
        )


        Assert.assertEquals(
            "0000-1",
            getBankOffice(0, 1)
        )
    }


    @Test
    fun `Test getBankAccountNumber`() {
        Assert.assertEquals(
            "10",
            getBankAccountNumber("10", null)
        )

        Assert.assertEquals(
            "10-1",
            getBankAccountNumber("10", "1")
        )

        Assert.assertEquals(
            "10-10",
            getBankAccountNumber("10", "10")
        )
    }


    @Test
    fun `Test getBankAccountInfo`() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val mockBankAccount = BankAccount(
            "Teste Account Hidden",
            "Joao Teste da Silva", "12.345.678/912.1-43",
            null, 1234, null, "12346",
            "7"
        )

        Assert.assertEquals(
            "Banco: Teste Account Hidden\n" +
                    "Agência: 1234\n" +
                    "Conta: 12346-7\n" +
                    "Joao Teste da Silva\n" +
                    "12.345.678/912.1-43",
            getBankAccountInfo(context, mockBankAccount)
        )
    }


}