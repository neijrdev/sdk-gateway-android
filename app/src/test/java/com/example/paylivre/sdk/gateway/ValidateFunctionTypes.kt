package com.example.paylivre.sdk.gateway

import com.example.paylivre.sdk.gateway.utils.TypesChecked
import com.example.paylivre.sdk.gateway.utils.getNumberByTypesChecked
import com.paylivre.sdk.gateway.android.utils.convertBinaryToDecimal
import org.junit.Assert
import org.junit.Test

class ConvertBinaryToDecimal {
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
    fun `Valid function getNumberTypesFromBitMask`() {
        Assert.assertEquals(1, getNumberByTypesChecked(TypesChecked(0,0,0,1)))
        Assert.assertEquals(2, getNumberByTypesChecked(TypesChecked(0,0,1,0)))
        Assert.assertEquals(3, getNumberByTypesChecked(TypesChecked(0,0,1, 1)))
        Assert.assertEquals(4, getNumberByTypesChecked(TypesChecked(0,1,0,0)))
        Assert.assertEquals(5, getNumberByTypesChecked(TypesChecked(0,1,0,1)))
        Assert.assertEquals(6, getNumberByTypesChecked(TypesChecked(0,1,1,0)))
        Assert.assertEquals(7, getNumberByTypesChecked(TypesChecked(0,1,1,1)))
        Assert.assertEquals(8, getNumberByTypesChecked(TypesChecked(1,0,0,0)))
        Assert.assertEquals(9, getNumberByTypesChecked(TypesChecked(1,0,0,1)))
        Assert.assertEquals(10, getNumberByTypesChecked(TypesChecked(1,0,1,0)))
        Assert.assertEquals(11, getNumberByTypesChecked(TypesChecked(1,0,1,1)))
        Assert.assertEquals(12, getNumberByTypesChecked(TypesChecked(1,1,0,0)))
        Assert.assertEquals(13, getNumberByTypesChecked(TypesChecked(1,1,0,1)))
        Assert.assertEquals(14, getNumberByTypesChecked(TypesChecked(1,1,1,0)))
        Assert.assertEquals(15, getNumberByTypesChecked(TypesChecked(1,1,1,1)))
    }
}