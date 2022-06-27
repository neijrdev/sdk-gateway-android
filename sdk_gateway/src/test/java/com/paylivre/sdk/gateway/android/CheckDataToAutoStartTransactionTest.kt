package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.TypePixKey
import com.paylivre.sdk.gateway.android.domain.model.checkDataToAutoStartTransaction
import org.junit.Assert
import org.junit.Test

class CheckDataToAutoStartTransactionTest {

    @Test
    fun `test checkDataToAutoStartTransaction only pix not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "1", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only pix empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "1", "", "",
            "", "60712326006",
            "", "", "", "", 0
        )

        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only pix not empty email and empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "1", "", "",
            "person_user_gateway@test.com", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only pix empty email e empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "1", "", "",
            "person_user_gateway@test.com", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only billet not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "2", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only billet empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "2", "", "",
            "", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only billet not empty email e empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "2", "", "",
            "person_user_gateway@test.com", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only billet empty email e empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "2", "", "",
            "", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only wiretransfer not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "4", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only wiretransfer empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "4", "", "",
            "", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction only wiretransfer not empty email e empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "4", "", "",
            "person_user_gateway@test.com", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }


    @Test
    fun `test checkDataToAutoStartTransaction only wiretransfer empty email e empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "4", "", "",
            "", "",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction many types(3) not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "3", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction many types(10) not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "10", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction many types(15) not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "15", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction many types(7) not empty email e not empty document`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.DEPOSIT.code.toString(),
            "", "7", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction if withdraw type Wallet`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.WITHDRAW.code.toString(),
            "", "8", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", "", "", 0
        )
        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
    }

    @Test
    fun `test checkDataToAutoStartTransaction if withdraw type PIX with pix_key_type and pix_key valid`() {
        val mockData = DataGenerateSignature(
            "", 0, "", "",
            "", "", Operation.WITHDRAW.code.toString(),
            "", "1", "", "",
            "person_user_gateway@test.com", "60712326006",
            "", "", TypePixKey.DOCUMENT.code.toString(), "60712326006", 0
        )
        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
    }


}