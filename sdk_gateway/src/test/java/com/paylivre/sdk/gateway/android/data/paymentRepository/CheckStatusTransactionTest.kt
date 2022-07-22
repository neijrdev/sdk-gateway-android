package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getMockCheckStatusTransaction
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import org.junit.Assert
import org.junit.Test

class CheckStatusTransactionTest {
    @Test
    fun `CASE 01, tests if checkStatusTransaction calls checkStatusTransaction from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callCheckStatusTransaction(-1)

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledCheckStatusTransaction)
    }

    @Test
    fun `CASE 02, checks if the transactionId is passed correctly`() {
        //GIVEN
        val mockTransactionId = 123456
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        val (transactionIdReceived) = paymentRepositoryTestUtils.callCheckStatusTransaction(mockTransactionId)

        //THEN
        Assert.assertEquals(mockTransactionId, transactionIdReceived)
        Assert.assertEquals(mockTransactionId, mockRemoteDataSource.transactionIdReceived)
    }

    @Test
    fun `CASE 03, when checkStatusTransaction from RemoteDataSource returns success`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusTransactionResponse = getMockCheckStatusTransaction()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusTransactionSuccessReceived,
            checkStatusTransactionFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusTransaction()

        //THEN
        Assert.assertEquals(null, checkStatusTransactionFailureReceived)
        Assert.assertEquals(getMockCheckStatusTransaction(), checkStatusTransactionSuccessReceived)
    }

    @Test
    fun `CASE 04, when checkStatusTransaction from RemoteDataSource returns failure`() {
        //GIVEN
        val errorTransaction =
            ErrorTransaction(original_message = "error transaction checkStatusTransaction")
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusTransactionResponse = null,
            mockResponseErrorTransaction = errorTransaction
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusTransactionSuccessReceived,
            checkStatusTransactionFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusDeposit()

        //THEN
        Assert.assertEquals(errorTransaction, checkStatusTransactionFailureReceived)
        Assert.assertEquals(null, checkStatusTransactionSuccessReceived)
    }

    @Test
    fun `CASE 05, when checkStatusTransaction run catch`() {
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "checkStatusTransaction run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            _,
            checkStatusTransactionFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusTransaction()

        Assert.assertEquals(
            ErrorTransaction("checkStatusTransaction run catch error!", null, null),
            checkStatusTransactionFailureReceived)
    }
}