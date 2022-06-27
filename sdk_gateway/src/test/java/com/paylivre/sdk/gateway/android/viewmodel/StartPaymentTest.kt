package com.paylivre.sdk.gateway.android.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.CoroutineTestRule
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class StartPaymentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()


    //Mock Base64
    @Before
    fun `Bypass android_util_Base64 to java_util_Base64`() {
        mockkStatic(Base64::class)
        val arraySlot = slot<ByteArray>()

        every {
            Base64.encodeToString(capture(arraySlot), Base64.DEFAULT)
        } answers {
            java.util.Base64.getEncoder().encodeToString(arraySlot.captured)
        }

        val stringSlot = slot<String>()
        every {
            Base64.decode(capture(stringSlot), Base64.DEFAULT)
        } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }
    }

    var fileTestsUtils = FileTestsUtils()

    @Test
    fun `test mainViewModel startPaymentByParams - Deposit Pix Success`() = runBlocking {

        //GIVEN
        val server = MockWebServer()
        server.start()

        val responseExpectedString =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseExpectedString)
        )

        val mockMainViewModel = MockMainViewModel(server)

        val dataMock = DataGenerateSignature(
            base_url = "https://dev.gateway.paylivre.com",
            merchant_id = 19,
            gateway_token = "teste",
            merchant_transaction_id = "123456",
            amount = 500.toString(),
            currency = "BRL",
            operation = Operation.DEPOSIT.code.toString(),
            callback_url = "https://www.google.com",
            type = TypesToSelect.PIX.code.toString(),
            selected_type = TypesToSelect.PIX.code.toString(),
            account_id = "1234567888",
            email = "test@test.com",
            document_number = "60712326006",
            login_email = "",
            api_token = "",
            pix_key = "",
            pix_key_type = "",
            auto_approve = 1
        )

        val argon2iHashService: Argon2iHash = mockk()
        val expectedHashArgon2iHash =
            "\$argon2i\$v=19\$m=16,t=2,p=1\$NndoSFd4Sm9XQWlmaTU0bQ\$SpA8z9AnfenYeAga+jWb4g"

        coEvery {
            argon2iHashService.generateArgon2iHash(
                any(),
                any()
            )
        } returns expectedHashArgon2iHash

        val generateSignatureService = GenerateSignature(argon2iHashService)

        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.startPayment(
            dataMock,
            generateSignatureService
        )

        //THEN
        val request1 = server.takeRequest()
        val requestExpected =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")
        val requestBody = Gson().fromJson(request1.body.readUtf8(), OrderDataRequest::class.java)
        val expectedRequestBody = Gson().fromJson(requestExpected, OrderDataRequest::class.java)

        val responseDataExpected =
            Gson().fromJson(responseExpectedString, ResponseCommonTransactionData::class.java)

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = responseDataExpected,
            error = null,
        )

        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway", request1.path)

        //check Request data is correct
        Assert.assertEquals(expectedRequestBody, requestBody)

        //Add timeout to wait mocked request data
        TimeUnit.SECONDS.sleep(1);

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mockMainViewModel.mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )

        server.shutdown()
    }

    @Test
    fun `test mainViewModel setStatusTransactionResponse when data valid`() = runBlocking {
        val mockMainViewModel = MockMainViewModel()

        val responseExpectedString =
            fileTestsUtils.loadJsonAsString("request_deposit_pix_transaction.json")

        val responseDataExpected =
            Gson().fromJson(responseExpectedString, ResponseCommonTransactionData::class.java)

        val responseExpected = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = responseDataExpected,
            error = null,
        )

        mockMainViewModel.mainViewModel.setStatusTransactionResponse(responseExpected)

        //check the value returned to livedata
        Assert.assertEquals(
            responseExpected,
            mockMainViewModel.mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )
    }

    @Test
    fun `test mainViewModel setStatusTransactionResponse when data = null`() = runBlocking {
        val mockMainViewModel = MockMainViewModel()
        mockMainViewModel.mainViewModel.setStatusTransactionResponse(null)

        //check the value returned to livedata
        Assert.assertEquals(
            StatusTransactionResponse(null, null, null, null),
            mockMainViewModel.mainViewModel.statusResponseTransaction.getOrAwaitValueTest()
        )
    }

}