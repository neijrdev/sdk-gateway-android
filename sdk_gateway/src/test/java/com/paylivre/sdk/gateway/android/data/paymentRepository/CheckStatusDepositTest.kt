package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getMockCheckStatusDeposit
import com.paylivre.sdk.gateway.android.data.getOrderDataRequest
import com.paylivre.sdk.gateway.android.data.getMockNewTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import org.junit.Assert
import org.junit.Test

class CheckStatusDepositTest {
    @Test
    fun `CASE 01, tests if checkStatusDeposit calls checkStatusDeposit from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callCheckStatusDeposit(-1)

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledCheckStatusDeposit)
    }

    @Test
    fun `CASE 02, checks if the depositId is passed correctly`() {
        //GIVEN
        val mockDepositId = 123456
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        val (depositIdReceived) = paymentRepositoryTestUtils.callCheckStatusDeposit(mockDepositId)

        //THEN
        Assert.assertEquals(mockDepositId, depositIdReceived)
        Assert.assertEquals(mockDepositId, mockRemoteDataSource.depositIdReceived)
    }

    @Test
    fun `CASE 03, when checkStatusDeposit from RemoteDataSource returns success`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusDepositResponse = getMockCheckStatusDeposit()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusDepositSuccessReceived,
            checkStatusDepositFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusDeposit()

        //THEN
        Assert.assertEquals(null, checkStatusDepositFailureReceived)
        Assert.assertEquals(getMockCheckStatusDeposit(), checkStatusDepositSuccessReceived)
    }

    @Test
    fun `CASE 04, when checkStatusDeposit from RemoteDataSource returns failure`() {
        //GIVEN
        val errorTransaction =
            ErrorTransaction(original_message = "error transaction checkStatusDeposit")
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockCheckStatusDepositResponse = null,
            mockResponseErrorTransaction = errorTransaction
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkStatusDepositSuccessReceived,
            checkStatusDepositFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusDeposit()

        //THEN
        Assert.assertEquals(errorTransaction, checkStatusDepositFailureReceived)
        Assert.assertEquals(null, checkStatusDepositSuccessReceived)
    }

    @Test
    fun `CASE 05, when checkStatusDeposit run catch`() {
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "checkStatusDeposit run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            _,
            checkStatusDepositFailureReceived,
        ) = paymentRepositoryTestUtils.callCheckStatusDeposit()

        Assert.assertEquals(
            ErrorTransaction("checkStatusDeposit run catch error!", null, null),
            checkStatusDepositFailureReceived)
    }
}