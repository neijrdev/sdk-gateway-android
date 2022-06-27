package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.*
import org.junit.Assert
import org.junit.Test

class HandleDataCheckoutTest {

    @Test
    fun `Valid transactions types valid`() {
        Assert.assertEquals(4, Types.PIX.code)
        Assert.assertEquals(1, Types.BILLET.code)
        Assert.assertEquals(0, Types.WIRETRANSFER.code)
        Assert.assertEquals(5, Types.WALLET.code)
    }

    @Test
    fun `Valid Environments list`() {
        Assert.assertEquals("PLAYGROUND", Environments.PLAYGROUND.toString())
        Assert.assertEquals("PRODUCTION", Environments.PRODUCTION.toString())
        Assert.assertEquals("DEVELOPMENT", Environments.DEVELOPMENT.toString())
    }

    @Test
    fun `Valid function get string binary by number int`() {
        Assert.assertEquals("0000", getBinaryStringByDecimalNumber(0))
        Assert.assertEquals("0001", getBinaryStringByDecimalNumber(1))
        Assert.assertEquals("0010", getBinaryStringByDecimalNumber(2))
        Assert.assertEquals("0011", getBinaryStringByDecimalNumber(3))
        Assert.assertEquals("0100", getBinaryStringByDecimalNumber(4))
        Assert.assertEquals("0101", getBinaryStringByDecimalNumber(5))
        Assert.assertEquals("0110", getBinaryStringByDecimalNumber(6))
        Assert.assertEquals("0111", getBinaryStringByDecimalNumber(7))
        Assert.assertEquals("1000", getBinaryStringByDecimalNumber(8))
        Assert.assertEquals("1001", getBinaryStringByDecimalNumber(9))
        Assert.assertEquals("1010", getBinaryStringByDecimalNumber(10))
        Assert.assertEquals("1011", getBinaryStringByDecimalNumber(11))
        Assert.assertEquals("1100", getBinaryStringByDecimalNumber(12))
        Assert.assertEquals("1101", getBinaryStringByDecimalNumber(13))
        Assert.assertEquals("1110", getBinaryStringByDecimalNumber(14))
        Assert.assertEquals("1111", getBinaryStringByDecimalNumber(15))
    }

    @Test
    fun `Valid function checkTypeEnable with invalid values`() {
        Assert.assertEquals(false, checkTypeEnable(0, Type.PIX.code))
        Assert.assertEquals(false, checkTypeEnable(0, Type.BILLET.code))
        Assert.assertEquals(false, checkTypeEnable(0, Type.WALLET.code))
        Assert.assertEquals(false, checkTypeEnable(0, Type.WIRETRANSFER.code))
        Assert.assertEquals(false, checkTypeEnable(10, Type.PIX.code))
        Assert.assertEquals(false, checkTypeEnable(14, Type.PIX.code))
        Assert.assertEquals(false, checkTypeEnable(12, Type.PIX.code))
        Assert.assertEquals(false, checkTypeEnable(6, Type.PIX.code))
        Assert.assertEquals(false, checkTypeEnable(6, Type.WALLET.code))
        Assert.assertEquals(false, checkTypeEnable(-1, -1))
    }

    @Test
    fun `Valid function checkTypeEnable with valid values`() {
        Assert.assertEquals(true, checkTypeEnable(1, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(2, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(3, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(3, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(4, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(5, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(5, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(6, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(6, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(7, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(7, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(7, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(8, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(9, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(9, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(10, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(10, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(11, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(11, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(11, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(12, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(12, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(13, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(13, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(13, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(14, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(14, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(14, Type.BILLET.code))
        Assert.assertEquals(true, checkTypeEnable(15, Type.PIX.code))
        Assert.assertEquals(true, checkTypeEnable(15, Type.WALLET.code))
        Assert.assertEquals(true, checkTypeEnable(15, Type.WIRETRANSFER.code))
        Assert.assertEquals(true, checkTypeEnable(15, Type.BILLET.code))
    }



}