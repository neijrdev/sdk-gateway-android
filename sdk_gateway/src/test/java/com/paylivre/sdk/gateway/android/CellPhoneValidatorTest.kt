package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.cellPhoneValidator
import org.junit.Assert
import org.junit.Test

class CellPhoneValidatorTest {
    @Test
    fun `Valid cell phone validator`() {
        Assert.assertEquals(true, cellPhoneValidator("(13) - 9999- 3333-. axb1"))
        Assert.assertEquals(true, cellPhoneValidator("(13) - 9999- 3333-. axb"))
        Assert.assertEquals(true, cellPhoneValidator("(13) - 9999- 33337"))
        Assert.assertEquals(true, cellPhoneValidator("73 9234 56783"))
        Assert.assertEquals(true, cellPhoneValidator("7382345678"))
        Assert.assertEquals(true, cellPhoneValidator("73823456783"))
        Assert.assertEquals(true, cellPhoneValidator("73 9912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("73723456789"))
        Assert.assertEquals(false, cellPhoneValidator("73623456789"))
        Assert.assertEquals(false, cellPhoneValidator("7362345689"))
        Assert.assertEquals(false, cellPhoneValidator("0362345689"))
        Assert.assertEquals(false, cellPhoneValidator("0302345689"))
        Assert.assertEquals(false, cellPhoneValidator("(13) - 99991- 33337"))
        Assert.assertEquals(false, cellPhoneValidator("(13) - 9999- 333"))
        Assert.assertEquals(false, cellPhoneValidator("73 1234567899"))
        Assert.assertEquals(false, cellPhoneValidator("731234567899"))
        Assert.assertEquals(false, cellPhoneValidator("00 9912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("01 9912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 7912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 6912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 5912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 4912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 3912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 2912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 1912 33333"))
        Assert.assertEquals(false, cellPhoneValidator("11 0912 33333"))
    }
}

