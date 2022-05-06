package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.*
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyUSD
import org.junit.Assert
import org.junit.Test

class CurrencyFormatTest {

    @Test
    fun `toCurrencyUSD()`() {
        Assert.assertEquals(
            "US$ 1,000,000.00",
            "100000000".toDouble().div(100).toCurrencyUSD("en")
        )
        Assert.assertEquals(
            "US$ 100.000.000,00",
            "10000000000".toDouble().div(100).toCurrencyUSD("pt")
        )
        Assert.assertEquals(
            "US$ 1.000.000.000,00",
            "100000000000".toDouble().div(100).toCurrencyUSD("pt")
        )
        Assert.assertEquals(
            "US$ 1.000.000.005,43",
            "100000000543".toDouble().div(100).toCurrencyUSD("pt")
        )
        Assert.assertEquals(
            "US$ 1.001.234.567,89",
            "100123456789".toDouble().div(100).toCurrencyUSD("pt")
        )
        Assert.assertEquals(
            "US$ 0,00",
            "0".toDouble().div(100).toCurrencyUSD("pt")
        )
    }

    @Test
    fun `toCurrencyBRL()`() {
        Assert.assertEquals(
            "R$ 1,000,000.00",
            "100000000".toDouble().div(100).toCurrencyBRL("en")
        )
        Assert.assertEquals(
            "R$ 100,000,000.00",
            "10000000000".toDouble().div(100).toCurrencyBRL("en")
        )
        Assert.assertEquals(
            "R$ 1.000.000.000,00",
            "100000000000".toDouble().div(100).toCurrencyBRL("pt")
        )
        Assert.assertEquals(
            "R$ 0.00",
            "0".toDouble().div(100).toCurrencyBRL("en")
        )
        Assert.assertEquals(
            "R$ 0.00",
            "00".toDouble().div(100).toCurrencyBRL("en")
        )
        Assert.assertEquals(
            "R$ 0.00",
            "000".toDouble().div(100).toCurrencyBRL("en")
        )
        Assert.assertEquals(
            "R$ 0,01",
            "001".toDouble().div(100).toCurrencyBRL("pt")
        )
        Assert.assertEquals(
            "R$ 0,01",
            "1".toDouble().div(100).toCurrencyBRL("pt")
        )
        Assert.assertEquals(
            "R$ 0,10",
            "10".toDouble().div(100).toCurrencyBRL("pt")
        )
    }

    @Test
    fun `formatToCurrencyUSD() invalidNumber`() {
        Assert.assertEquals("", formatToCurrencyUSD("100v0"))
        Assert.assertEquals("", formatToCurrencyUSD("100,00"))
        Assert.assertEquals("", formatToCurrencyUSD("100,"))
        Assert.assertEquals("", formatToCurrencyUSD(null))
    }

    @Test
    fun `formatToCurrencyUSD() validNumber`() {
        Assert.assertEquals(
            "US$ 1.00",
            formatToCurrencyUSD("100.", 100, "en")
        )
        Assert.assertEquals(
            "US$ 1.00",
            formatToCurrencyUSD("100.0", 100, "en")
        )
        Assert.assertEquals(
            "US$ 1.00",
            formatToCurrencyUSD("100.00", 100, "en")
        )
        Assert.assertEquals(
            "US$ 1.23",
            formatToCurrencyUSD("123", 100, "en")
        )
        Assert.assertEquals(
            "US$ 12.34",
            formatToCurrencyUSD("1234", 100, "en")
        )
        Assert.assertEquals(
            "US$ 123.45",
            formatToCurrencyUSD("12345", 100, "en")
        )
        Assert.assertEquals(
            "US$ 1,234.56",
            formatToCurrencyUSD("123456", 100, "en")
        )
        Assert.assertEquals(
            "US$ 12,345.67",
            formatToCurrencyUSD("1234567", 100, "en")
        )
        Assert.assertEquals(
            "US$ 123,456.78",
            formatToCurrencyUSD("12345678", 100, "en")
        )
        Assert.assertEquals(
            "US$ 1,234,567.89",
            formatToCurrencyUSD("123456789", 100, "en")
        )
        Assert.assertEquals(
            "US$ 12,345,678.90",
            formatToCurrencyUSD("1234567890", 100, "en")
        )
    }


    @Test
    fun `formatToCurrencyBRL() invalidNumber`() {
        Assert.assertEquals("", formatToCurrencyBRL("100v0"))
        Assert.assertEquals("", formatToCurrencyBRL("100,00"))
        Assert.assertEquals("", formatToCurrencyBRL("100,"))
        Assert.assertEquals("", formatToCurrencyBRL(null))
    }

    @Test
    fun `formatToCurrencyBRL() validNumber`() {
        Assert.assertEquals(
            "R$ 1,00",
            formatToCurrencyBRL("100.", 100, "pt")
        )
        Assert.assertEquals(
            "R$ 1.00",
            formatToCurrencyBRL("100.0", 100, "en")
        )
        Assert.assertEquals(
            "R$ 1.00",
            formatToCurrencyBRL("100.00", 100, "en")
        )
        Assert.assertEquals(
            "R$ 1.23",
            formatToCurrencyBRL("123", 100, "en")
        )
        Assert.assertEquals(
            "R$ 12.34",
            formatToCurrencyBRL("1234", 100, "en")
        )
        Assert.assertEquals(
            "R$ 123.45",
            formatToCurrencyBRL("12345", 100, "en")
        )
        Assert.assertEquals(
            "R$ 1,234.56",
            formatToCurrencyBRL("123456", 100, "en")
        )
        Assert.assertEquals(
            "R$ 12,345.67",
            formatToCurrencyBRL("1234567", 100, "en")
        )
        Assert.assertEquals(
            "R$ 123.456,78",
            formatToCurrencyBRL("12345678", 100, "pt")
        )
        Assert.assertEquals(
            "R$ 1,234,567.89",
            formatToCurrencyBRL("123456789", 100, "en")
        )
        Assert.assertEquals(
            "R$ 12,345,678.90",
            formatToCurrencyBRL("1234567890", 100, "en")
        )
    }


    @Test
    fun `formatToCurrencyBRL() validNumber byPass decimalFactor`() {
        Assert.assertEquals(
            "R$ 100.00",
            formatToCurrencyBRL("100.00", 1, "en")
        )
        Assert.assertEquals(
            "R$ 100.00",
            formatToCurrencyBRL("100.0", 1, "en")
        )
        Assert.assertEquals(
            "R$ 100.00",
            formatToCurrencyBRL("100.", 1, "en")
        )
        Assert.assertEquals(
            "R$ 10,000.00",
            formatToCurrencyBRL("10000.", 1, "en")
        )
        Assert.assertEquals(
            "R$ 100,000.00",
            formatToCurrencyBRL("100000.", 1, "en")
        )
        Assert.assertEquals(
            "R$ 1,000,000.00",
            formatToCurrencyBRL("1000000.", 1, "en")
        )
    }

    @Test
    fun `formatToCurrencyUSD() validNumber byPass decimalFactor`() {
        Assert.assertEquals(
            "US$ 100.00",
            formatToCurrencyUSD("100.00", 1, "en")
        )
        Assert.assertEquals(
            "US$ 100.00",
            formatToCurrencyUSD("100.0", 1, "en")
        )
        Assert.assertEquals(
            "US$ 100.00",
            formatToCurrencyUSD("100.", 1, "en")
        )
        Assert.assertEquals(
            "US$ 10,000.00",
            formatToCurrencyUSD("10000.", 1, "en")
        )
        Assert.assertEquals(
            "US$ 100,000.00",
            formatToCurrencyUSD("100000.", 1, "en")
        )
        Assert.assertEquals(
            "US$ 1,000,000.00",
            formatToCurrencyUSD("1000000.", 1, "en")
        )
    }


}