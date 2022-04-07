package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.validateDocument
import org.junit.Assert
import org.junit.Test

class ValidateDocument {
    @Test
    fun `Valid cnpj validation test with document with score`() {
        Assert.assertEquals(true, validateDocument("82.327.872/0001-23"))
        Assert.assertEquals(true, validateDocument("82.327.872/000123"))
        Assert.assertEquals(true, validateDocument("82.327.872000123"))
        Assert.assertEquals(true, validateDocument("82.327872000123"))
    }

    @Test
    fun `Invalid cnpj validation test with document with score`() {
        Assert.assertEquals(false, validateDocument("82.327.872/3001-23"))
        Assert.assertEquals(false, validateDocument("82.327.872/300123"))
        Assert.assertEquals(false, validateDocument("82.327.872300123"))
        Assert.assertEquals(false, validateDocument("82.327872300123"))
    }

    @Test
    fun `cnpj validation test valid with document only with numbers`() {
        Assert.assertEquals(true, validateDocument("40700819000186"))
        Assert.assertEquals(true, validateDocument("32525767000120"))
        Assert.assertEquals(true, validateDocument("33041910000171"))
        Assert.assertEquals(true, validateDocument("97835114000193"))
    }

    @Test
    fun `cnpj validation test invalid with document only with numbers`() {
        Assert.assertEquals(false, validateDocument("40700819740086"))
        Assert.assertEquals(false, validateDocument("32525799000120"))
        Assert.assertEquals(false, validateDocument("33041223000171"))
        Assert.assertEquals(false, validateDocument("97835114001193"))
    }

    @Test
    fun `cnpj validation test with insufficient numbers with score`() {
        Assert.assertEquals(false, validateDocument("83.070.572/0001-7"))
        Assert.assertEquals(false, validateDocument("83.070.572/0001-"))
        Assert.assertEquals(false, validateDocument("83.070.572/000"))
        Assert.assertEquals(false, validateDocument("83.070.572/00"))
        Assert.assertEquals(false, validateDocument("83.070.572/0"))
        Assert.assertEquals(false, validateDocument("83.070.572/"))
        Assert.assertEquals(false, validateDocument("83.070.57"))
        Assert.assertEquals(false, validateDocument("83.070.5"))
        Assert.assertEquals(false, validateDocument("83.070"))
        Assert.assertEquals(false, validateDocument("83.0"))
    }

    @Test
    fun `cnpj validation test with insufficient numbers with numbers`() {
        Assert.assertEquals(false, validateDocument("6733649500010"))
        Assert.assertEquals(false, validateDocument("673364950001"))
        Assert.assertEquals(false, validateDocument("67336495000"))
        Assert.assertEquals(false, validateDocument("6733649500"))
        Assert.assertEquals(false, validateDocument("673364950"))
        Assert.assertEquals(false, validateDocument("67336495"))
        Assert.assertEquals(false, validateDocument("6733649"))
    }

    @Test
    fun `cnpj validation test with invalid characters`() {
        Assert.assertEquals(false, validateDocument("null"))
        Assert.assertEquals(false, validateDocument("undefined"))
        Assert.assertEquals(false, validateDocument("[]"))
        Assert.assertEquals(false, validateDocument("."))
        Assert.assertEquals(false, validateDocument("{}"))
        Assert.assertEquals(false, validateDocument("Object"))
        Assert.assertEquals(false, validateDocument("-"))
    }

    @Test
    fun `Valid cpf validation test with document with score`() {
        Assert.assertEquals(true, validateDocument("588.392.570-71"))
        Assert.assertEquals(true, validateDocument("588392.570-71"))
        Assert.assertEquals(true, validateDocument("588392.570-71"))
        Assert.assertEquals(true, validateDocument("588392570-71"))
    }

    @Test
    fun `Invalid cpf validation test with document with score`() {
        Assert.assertEquals(false, validateDocument("588.392.570-77"))
        Assert.assertEquals(false, validateDocument("588392.570-77"))
        Assert.assertEquals(false, validateDocument("588392.570-77"))
        Assert.assertEquals(false, validateDocument("588392570-77"))
    }

    @Test
    fun `Cpf validation test valid with document only with numbers`() {
        Assert.assertEquals(true, validateDocument("58839257071"))
        Assert.assertEquals(true, validateDocument("98278924015"))
        Assert.assertEquals(true, validateDocument("05484767059"))
        Assert.assertEquals(true, validateDocument("05152593068"))
    }

    @Test
    fun `Cpf validation test invalid with document only with numbers`() {
        Assert.assertEquals(false, validateDocument("58839257771"))
        Assert.assertEquals(false, validateDocument("98278929735"))
        Assert.assertEquals(false, validateDocument("05484799009"))
        Assert.assertEquals(false, validateDocument("05172531268"))
    }

    @Test
    fun `Cpf validation test with insufficient numbers with score`() {
        Assert.assertEquals(false, validateDocument("410.103.040-5"))
        Assert.assertEquals(false, validateDocument("410.103.040-"))
        Assert.assertEquals(false, validateDocument("410.103.040"))
        Assert.assertEquals(false, validateDocument("410.103.0"))
        Assert.assertEquals(false, validateDocument("410.103."))
        Assert.assertEquals(false, validateDocument("410.103"))
        Assert.assertEquals(false, validateDocument("410.1"))
    }

    @Test
    fun `Cpf validation test with insufficient numbers with numbers`() {
        Assert.assertEquals(false, validateDocument("4101030405"))
        Assert.assertEquals(false, validateDocument("410103040"))
        Assert.assertEquals(false, validateDocument("410103040"))
        Assert.assertEquals(false, validateDocument("4101030"))
        Assert.assertEquals(false, validateDocument("410103"))
        Assert.assertEquals(false, validateDocument("410103"))
        Assert.assertEquals(false, validateDocument("4101"))
        Assert.assertEquals(false, validateDocument(""))
        Assert.assertEquals(false, validateDocument("null"))
    }
    
}