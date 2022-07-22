package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.api.ApiService
import com.paylivre.sdk.gateway.android.data.getMockPixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.lang.RuntimeException


class GetPixApprovalTimeTest {

    @Test
    fun `CASE 01, tests if getPixApprovalTime calls getPixApprovalTime from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callGetPixApprovalTime()

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledGetPixApprovalTime)
    }

    @Test
    fun `CASE 02, when getPixApprovalTime from RemoteDataSource returns success`() {
        //GIVEN
        val mockPixApprovalTimeResponse = getMockPixApprovalTimeResponse()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockPixApprovalTimeResponse = mockPixApprovalTimeResponse)

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived,
        ) = paymentRepositoryTestUtils.callGetPixApprovalTime()

        //THEN
        Assert.assertEquals(null, getPixApprovalTimeFailureReceived)
        Assert.assertEquals(mockPixApprovalTimeResponse, getPixApprovalTimeSuccessReceived)

    }

    @Test
    fun `CASE 03, when getPixApprovalTime from RemoteDataSource returns Throwable`() {
        //GIVEN
        val mockRemoteDataSource =
            RemoteDataSourceMock(
                mockPixApprovalTimeResponse = null,
                mockResponseThrowable = RuntimeException("error_request_not_connect_server")
            )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            getPixApprovalTimeSuccessReceived,
            getPixApprovalTimeFailureReceived,
        ) = paymentRepositoryTestUtils.callGetPixApprovalTime()

        //THEN
        Assert.assertEquals(null, getPixApprovalTimeSuccessReceived)
        Assert.assertEquals(
            RuntimeException("error_request_not_connect_server").message,
            getPixApprovalTimeFailureReceived?.message)
    }

    @Test
    fun `CASE 04, when getPixApprovalTime run catch`() {
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "getPixApprovalTime run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            getPixApprovalTimeFailureReceived,
        ) = paymentRepositoryTestUtils.callGetPixApprovalTime()

        Assert.assertEquals(
            "getPixApprovalTime run catch error!",
            getPixApprovalTimeFailureReceived?.message)

    }
}