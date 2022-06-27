package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.*
import org.junit.Assert
import org.junit.Test

class DataStartCheckoutByParamsTest {
    @Test
    fun `Test CurrencyPrefix`() {
        Assert.assertEquals("R$", CurrencyPrefix.BRL.prefix)
        Assert.assertEquals("$", CurrencyPrefix.USD.prefix)
    }

    @Test
    fun `Test getBaseUrlByEnvironment`() {
        Assert.assertEquals(BASE_URL_ENVIRONMENT_PLAYGROUND, getBaseUrlByEnvironment("PLAYGROUND"))
        Assert.assertEquals(BASE_URL_ENVIRONMENT_PRODUCTION, getBaseUrlByEnvironment("PRODUCTION"))
        Assert.assertEquals(BASE_URL_ENVIRONMENT_DEV, getBaseUrlByEnvironment("DEVELOPMENT"))
    }

    @Test
    fun `Test getHostApiByEnvironment`() {
        Assert.assertEquals(API_HOST_ENVIRONMENT_PLAYGROUND, getHostApiByEnvironment("PLAYGROUND"))
        Assert.assertEquals(API_HOST_ENVIRONMENT_PRODUCTION, getHostApiByEnvironment("PRODUCTION"))
        Assert.assertEquals(API_HOST_ENVIRONMENT_DEV, getHostApiByEnvironment("DEVELOPMENT"))
        Assert.assertEquals("INVALID_ENVIRONMENT", getHostApiByEnvironment("OTHER_ENVIRONMENT"))
    }

    @Test
    fun `Test getHostApiByBaseUrl`() {
        Assert.assertEquals(API_HOST_ENVIRONMENT_PLAYGROUND, getHostApiByBaseUrl(BASE_URL_ENVIRONMENT_PLAYGROUND))
        Assert.assertEquals(API_HOST_ENVIRONMENT_PRODUCTION, getHostApiByBaseUrl(BASE_URL_ENVIRONMENT_PRODUCTION))
        Assert.assertEquals(API_HOST_ENVIRONMENT_DEV, getHostApiByBaseUrl(BASE_URL_ENVIRONMENT_DEV))
        Assert.assertEquals("INVALID_ENVIRONMENT", getHostApiByBaseUrl("OTHER_ENVIRONMENT"))
    }


    @Test
    fun `Test getHandledDataOrderRequestUrl, clear unnecessary data according to the transaction`() {
        val mockOrderDataRequest = OrderDataRequest(
            "", 0, "", "", "",
            Operation.WITHDRAW.code.toString(), "", "", Type.WALLET.code.toString(),
            "", "", "", "test@test.com",
            "45132a1s3d21a321654dasd", "-1", "123456789", "",
            "", "", "", ""
        )
        Assert.assertEquals("", getHandledDataOrderRequestUrl(mockOrderDataRequest).api_token)
        Assert.assertEquals("", getHandledDataOrderRequestUrl(mockOrderDataRequest).pix_key_type)
        Assert.assertEquals("", getHandledDataOrderRequestUrl(mockOrderDataRequest).pix_key)
        Assert.assertEquals("", getHandledDataOrderRequestUrl(mockOrderDataRequest).login_email)
    }
}