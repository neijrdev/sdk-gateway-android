package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getMockCheckStatusOrderDataRequest
import com.paylivre.sdk.gateway.android.data.getMockCheckStatusOrderDataResponse
import com.paylivre.sdk.gateway.android.data.getMockCheckStatusTransaction
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import org.junit.Assert
import org.junit.Test

class CheckStatusOrderTest {
    @Test
    fun `CASE 01, tests if checkStatusOrder calls checkStatusOrder from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callCheckStatusOrder(getMockCheckStatusOrderDataRequest())

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledCheckStatusOrder)
    }

    @Test
    fun `CASE 02, checks if the checkStatusOrder is passed correctly`() {
        //GIVEN
        val mockCheckStatusOrderDataRequest = getMockCheckStatusOrderDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        val (checkStatusOrderDataRequest) = paymentRepositoryTestUtils.callCheckStatusOrder(
            mockCheckStatusOrderDataRequest)

        //THEN
        Assert.assertEquals(mockCheckStatusOrderDataRequest, checkStatusOrderDataRequest)
        Assert.assertEquals(mockCheckStatusOrderDataRequest,
            mockRemoteDataSource.checkStatusOrderDataRequestReceived)
    }

    @Test
    fun `CASE 03, when checkStatusOrder from RemoteDataSource returns success`() {
        //GIVEN
        val mockCheckStatusOrderDataRequest = getMockCheckStatusOrderDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusOrderDataResponse = getMockCheckStatusOrderDataResponse()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusOrderSuccessReceived,
            checkStatusOrderFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusOrder(mockCheckStatusOrderDataRequest)

        //THEN
        Assert.assertEquals(null, checkStatusOrderFailureReceived)
        Assert.assertEquals(getMockCheckStatusOrderDataResponse(), checkStatusOrderSuccessReceived)
    }

    @Test
    fun `CASE 04, when checkStatusOrder from RemoteDataSource returns failure`() {
        //GIVEN
        val errorTransaction = ErrorTransaction(original_message = "error checkStatusOrder")
        val mockCheckStatusOrderDataRequest = getMockCheckStatusOrderDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusOrderDataResponse = null,
            mockResponseErrorTransaction = errorTransaction
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusOrderSuccessReceived,
            checkStatusOrderFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusOrder(mockCheckStatusOrderDataRequest)

        //THEN
        Assert.assertEquals(errorTransaction, checkStatusOrderFailureReceived)
        Assert.assertEquals(null, checkStatusOrderSuccessReceived)
    }

    @Test
    fun `CASE 05, when checkStatusOrder run catch`() {
        val mockCheckStatusOrderDataRequest = getMockCheckStatusOrderDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "checkStatusOrder run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            _,
            checkStatusOrderFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusOrder(mockCheckStatusOrderDataRequest)

        Assert.assertEquals(
            ErrorTransaction("checkStatusOrder run catch error!", null, null),
            checkStatusOrderFailureReceived)
    }
}