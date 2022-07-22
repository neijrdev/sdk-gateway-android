package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getMockServiceStatusResponse
import org.junit.Assert
import org.junit.Test

class GetServiceStatusTest {

    @Test
    fun `CASE 01, tests if getServiceStatus calls getServiceStatus from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callGetServiceStatus()

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledGetServiceStatus)
    }

    @Test
    fun `CASE 02, when getServiceStatus from RemoteDataSource returns success`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockServicesStatusResponse = getMockServiceStatusResponse()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            getServiceStatusSuccessReceived,
            getServiceStatusFailureReceived,
        ) = paymentRepositoryTestUtils.callGetServiceStatus()

        //THEN
        Assert.assertEquals(null, getServiceStatusFailureReceived)
        Assert.assertEquals(getMockServiceStatusResponse(), getServiceStatusSuccessReceived)
    }

    @Test
    fun `CASE 03, when getServiceStatus from RemoteDataSource returns failure`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockServicesStatusResponse = null,
            mockResponseThrowable = RuntimeException("Error getServiceStatus request!")
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            getServiceStatusSuccessReceived,
            getServiceStatusFailureReceived,
        ) = paymentRepositoryTestUtils.callGetServiceStatus()

        //THEN
        Assert.assertEquals(null, getServiceStatusSuccessReceived)
        Assert.assertEquals("Error getServiceStatus request!",
            getServiceStatusFailureReceived?.message)
    }

    @Test
    fun `CASE 04, when getServiceStatus run catch`() {
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "getServiceStatus run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            getServiceStatusFailureReceived,
        ) = paymentRepositoryTestUtils.callGetServiceStatus()

        Assert.assertEquals(
            "getServiceStatus run catch error!",
            getServiceStatusFailureReceived?.message)
    }
}