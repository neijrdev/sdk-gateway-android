package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.ErrorTags
import com.paylivre.sdk.gateway.android.domain.model.ResponseValidateDataParams
import com.paylivre.sdk.gateway.android.domain.model.getResponseValidateDataParams
import org.junit.Assert
import org.junit.Test

class GetResponseValidateDataParamsTest {
    @Test
    fun `test getResponseValidateDataParams with sErrorTag not null`() {
        val responseValidateDataParams =
            getResponseValidateDataParams(false, sErrorTag = ErrorTags.RX011.toString())
        var mockErrorTagsCustom: MutableList<String> = mutableListOf()
        mockErrorTagsCustom.add(ErrorTags.RX011.toString())

        Assert.assertEquals(
            ResponseValidateDataParams(false, mockErrorTagsCustom),
            responseValidateDataParams
        )
    }

    @Test
    fun `test getResponseValidateDataParams with sErrorTag null and errorTags list null`() {
        val responseValidateDataParams = getResponseValidateDataParams(false)
        var mockErrorTagsCustom: MutableList<String> = mutableListOf()
        Assert.assertEquals(
            ResponseValidateDataParams(false, mockErrorTagsCustom),
            responseValidateDataParams
        )
    }

    @Test
    fun `test getResponseValidateDataParams with sErrorTag null and errorTags list not null`() {
        var mockErrorTagsCustom: MutableList<String> = mutableListOf()
        mockErrorTagsCustom.add(ErrorTags.RX011.toString())

        val responseValidateDataParams =
            getResponseValidateDataParams(false, errorTags = mockErrorTagsCustom)

        Assert.assertEquals(
            ResponseValidateDataParams(false, mockErrorTagsCustom),
            responseValidateDataParams
        )
    }

    @Test
    fun `test getResponseValidateDataParams with sErrorTag null and errorTags list not null 2`() {
        var mockErrorTagsCustom: MutableList<String> = mutableListOf(
            ErrorTags.RX011.toString(),
            ErrorTags.RX001.toString()
        )

        val mockErrorTags: MutableList<String> = mutableListOf(
            ErrorTags.RX011.toString(),
            ErrorTags.RX001.toString()
        )

        val responseValidateDataParams =
            getResponseValidateDataParams(false, errorTags = mockErrorTags)

        Assert.assertEquals(
            ResponseValidateDataParams(false, mockErrorTagsCustom),
            responseValidateDataParams
        )
    }
}