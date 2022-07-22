package com.paylivre.sdk.gateway.android.data

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.services.log.LogErrorServiceImplTest
import org.junit.Assert
import org.junit.Test

class RemoteDataSourceTest {


    @Test
    fun `CASE 01, getPixApprovalTime given ON_RESPONSE success with valid data`() {
        val mockPixApprovalTimeResponse = getMockPixApprovalTimeResponse()
        val rawResponseBody = Gson().toJson(mockPixApprovalTimeResponse)
        val mockCallResponseBody = MockCallResponseBody(
            code = 200,
            rawResponseBodySuccess = rawResponseBody,
            enqueueCallback = EnqueueCallbackTypes.ON_RESPONSE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(mockApiService = apiServiceMock)
        val (
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callGetPixApprovalTime()

        Assert.assertEquals(mockPixApprovalTimeResponse, dataResponseSuccess)
        Assert.assertEquals(null, dataResponseFailure)
    }

    @Test
    fun `CASE 02, getPixApprovalTime given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val (
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callGetPixApprovalTime()

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (api/v2/gateway/averagePixApprovalTime)"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable", "generic error OnFailure"),
            Pair("error", "error_request_not_connect_server")
        )

        val expectedListOfAddedSentryBreadcrumb = remoteDataSourceTestUtils.getListOfAddedSentryBreadcrumb(
            Pair("error_throwable_average_pix_approval_time", "generic error OnFailure")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfAddedSentryBreadcrumb, logErrorService.listOfAddedSentryBreadcrumb)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 03, getPixApprovalTime given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val (
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callGetServicesStatus()

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (api/v2/gateway/deposit-status)"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable_gateway", "generic error OnFailure"),
            Pair("error_gateway", "error_request_not_connect_server")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 04, newTransaction given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val orderDataRequest = getOrderDataRequest()
        val (
            orderDataRequestReceived,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callNewTransaction(orderDataRequest)

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (api/v2/gateway)"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable_gateway", "generic error OnFailure"),
            Pair("error_gateway", "error_request_not_connect_server")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 05, checkStatusDeposit given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val (
            _,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callCheckStatusDeposit(12345)

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (api/v2/transaction/deposit/status/{id})"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable_status_deposit", "generic error OnFailure"),
            Pair("error_status_deposit", "error_request_not_connect_server"),
            Pair("request_url_status_deposit", "(/api/v2/transaction/deposit/status/12345)")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 06, checkStatusTransaction given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val (
            _,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callCheckStatusTransaction(12345)

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (api/v2/transaction/status/{id})"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable_status_transaction", "generic error OnFailure"),
            Pair("error_status_transaction", "error_request_not_connect_server"),
            Pair("request_url_status_transaction", "(/api/v2/transaction/status/12345)")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 07, transferProof given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val insertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val (
            _,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callTransferProof(insertTransferProofDataRequest)

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (/api/v2/gateway/{order_id}/transfer-proof)"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_throwable_transfer_proof", "generic error OnFailure"),
            Pair("error_transfer_proof", "error_request_not_connect_server"),
            Pair("request_url_transfer_proof", "(/api/v2/gateway/12345/transfer-proof)")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 08, checkStatusOrder given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_FAILURE
        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val checkStatusOrderDataRequest = getMockCheckStatusOrderDataRequest()
        val (
            _,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callCheckStatusOrder(checkStatusOrderDataRequest)

        val expectedListOfCapturedMessages = remoteDataSourceTestUtils.getListOfCapturedMessages(
            "ERROR_API (/api/v2/gateway/status/{order_id}/{token})"
        )

        val expectedListOfCapturedExtras = remoteDataSourceTestUtils.getListOfCapturedExtras(
            Pair("error_check_status_order", "generic error OnFailure"),
            Pair("error_check_status_order", "error_request_not_connect_server"),
            Pair("error_check_status_order", "(/api/v2/gateway/status/12345/asd456123asd)")
        )

        Assert.assertEquals(null, dataResponseSuccess)
        Assert.assertEquals("error_request_not_connect_server", dataResponseFailure?.message)
        Assert.assertEquals(expectedListOfCapturedExtras, logErrorService.listOfCapturedExtras)
        Assert.assertEquals(expectedListOfCapturedMessages, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `CASE 09, transferProof given onFailure with error generic`() {
        val logErrorService = LogErrorServiceImplTest()
        val mockTransferProofDataResponse = getMockTransferProofResponse()
        val rawResponseBody = Gson().toJson(mockTransferProofDataResponse)
        val mockCallResponseBody = MockCallResponseBody(
            enqueueCallback = EnqueueCallbackTypes.ON_RESPONSE,
            rawResponseBodySuccess = rawResponseBody

        )
        val apiServiceMock = ApiServiceMock(mockCallResponseBody = mockCallResponseBody)
        val remoteDataSourceTestUtils = RemoteDataSourceTestUtils(
            mockApiService = apiServiceMock,
            logErrorService = logErrorService,
        )
        val insertTransferProofDataRequest = getMockInsertTransferProofDataRequest()
        val (
            _,
            dataResponseSuccess,
            dataResponseFailure,
        ) = remoteDataSourceTestUtils.callTransferProof(insertTransferProofDataRequest)

        Assert.assertEquals(mockTransferProofDataResponse, dataResponseSuccess)
        Assert.assertEquals(null, dataResponseFailure)

    }
}