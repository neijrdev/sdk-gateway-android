package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.utils.isCpf
import org.junit.Assert
import org.junit.Test

class CpfValidatorTest {
    @Test
    fun `Valid cpf validation test with document with score`() {
        Assert.assertEquals(true, "588.392.570-71".isCpf())
        Assert.assertEquals(true, "588392.570-71".isCpf())
        Assert.assertEquals(true, "588392.570-71".isCpf())
        Assert.assertEquals(true, "588392570-71".isCpf())
        Assert.assertEquals(true, "5*88.392.570-7/1".isCpf())
    }

    @Test
    fun `Invalid cpf validation test with document with score`() {
        Assert.assertEquals(false, "588.392.570-77".isCpf())
        Assert.assertEquals(false, "588392.570-77".isCpf())
        Assert.assertEquals(false, "588392.570-77".isCpf())
        Assert.assertEquals(false, "588392570-77".isCpf())
    }

    @Test
    fun `Cpf validation test valid with document only with numbers`() {
        Assert.assertEquals(true, "58839257071".isCpf())
        Assert.assertEquals(true, "98278924015".isCpf())
        Assert.assertEquals(true, "05484767059".isCpf())
        Assert.assertEquals(true, "05152593068".isCpf())
    }

    @Test
    fun `Cpf validation test invalid with document only with numbers`() {
        Assert.assertEquals(false, "58839257771".isCpf())
        Assert.assertEquals(false, "98278929735".isCpf())
        Assert.assertEquals(false, "05484799009".isCpf())
        Assert.assertEquals(false, "05172531268".isCpf())
    }

    @Test
    fun `Cpf validation test with insufficient numbers with score`() {
        Assert.assertEquals(false, "410.103.040-5".isCpf())
        Assert.assertEquals(false, "410.103.040-".isCpf())
        Assert.assertEquals(false, "410.103.040".isCpf())
        Assert.assertEquals(false, "410.103.0".isCpf())
        Assert.assertEquals(false, "410.103.".isCpf())
        Assert.assertEquals(false, "410.103".isCpf())
        Assert.assertEquals(false, "410.1".isCpf())
    }

    @Test
    fun `Cpf validation test with insufficient numbers with numbers`() {
        Assert.assertEquals(false, "4101030405".isCpf())
        Assert.assertEquals(false, "410103040".isCpf())
        Assert.assertEquals(false, "410103040".isCpf())
        Assert.assertEquals(false, "4101030".isCpf())
        Assert.assertEquals(false, "410103".isCpf())
        Assert.assertEquals(false, "410103".isCpf())
        Assert.assertEquals(false, "4101".isCpf())
    }

    @Test
    fun `cpf validation test with invalid characters`() {
        Assert.assertEquals(false, "null".isCpf())
        Assert.assertEquals(false, "undefined".isCpf())
        Assert.assertEquals(false, "[]".isCpf())
        Assert.assertEquals(false, ".".isCpf())
        Assert.assertEquals(false, "{}".isCpf())
        Assert.assertEquals(false, "Object".isCpf())
        Assert.assertEquals(false, "-".isCpf())
    }


}