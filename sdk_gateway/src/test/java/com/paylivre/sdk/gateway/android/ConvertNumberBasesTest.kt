package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.convertDecimalToBinary
import com.paylivre.sdk.gateway.android.utils.convertBinaryToDecimal
import org.junit.Assert
import org.junit.Test

class ConvertNumberBasesTest {
    @Test
    fun `Valid function to convert binary number to decimal`() {
        Assert.assertEquals(0, convertBinaryToDecimal("0000".toLong()))
        Assert.assertEquals(1, convertBinaryToDecimal("0001".toLong()))
        Assert.assertEquals(2, convertBinaryToDecimal("0010".toLong()))
        Assert.assertEquals(3, convertBinaryToDecimal("0011".toLong()))
        Assert.assertEquals(4, convertBinaryToDecimal("0100".toLong()))
        Assert.assertEquals(5, convertBinaryToDecimal("0101".toLong()))
        Assert.assertEquals(6, convertBinaryToDecimal("0110".toLong()))
        Assert.assertEquals(7, convertBinaryToDecimal("0111".toLong()))
        Assert.assertEquals(8, convertBinaryToDecimal("1000".toLong()))
        Assert.assertEquals(9, convertBinaryToDecimal("1001".toLong()))
        Assert.assertEquals(10, convertBinaryToDecimal("1010".toLong()))
        Assert.assertEquals(11, convertBinaryToDecimal("1011".toLong()))
        Assert.assertEquals(12, convertBinaryToDecimal("1100".toLong()))
        Assert.assertEquals(13, convertBinaryToDecimal("1101".toLong()))
        Assert.assertEquals(14, convertBinaryToDecimal("1110".toLong()))
        Assert.assertEquals(15, convertBinaryToDecimal("1111".toLong()))
    }


    @Test
    fun `Valid function to convert decimal number to binary number`() {
        Assert.assertEquals("0000", convertDecimalToBinary(0).toString().padStart(4,'0'))
        Assert.assertEquals("0001", convertDecimalToBinary(1).toString().padStart(4,'0'))
        Assert.assertEquals("0010", convertDecimalToBinary(2).toString().padStart(4,'0'))
        Assert.assertEquals("0011", convertDecimalToBinary(3).toString().padStart(4,'0'))
        Assert.assertEquals("0100", convertDecimalToBinary(4).toString().padStart(4,'0'))
        Assert.assertEquals("0101", convertDecimalToBinary(5).toString().padStart(4,'0'))
        Assert.assertEquals("0110", convertDecimalToBinary(6).toString().padStart(4,'0'))
        Assert.assertEquals("0111", convertDecimalToBinary(7).toString().padStart(4,'0'))
        Assert.assertEquals("1000", convertDecimalToBinary(8).toString().padStart(4,'0'))
        Assert.assertEquals("1001", convertDecimalToBinary(9).toString().padStart(4,'0'))
        Assert.assertEquals("1010", convertDecimalToBinary(10).toString().padStart(4,'0'))
        Assert.assertEquals("1011", convertDecimalToBinary(11).toString().padStart(4,'0'))
        Assert.assertEquals("1100", convertDecimalToBinary(12).toString().padStart(4,'0'))
        Assert.assertEquals("1101", convertDecimalToBinary(13).toString().padStart(4,'0'))
        Assert.assertEquals("1110", convertDecimalToBinary(14).toString().padStart(4,'0'))
        Assert.assertEquals("1111", convertDecimalToBinary(15).toString().padStart(4,'0'))

    }


}