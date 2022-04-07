package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.isCnpj
import org.junit.Assert
import org.junit.Test

class CnpjValidatorTest {
    @Test
    fun `Valid cnpj validation test with document with score`() {
        Assert.assertEquals(true, "82.327.872/0001-23".isCnpj())
        Assert.assertEquals(true, "82.327.872/000123".isCnpj())
        Assert.assertEquals(true, "82.327.872000123".isCnpj())
        Assert.assertEquals(true, "82.327872000123".isCnpj())
        Assert.assertEquals(true, "82.327-8-7a2000=12-3".isCnpj())
    }

    @Test
    fun `Invalid cnpj validation test with document with score`() {
        Assert.assertEquals(false, "82.327.872/3001-23".isCnpj())
        Assert.assertEquals(false, "82.327.872/300123".isCnpj())
        Assert.assertEquals(false, "82.327.872300123".isCnpj())
        Assert.assertEquals(false, "82.327872300123".isCnpj())
    }

    @Test
    fun `cnpj validation test valid with document only with numbers`() {
        Assert.assertEquals(true, "40700819000186".isCnpj())
        Assert.assertEquals(true, "32525767000120".isCnpj())
        Assert.assertEquals(true, "33041910000171".isCnpj())
        Assert.assertEquals(true, "97835114000193".isCnpj())
    }

    @Test
    fun `cnpj validation test invalid with document only with numbers`() {
        Assert.assertEquals(false, "40700819740086".isCnpj())
        Assert.assertEquals(false, "32525799000120".isCnpj())
        Assert.assertEquals(false, "33041223000171".isCnpj())
        Assert.assertEquals(false, "97835114001193".isCnpj())
    }

    @Test
    fun `cnpj validation test with insufficient numbers with score`() {
        Assert.assertEquals(false, "83.070.572/0001-7".isCnpj())
        Assert.assertEquals(false, "83.070.572/0001-".isCnpj())
        Assert.assertEquals(false, "83.070.572/000".isCnpj())
        Assert.assertEquals(false, "83.070.572/00".isCnpj())
        Assert.assertEquals(false, "83.070.572/0".isCnpj())
        Assert.assertEquals(false, "83.070.572/".isCnpj())
        Assert.assertEquals(false, "83.070.57".isCnpj())
        Assert.assertEquals(false, "83.070.5".isCnpj())
        Assert.assertEquals(false, "83.070".isCnpj())
        Assert.assertEquals(false, "83.0".isCnpj())
    }

    @Test
    fun `cnpj validation test with insufficient numbers with numbers`() {
        Assert.assertEquals(false, "6733649500010".isCnpj())
        Assert.assertEquals(false, "673364950001".isCnpj())
        Assert.assertEquals(false, "67336495000".isCnpj())
        Assert.assertEquals(false, "6733649500".isCnpj())
        Assert.assertEquals(false, "673364950".isCnpj())
        Assert.assertEquals(false, "67336495".isCnpj())
        Assert.assertEquals(false, "6733649".isCnpj())
    }

    @Test
    fun `cnpj validation test with invalid characters`() {
        Assert.assertEquals(false, "null".isCnpj())
        Assert.assertEquals(false, "undefined".isCnpj())
        Assert.assertEquals(false, "[]".isCnpj())
        Assert.assertEquals(false, ".".isCnpj())
        Assert.assertEquals(false, "{}".isCnpj())
        Assert.assertEquals(false, "Object".isCnpj())
        Assert.assertEquals(false, "-".isCnpj())
    }


}