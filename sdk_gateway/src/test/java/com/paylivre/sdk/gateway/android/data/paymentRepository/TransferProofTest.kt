package com.paylivre.sdk.gateway.android.data.paymentRepository

import com.paylivre.sdk.gateway.android.data.getMockInsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.getMockTransferProofResponse
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import org.junit.Assert
import org.junit.Test

class TransferProofTest {
    @Test
    fun `CASE 01, tests if transferProof calls TransferProof from RemoteDataSource`() {
        //GIVEN
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        paymentRepositoryTestUtils.callTransferProof(getMockInsertTransferProofDataRequest())

        //THEN
        Assert.assertTrue(mockRemoteDataSource.calledTransferProof)
    }

    @Test
    fun `CASE 02, checks if the transferProof is passed correctly`() {
        //GIVEN
        val mockInsertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock()
        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        val (transferProofDataRequestReceived) = paymentRepositoryTestUtils.callTransferProof(
            mockInsertTransferProofDataRequest)

        //THEN
        Assert.assertEquals(mockInsertTransferProofDataRequest, transferProofDataRequestReceived)
        Assert.assertEquals(mockInsertTransferProofDataRequest,
            mockRemoteDataSource.transferProofDataRequestReceived)
    }

    @Test
    fun `CASE 03, when transferProof from RemoteDataSource returns success`() {
        //GIVEN
        val mockInsertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockTransferProofResponse = getMockTransferProofResponse()
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkTransferProofResponseReceived,
            checkTransferProofResponseFailureReceived,
        ) = paymentRepositoryTestUtils.callTransferProof(mockInsertTransferProofDataRequest)

        //THEN
        Assert.assertEquals(null, checkTransferProofResponseFailureReceived)
        Assert.assertEquals(getMockTransferProofResponse(), checkTransferProofResponseReceived)
    }

    @Test
    fun `CASE 04, when transferProof from RemoteDataSource returns failure`() {
        //GIVEN
        val errorThrowable = RuntimeException("TransferProof error!")
        val mockInsertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            mockTransferProofResponse = null,
            mockResponseThrowable = errorThrowable
        )

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        //WHEN
        var (
            _,
            checkTransferProofResponseReceived,
            checkTransferProofResponseFailureReceived,
        ) = paymentRepositoryTestUtils.callTransferProof(mockInsertTransferProofDataRequest)

        //THEN
        Assert.assertEquals(errorThrowable, checkTransferProofResponseFailureReceived)
        Assert.assertEquals(null, checkTransferProofResponseReceived)
    }

    @Test
    fun `CASE 05, when transferProof run catch`() {
        val mockInsertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val mockRemoteDataSource = RemoteDataSourceMock(
            runErrorRuntimeGeneric = true,
            messageRunErrorRuntimeGeneric = "transferProof run catch error!")

        var paymentRepositoryTestUtils = PaymentRepositoryTestUtils(mockRemoteDataSource)

        var (
            _,
            _,
            checkTransferProofFailureReceived,
        ) = paymentRepositoryTestUtils.callTransferProof(mockInsertTransferProofDataRequest)

        Assert.assertEquals(
            "transferProof run catch error!",
            checkTransferProofFailureReceived?.message)
    }

}