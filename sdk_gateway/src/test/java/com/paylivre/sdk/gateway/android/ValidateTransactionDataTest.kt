package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.*
import org.junit.Assert
import org.junit.Test

class ValidateTransactionDataTest {

    @Test
    fun `test validateOperation`() {
        //valid operation: 0-(Deposit) 5-(Withdraw)
        Assert.assertEquals(true, validateOperation(0).isValid)
        Assert.assertEquals("",
            validateOperation(0).errorTags?.joinToString { it }
        )

        Assert.assertEquals(true, validateOperation(5).isValid)
        Assert.assertEquals("",
            validateOperation(5).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateOperation(1).isValid)
        Assert.assertEquals("RX002",
            validateOperation(1).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateOperation(2).isValid)
        Assert.assertEquals("RX002",
            validateOperation(2).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateOperation(3).isValid)
        Assert.assertEquals("RX002",
            validateOperation(3).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateOperation(4).isValid)
        Assert.assertEquals("RX002",
            validateOperation(4).errorTags?.joinToString { it }
        )


        Assert.assertEquals(false, validateOperation(-1).isValid)
        Assert.assertEquals("RX002",
            validateOperation(-1).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateOperation(6).isValid)
        Assert.assertEquals("RX002",
            validateOperation(6).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `test validateCurrency`() {
        //valid currency: BRL and USD
        Assert.assertEquals(true, validateCurrency("BRL").isValid)
        Assert.assertEquals("",
            validateCurrency("BRL").errorTags?.joinToString { it }
        )

        Assert.assertEquals(true, validateCurrency("USD").isValid)
        Assert.assertEquals("",
            validateCurrency("USD").errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateCurrency("EUR").isValid)
        Assert.assertEquals("RX005",
            validateCurrency("EUR").errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateCurrency("GBP").isValid)
        Assert.assertEquals("RX005",
            validateCurrency("GBP").errorTags?.joinToString { it }
        )
        Assert.assertEquals(false, validateCurrency("123").isValid)
        Assert.assertEquals("RX005",
            validateCurrency("123").errorTags?.joinToString { it }
        )
    }

    @Test
    fun `test validateAmount`() {
        //min amount = 500 (BRL/USD)
        Assert.assertEquals(true, validateAmount(500).isValid)
        Assert.assertEquals("",
            validateAmount(500).errorTags?.joinToString { it }
        )

        Assert.assertEquals(true, validateAmount(1000).isValid)
        Assert.assertEquals("",
            validateAmount(1000).errorTags?.joinToString { it }
        )

        Assert.assertEquals(true, validateAmount(100000).isValid)
        Assert.assertEquals("",
            validateAmount(100000).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateAmount(499).isValid)
        Assert.assertEquals("RX004",
            validateAmount(499).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateAmount(-500).isValid)
        Assert.assertEquals("RX004",
            validateAmount(-500).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateAmount(0).isValid)
        Assert.assertEquals("RX004",
            validateAmount(0).errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateAmount(-1).isValid)
        Assert.assertEquals("RX004",
            validateAmount(-1).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `test validateDataTransaction with valid values`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf()
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                0,
                "BRL",
                1,
                500,
                "12654d7",
                0
            )
        )

        Assert.assertEquals(
            "",
            mockDataTransaction1.errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            ResponseValidateDataParams(true, errorTagsCustom1),
            mockDataTransaction1
        )

        var errorTagsCustom2: MutableList<String> = mutableListOf()
        var mockDataTransaction2 = validateDataTransaction(
            DataTransaction(
                5,
                "BRL",
                1,
                500,
                "12654d7",
                0
            )
        )

        Assert.assertEquals(
            "",
            mockDataTransaction2.errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            ResponseValidateDataParams(true, errorTagsCustom2),
            mockDataTransaction2
        )
    }

    @Test
    fun `test validateDataTransaction with invalid type`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf("RX011")
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                0,
                "BRL",
                0,
                500,
                "12654d7", 0
            )
        )

        Assert.assertEquals(
            "RX011",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )

    }

    @Test
    fun `test validateDataTransaction with invalid operation`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf("RX002")
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                1,
                "BRL",
                1,
                500,
                "12654d7",
                0
            )
        )

        Assert.assertEquals(
            "RX002",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )

    }


    @Test
    fun `test validateDataTransaction with invalid currency`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf("RX005")
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                0,
                "EUR",
                1,
                500,
                "12654d7",
                0
            )
        )

        Assert.assertEquals(
            "RX005",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )
    }


    @Test
    fun `test validateDataTransaction with invalid amount`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf("RX004")
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                0,
                "BRL",
                1,
                499,
                "12654d7",
                0
            )
        )

        Assert.assertEquals(
            "RX004",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )
    }

    @Test
    fun `test validateDataTransaction with invalid Merchant Transaction Id`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf("RX003")
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                0,
                "BRL",
                1,
                500,
                "",
                0
            )
        )

        Assert.assertEquals(
            "RX003",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )
    }

    @Test
    fun `test validateDataTransaction with invalid all data`() {
        var errorTagsCustom1: MutableList<String> = mutableListOf(
            "RX011", "RX002", "RX005", "RX004", "RX003"
        )
        var mockDataTransaction1 = validateDataTransaction(
            DataTransaction(
                2,
                "EUR",
                0,
                0,
                "",
                0
            )
        )

        Assert.assertEquals(
            "RX011, RX002, RX005, RX004, RX003",
            mockDataTransaction1.errorTags?.joinToString { it }
        )
        Assert.assertEquals(
            ResponseValidateDataParams(false, errorTagsCustom1),
            mockDataTransaction1
        )
    }

    @Test
    fun `test validateSignature with signature valid`() {
        val mockSignature = "asdasdhadfkljsadhlfakjsdhflkasjh"
        Assert.assertEquals(
            true,
            validateSignature(mockSignature).isValid
        )
    }

    @Test
    fun `test validateSignature with signature invalid`() {
        Assert.assertEquals(false, validateSignature("").isValid)
        Assert.assertEquals("RX008",
            validateSignature("").errorTags?.joinToString { it }
        )

        Assert.assertEquals(false, validateSignature(null).isValid)
        Assert.assertEquals("RX008",
            validateSignature(null).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `test validateWithdrawDataPix`() {
        Assert.assertEquals(true,
            validateWithdrawDataPix(TypePixKey.DOCUMENT.code, "57619632050").isValid
        )
        Assert.assertEquals(true,
            validateWithdrawDataPix(TypePixKey.PHONE.code, "99999999999").isValid
        )
        Assert.assertEquals(true,
            validateWithdrawDataPix(TypePixKey.EMAIL.code, "test@test.com").isValid
        )
        Assert.assertEquals(false,
            validateWithdrawDataPix(null, "test@test.com").isValid
        )
        Assert.assertEquals("RX015, RX016",
            validateWithdrawDataPix(null, "test@test.com")
                .errorTags?.joinToString { it }
        )
        Assert.assertEquals(false,
            validateWithdrawDataPix(null, null).isValid
        )
        Assert.assertEquals("RX015, RX016",
            validateWithdrawDataPix(null, null)
                .errorTags?.joinToString { it }
        )
    }
}