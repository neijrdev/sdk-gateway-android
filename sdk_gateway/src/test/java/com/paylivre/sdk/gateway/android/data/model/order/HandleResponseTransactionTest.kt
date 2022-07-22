package com.paylivre.sdk.gateway.android.data.model.order

import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.MockCallResponseBody
import com.paylivre.sdk.gateway.android.data.RemoteDataSourceTestUtils
import com.paylivre.sdk.gateway.android.data.getGenericErrorData
import com.paylivre.sdk.gateway.android.domain.model.DataStartCheckout
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImplTest
import com.paylivre.sdk.gateway.android.services.log.LogErrorServiceImplTest
import org.junit.Assert
import org.junit.Test

class HandleResponseTransactionTest {
    @Test
    fun `getErrorTags422 with error = null`() {
        val getErrorTags422Result = getErrorTags422()
        Assert.assertEquals("", getErrorTags422Result)
    }

    @Test
    fun `getErrorTags422 with errors = null`() {
        val errorTransaction = ErrorTransaction(
            errors = null
        )
        val getErrorTags422Result = getErrorTags422(errorTransaction)
        Assert.assertEquals("", getErrorTags422Result)
    }

    @Test
    fun `getErrorResponseJson, given error is ErrorTransactionTwo`() {
        val errorTransactionTwo = ErrorTransactionTwo(message = "error_transaction_two")
        val rawResponseBodyError = Gson().toJson(errorTransactionTwo)
        val mockCallResponseBody =
            MockCallResponseBody(code = 500, rawResponseBodyError = rawResponseBodyError)
        val getErrorResponseJsonResult = getErrorResponseJson(mockCallResponseBody.execute())
        Assert.assertEquals(ErrorTransaction(message = "error_transaction_two"),
            getErrorResponseJsonResult)
    }


    @Test
    fun `getErrorResponseJson, given errorBody is json unexpected`() {
        val rawResponseBodyError = "{\"unexpected_error\":\"unexpected\"}"
        val mockCallResponseBody =
            MockCallResponseBody(code = 500, rawResponseBodyError = rawResponseBodyError)
        val getErrorResponseJsonResult = getErrorResponseJson(mockCallResponseBody.execute())
        Assert.assertEquals(ErrorTransaction(), getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorResponseJson, given errorBody is string unexpected`() {
        val rawResponseBodyError = "unexpected_error:unexpected"
        val mockCallResponseBody =
            MockCallResponseBody(code = 500, rawResponseBodyError = rawResponseBodyError)
        val getErrorResponseJsonResult = getErrorResponseJson(mockCallResponseBody.execute())
        Assert.assertEquals(getGenericErrorData(),
            getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorResponseJson, given errorBody is null`() {
        val mockCallResponseBody = MockCallResponseBody(code = 200)
        val getErrorResponseJsonResult = getErrorResponseJson(mockCallResponseBody.execute())
        Assert.assertEquals(getGenericErrorData(),
            getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorCodeWithTagResponseJson, given message Invalid signature`() {
        //GIVEN
        val rawResponseBodyError = "{\"message\":\"Invalid signature.\"}"
        val mockCallResponseBody =
            MockCallResponseBody(code = 401, rawResponseBodyError = rawResponseBodyError)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorCodeWithTagResponseJson(mockCallResponseBody.execute())

        //THEN
        Assert.assertEquals(ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = null,
            error = null,
            errors = null,
            errorTags = "UC002",
            original_message = "Invalid signature."
        ),
            getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorCodeWithTagResponseJson, given errorBody() is null`() {
        //GIVEN
        val mockCallResponseBody = MockCallResponseBody(code = 200)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorCodeWithTagResponseJson(mockCallResponseBody.execute())

        //THEN
        Assert.assertEquals(ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = null,
            error = null,
            errors = null,
            errorTags = "UX000",
            original_message = "title_unexpected_error"
        ),
            getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorCode400ResponseJson, given errorBody() is null`() {
        //GIVEN
        val mockCallResponseBody = MockCallResponseBody(code = 200)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorCode400ResponseJson(mockCallResponseBody.execute())

        //THEN
        Assert.assertEquals(ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = "",
            error = null,
            errors = null,
            errorTags = null,
            original_message = "title_unexpected_error"
        ),
            getErrorResponseJsonResult)
    }

    @Test
    fun `getErrorDataByCode, given code response is 401`() {
        //GIVEN
        val
                logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(code = 401)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorDataByCode(mockCallResponseBody.execute(), logErrorService = logErrorService)

        //THEN
        val errorTransactionExpected = ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = null,
            error = null,
            errors = null,
            errorTags = "UB104",
            original_message = null
        )
        Assert.assertEquals(errorTransactionExpected,
            getErrorResponseJsonResult)
        //test sentry logs
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedExtras(
            Pair("response_error_data", errorTransactionExpected.toString())
        )
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedExtras)
    }


    @Test
    fun `getErrorDataByCode, given code response is 403`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(code = 403)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorDataByCode(mockCallResponseBody.execute(), logErrorService = logErrorService)

        //THEN
        val errorTransactionExpected = ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = null,
            error = null,
            errors = null,
            errorTags = "UX000",
            original_message = "title_unexpected_error"
        )
        Assert.assertEquals(errorTransactionExpected,
            getErrorResponseJsonResult)
        //test sentry logs
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedExtras(
            Pair("response_error_data", errorTransactionExpected.toString())
        )
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedExtras)
    }

    @Test
    fun `getErrorDataByCode, given code response is 404`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(code = 404)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorDataByCode(mockCallResponseBody.execute(), logErrorService = logErrorService)

        //THEN
        val errorTransactionExpected = ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = null,
            error = null,
            errors = null,
            errorTags = "UX000",
            original_message = "title_unexpected_error"
        )
        Assert.assertEquals(errorTransactionExpected,
            getErrorResponseJsonResult)

        //test sentry logs
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedExtras(
            Pair("response_error_data", errorTransactionExpected.toString())
        )
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedExtras)
    }

    @Test
    fun `getErrorDataByCode, given code response is 500`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(code = 500)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorDataByCode(mockCallResponseBody.execute(), logErrorService = logErrorService)

        //THEN
        val errorTransactionExpected = ErrorTransaction(
            message = "title_unexpected_error",
            messageDetails = "title_unexpected_error_body",
            error = null,
            errors = null,
            errorTags = "UX005",
            original_message = "title_unexpected_error"
        )
        Assert.assertEquals(errorTransactionExpected, getErrorResponseJsonResult)

        //test sentry logs
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedExtras(
            Pair("response_error_data", errorTransactionExpected.toString())
        )
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedExtras)

    }

    @Test
    fun `getErrorDataByCode, given code response is unexpected`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val mockCallResponseBody = MockCallResponseBody(code = 501)

        //WHEN
        val getErrorResponseJsonResult =
            getErrorDataByCode(mockCallResponseBody.execute(), logErrorService = logErrorService)

        //THEN
        val errorTransactionExpected = ErrorTransaction(
            message = "title_unexpected_error",
            messageDetails = "title_unexpected_error_body",
            error = null,
            errors = null,
            errorTags = "UX000",
            original_message = "title_unexpected_error"
        )
        Assert.assertEquals(errorTransactionExpected,
            getErrorResponseJsonResult)

        //test sentry logs
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedExtras(
            Pair("response_error_data", errorTransactionExpected.toString())
        )
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedExtras)
    }

    @Test
    fun `getSelectedTypeByType, given type is unexpected`() {
        val getSelectedTypeByType = getSelectedTypeByType(-1)
        Assert.assertEquals("", getSelectedTypeByType)
    }

    @Test
    fun `getDataWithOnlySelectedType, given selectedType is empty`() {
        val mockDataGenerateSignature = DataGenerateSignature(
            "", 0, "", "",
            "", "", "", "", "-1",
            "", "", "", "",
            "", "", "", "", 0
        )
        val getDataWithOnlySelectedType = getDataWithOnlySelectedType(mockDataGenerateSignature)
        Assert.assertEquals(DataGenerateSignature(
            "", 0, "", "",
            "", "", "", "", "-1",
            "", "", "", "",
            "", "", "", "", 0
        ), getDataWithOnlySelectedType)
    }

    @Test
    fun `getDataAutoRequestUrlWithSelectedType, given selectedType is valid`() {
        val mockDataStartCheckout = DataStartCheckout(
            0, "", 0, "",
            0, "", 0, "", "",
            "", "", "", 0,
            "", "", "", "", 0
        )
        val getDataWithOnlySelectedType =
            getDataAutoRequestUrlWithSelectedType(mockDataStartCheckout)
        val orderDataRequestExpected = OrderDataRequest(
            "", 0, "", "0",
            "", "0", "", "0", "",
            "", "", "", "",
            "", "0", "null", "", "",
            auto_approve = "0", redirect_url = "", logo_url = ""
        )
        Assert.assertEquals(
            orderDataRequestExpected.toString(), getDataWithOnlySelectedType.toString()
        )
    }

    @Test
    fun `handleResponseTransaction, given catch error response invalid`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val logEventsService = LogEventsServiceImplTest()
        val mockResponseRaw = "invalid data"
        val mockCallResponseBody =
            MockCallResponseBody(code = 200, rawResponseBodySuccess = mockResponseRaw)
        val mockOrderDataRequest = OrderDataRequest(
            "", 0, "", "0",
            "", "0", "", "0", "",
            "", "", "", "",
            "", "0", "null", "", "",
            auto_approve = "0", redirect_url = "", logo_url = ""
        )

        var dataResponseReceived: ResponseCommonTransactionData? = null
        var dataErrorReceived: ErrorTransaction? = null
        fun onResponse(data: ResponseCommonTransactionData?, error: ErrorTransaction?) {
            dataResponseReceived = data
            dataErrorReceived = error
        }
        handleResponseTransaction(
            dataRequest = mockOrderDataRequest,
            response = mockCallResponseBody.execute(),
            onResponse = ::onResponse,
            logEventsService = logEventsService,
            logErrorService = logErrorService,
        )
        val errorTransactionExpected = ErrorTransaction(
            message = "title_unexpected_error",
            messageDetails = "title_unexpected_error_body",
            errorTags = "UX000"
        )
        Assert.assertEquals(errorTransactionExpected, dataErrorReceived)
        Assert.assertEquals(null, dataResponseReceived)

        //test sentry logs
        val listOfListOfCapturedExtrasExpected =
            RemoteDataSourceTestUtils().getListOfCapturedExtras(
                Pair("error_catch_gateway",
                    "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path \$"),
                Pair("error_generic_gateway",
                    "{\"message\":\"title_unexpected_error\",\"messageDetails\":\"title_unexpected_error_body\",\"errorTags\":\"UX000\"}")
            )
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedMessages(
            "ERROR_API | status_code: 200 (api/v2/gateway)"
        )
        Assert.assertEquals(listOfListOfCapturedExtrasExpected,
            logErrorService.listOfCapturedExtras)
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedMessages)
    }

    @Test
    fun `handleResponseTransaction, given response error`() {
        //GIVEN
        val logErrorService = LogErrorServiceImplTest()
        val logEventsService = LogEventsServiceImplTest()
        val mockResponseRaw = "{\"message\":\"invalid data\"}"
        val mockCallResponseBody =
            MockCallResponseBody(code = 400, rawResponseBodySuccess = mockResponseRaw)
        val mockOrderDataRequest = OrderDataRequest(
            "", 0, "", "0",
            "", "0", "", "0", "",
            "", "", "", "",
            "", "0", "null", "", "",
            auto_approve = "0", redirect_url = "", logo_url = ""
        )

        var dataResponseReceived: ResponseCommonTransactionData? = null
        var dataErrorReceived: ErrorTransaction? = null
        fun onResponse(data: ResponseCommonTransactionData?, error: ErrorTransaction?) {
            dataResponseReceived = data
            dataErrorReceived = error
        }
        handleResponseTransaction(
            dataRequest = mockOrderDataRequest,
            response = mockCallResponseBody.execute(),
            onResponse = ::onResponse,
            logEventsService = logEventsService,
            logErrorService = logErrorService,
        )
        val errorTransactionExpected = ErrorTransaction(
            message = "invalid_data_error",
            messageDetails = "",
            original_message = "title_unexpected_error",
        )
        Assert.assertEquals(errorTransactionExpected, dataErrorReceived)
        Assert.assertEquals(null, dataResponseReceived)

        //test sentry logs
        val requestBodyJsonErrorReceived =
            filterPasswordFromRequest(mockOrderDataRequest)

        val listOfListOfCapturedExtrasExpected =
            RemoteDataSourceTestUtils().getListOfCapturedExtras(
                Pair("request_body_json_gateway", requestBodyJsonErrorReceived.toString())
            )
        val listOfCapturedMessagesExpected = RemoteDataSourceTestUtils().getListOfCapturedMessages(
            "ERROR_API | status_code: 400 (api/v2/gateway)"
        )
        Assert.assertEquals(listOfListOfCapturedExtrasExpected,
            logErrorService.listOfCapturedExtras)
        Assert.assertEquals(listOfCapturedMessagesExpected, logErrorService.listOfCapturedMessages)

        //test sentry logs: error with context
        Assert.assertEquals(listOf(Pair("status_code_gateway", "400")),
            logErrorService.logErrorScopeReceived?.getTags())
        Assert.assertEquals(listOf(Pair("request_body_gateway", requestBodyJsonErrorReceived)),
            logErrorService.logErrorScopeReceived?.getContexts())
    }

    @Test
    fun `test data class Bank`() {
        val bank = Bank(
            id = 1,
            country_cca3 = "123456",
            name = "test",
            number = "123",
            website = "http://banktest.com",
            display = 1,
            blacklisted = 0
        )

        Assert.assertEquals("Bank(id=1, country_cca3=123456, name=test, number=123, website=http://banktest.com, display=1, blacklisted=0)",
            bank.toString())
    }

    @Test
    fun `test data class ErrorData`() {
        val errorData = ErrorData(type = "type test")
        Assert.assertEquals("ErrorData(type=type test)", errorData.toString())
    }

    @Test
    fun `getErrorCommon, errorStag errorTag is null`() {
        val mockCallResponseBody = MockCallResponseBody(code = 400)
        val errorCommon =
            getErrorCommon(response = mockCallResponseBody.execute(), errorStag = null)
        val expectedErrorTransaction = ErrorTransaction(
            message = "title_unexpected_error",
            messageDetails = "title_unexpected_error_body",
            original_message = "title_unexpected_error"
        )
        Assert.assertEquals(expectedErrorTransaction, errorCommon)
    }

}