package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class InsertTransferProofTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    @Test
    fun `test mainViewModel insertTransferProof request and failure`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("")
        )

        val mockMainViewModel = MockMainViewModel(server)


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.insertTransferProof(
            InsertTransferProofDataRequest(
                file = File("brasil.png"),
                order_id = 1,
                token = "12345d6as4d65as4fd564fsgd56fa564"
            )
        )

        //check the value returned to livedata
        Assert.assertEquals(
            InsertTransferProofDataResponse(
                null,
                null,
                null,
                null,
                loading = true,
                error = null,
                isSuccess = null,
                deposit_status_id = null
            ),
            mockMainViewModel.mainViewModel.transfer_proof_response.getOrAwaitValueTest()
        )

        //Add timeout to wait mocked request data
        TimeUnit.SECONDS.sleep(1);

        Assert.assertEquals(
            InsertTransferProofDataResponse(
                null,
                null,
                null,
                null,
                loading = false,
                error = "error_request_not_connect_server",
                isSuccess = false,
                deposit_status_id = null
            ),
            mockMainViewModel.mainViewModel.transfer_proof_response.getOrAwaitValueTest()
        )
        server.shutdown()
    }


    @Test
    fun `test mainViewModel insertTransferProofSuccess`() = runBlocking {
        val mockMainViewModel = MockMainViewModel()

        val responseExpectedString = fileTestsUtils
            .loadJsonAsString("res_insert_transfer_proof_success.json")

        var responseDataExpected =
            Gson().fromJson(responseExpectedString, InsertTransferProofDataResponse::class.java)

        mockMainViewModel.mainViewModel.insertTransferProofSuccess(
            responseDataExpected
        )

        responseDataExpected.isSuccess = true
        responseDataExpected.loading = false

        //check the value returned to livedata
        Assert.assertEquals(
            responseDataExpected,
            mockMainViewModel.mainViewModel.transfer_proof_response.getOrAwaitValueTest()
        )
    }
}