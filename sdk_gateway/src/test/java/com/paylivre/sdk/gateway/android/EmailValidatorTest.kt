package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.isEmailValid
import org.junit.Assert
import org.junit.Test

class EmailValidatorTest {
    @Test
    fun `Test function validateEmail with valid Email`() {
        Assert.assertEquals(true, isEmailValid("teste@test.com"))
        Assert.assertEquals(true, isEmailValid("teste@test.com.br"))
        Assert.assertEquals(true, isEmailValid("123654@test.com.br"))
        Assert.assertEquals(true, isEmailValid("123654-23246@test.com.br"))
        Assert.assertEquals(true, isEmailValid("123654-._13_23246@test.com.br"))
        Assert.assertEquals(true, isEmailValid("null@null.com"))
    }

    @Test
    fun `Test function validateEmail with invalid Email`() {
        Assert.assertEquals(false, isEmailValid("testetest.com"))
        Assert.assertEquals(false, isEmailValid("teste@test"))
        Assert.assertEquals(false, isEmailValid("teste@test."))
        Assert.assertEquals(false, isEmailValid("teste@"))
        Assert.assertEquals(false, isEmailValid("teste.com.br"))
        Assert.assertEquals(false, isEmailValid("@teste.com.br"))

    }
}