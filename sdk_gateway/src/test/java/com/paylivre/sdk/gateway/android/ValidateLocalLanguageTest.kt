package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.validateLocaleLanguage
import org.junit.Assert
import org.junit.Test

class ValidateLocalLanguageTest {

    @Test
    fun `Valid function validateLocalLanguage with valid locales languages`() {
        Assert.assertEquals(true, validateLocaleLanguage("pt"))
        Assert.assertEquals(true, validateLocaleLanguage("PT"))
        Assert.assertEquals(true, validateLocaleLanguage("pT"))
        Assert.assertEquals(true, validateLocaleLanguage("Pt"))
        Assert.assertEquals(true, validateLocaleLanguage("pt-PT"))
        Assert.assertEquals(true, validateLocaleLanguage("pt-BR"))
        Assert.assertEquals(true, validateLocaleLanguage("pt-BR"))

        Assert.assertEquals(true, validateLocaleLanguage("en"))
        Assert.assertEquals(true, validateLocaleLanguage("EN"))
        Assert.assertEquals(true, validateLocaleLanguage("En"))
        Assert.assertEquals(true, validateLocaleLanguage("eN"))
        Assert.assertEquals(true, validateLocaleLanguage("en-tt"))
        Assert.assertEquals(true, validateLocaleLanguage("en-US"))
        Assert.assertEquals(true, validateLocaleLanguage("en-CA"))

        Assert.assertEquals(true, validateLocaleLanguage("es"))
        Assert.assertEquals(true, validateLocaleLanguage("ES"))
        Assert.assertEquals(true, validateLocaleLanguage("Es"))
        Assert.assertEquals(true, validateLocaleLanguage("eS"))
        Assert.assertEquals(true, validateLocaleLanguage("es-AR"))
        Assert.assertEquals(true, validateLocaleLanguage("es-cl"))
        Assert.assertEquals(true, validateLocaleLanguage("es-ES"))

    }

    @Test
    fun `Valid function validateLocalLanguage with invalid locales languages`() {
        Assert.assertEquals(false, validateLocaleLanguage("ta"))
        Assert.assertEquals(false, validateLocaleLanguage("tg"))
        Assert.assertEquals(false, validateLocaleLanguage("ja"))
        Assert.assertEquals(false, validateLocaleLanguage("zh-cn"))
        Assert.assertEquals(false, validateLocaleLanguage("az"))
        Assert.assertEquals(false, validateLocaleLanguage("null"))
        Assert.assertEquals(false, validateLocaleLanguage(null))
        Assert.assertEquals(false, validateLocaleLanguage(""))
    }
}