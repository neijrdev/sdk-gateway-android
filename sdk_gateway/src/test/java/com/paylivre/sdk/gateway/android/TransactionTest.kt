package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.data.model.order.getDataWithOnlySelectedType
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.domain.model.TypesToSelect
import org.junit.Assert
import org.junit.Test

class TransactionTest {
    @Test
    fun `Valid getDataWithOnlySelectedType Only Type Pix`() {
        val mockDataWithoutSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.PIX.code.toString(), "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        val mockDataWithSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.PIX.code.toString(), Types.PIX.code.toString(), "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(
            mockDataWithSelectedType,
            getDataWithOnlySelectedType(mockDataWithoutSelectedType)
        )
    }

    @Test
    fun `Valid getDataWithOnlySelectedType Only Type Billet`() {
        val mockDataWithoutSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.BILLET.code.toString(), "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        val mockDataWithSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.BILLET.code.toString(), Types.BILLET.code.toString(), "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(
            mockDataWithSelectedType,
            getDataWithOnlySelectedType(mockDataWithoutSelectedType)
        )
    }

    @Test
    fun `Valid getDataWithOnlySelectedType Only Type Wiretransfer`() {
        val mockDataWithoutSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.WIRETRANSFER.code.toString(), "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        val mockDataWithSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.WIRETRANSFER.code.toString(), Types.WIRETRANSFER.code.toString(), "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(
            mockDataWithSelectedType,
            getDataWithOnlySelectedType(mockDataWithoutSelectedType)
        )
    }

    @Test
    fun `Valid getDataWithOnlySelectedType Only Type Wallet`() {
        val mockDataWithoutSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.WALLET.code.toString(), "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        val mockDataWithSelectedType = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", TypesToSelect.WALLET.code.toString(), Types.WALLET.code.toString(), "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(
            mockDataWithSelectedType,
            getDataWithOnlySelectedType(mockDataWithoutSelectedType)
        )
    }
}