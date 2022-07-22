package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getOrderDataRequest
import com.paylivre.sdk.gateway.android.data.getMockNewTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import org.junit.Assert
import org.junit.Test

class NewTransactionTest {

    @Test
    fun `CASE 01, tests if newTransaction calls newTransaction from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callNewTransaction(getOrderDataRequest())

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledNewTransaction)
    }

    @Test
    fun `CASE 02, checks if the orderDataRequest is passed correctly`() {
        //GIVEN
        val mockOrderDataRequest = getOrderDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockNewTransactionResponse = getMockNewTransactionResponse()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (orderDataRequestReceived) = paymentRepositoryTestUtils.callNewTransaction(
            mockOrderDataRequest)

        //THEN
        //check orderDataRequest received on remoteDataSource
        Assert.assertEquals(mockOrderDataRequest, mockRemoteDataSource.orderDataRequestReceived)
        //check orderDataRequest received on PaymentRepository
        Assert.assertEquals(mockOrderDataRequest, orderDataRequestReceived)
    }

    @Test
    fun `CASE 03, when newTransaction from RemoteDataSource returns success`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockNewTransactionResponse = getMockNewTransactionResponse()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            newTransactionSuccessReceived,
            newTransactionFailureReceived,
        ) = paymentRepositoryTestUtils.callNewTransaction(getOrderDataRequest())

        //THEN
        Assert.assertEquals(null, newTransactionFailureReceived)
        Assert.assertEquals(getMockNewTransactionResponse(), newTransactionSuccessReceived)
    }

    @Test
    fun `CASE 04, when newTransaction from RemoteDataSource returns failure`() {
        //GIVEN
        val errorTransaction = ErrorTransaction(original_message = "error transaction")
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockNewTransactionResponse = null,
            mockResponseErrorTransaction = errorTransaction
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            newTransactionSuccessReceived,
            newTransactionFailureReceived,
        ) = paymentRepositoryTestUtils.callNewTransaction(getOrderDataRequest())

        //THEN
        Assert.assertEquals(errorTransaction, newTransactionFailureReceived)
        Assert.assertEquals(null, newTransactionSuccessReceived)
    }

    @Test
    fun `CASE 05, when newTransaction run catch`() {
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "newTransaction run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            _,
            getServiceStatusFailureReceived,
        ) = paymentRepositoryTestUtils.callNewTransaction()

        Assert.assertEquals(
            ErrorTransaction("newTransaction run catch error!", null, null),
            getServiceStatusFailureReceived)
    }
}