package com.paylivre.sdk.gateway.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.DataPixApprovalTime
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
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

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetPixApprovalTimeTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var fileTestsUtils = FileTestsUtils()

    @Test
    fun `test mainViewModel getServicesStatus success`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        val responseExpectedString = fileTestsUtils
            .loadJsonAsString("res_get_pix_approvaltime_success.json")

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseExpectedString)
        )
        val mockMainViewModel = MockMainViewModel(server)


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.getPixApprovalTime()

        val responseDataExpected =
            Gson().fromJson(responseExpectedString, PixApprovalTimeResponse::class.java)


        //THEN
        val request1 = server.takeRequest()


        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway/averagePixApprovalTime", request1.path)


        //check the value returned to livedata
        Assert.assertEquals(
            responseDataExpected,
            mockMainViewModel.mainViewModel.pixApprovalTime.getOrAwaitValueTest()
        )

        server.shutdown()
    }


    @Test
    fun `test mainViewModel getServicesStatus error api generic`() = runBlocking {
        //GIVEN
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse()
                .setResponseCode(500)
        )
        val mockMainViewModel = MockMainViewModel(server)


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.getPixApprovalTime()


        //THEN
        val request1 = server.takeRequest()


        //check Endpoint path is correct
        Assert.assertEquals("/api/v2/gateway/averagePixApprovalTime", request1.path)


        //check the value returned to livedata
        Assert.assertEquals(
            PixApprovalTimeResponse(
                status = "error",
                status_code = 0,
                message = "",
                data = null,
            ),
            mockMainViewModel.mainViewModel.pixApprovalTime.getOrAwaitValueTest()
        )

        server.shutdown()
    }

    @Test
    fun `test mainViewModel setPixApprovalTime when data = null`() = runBlocking {

        val mockMainViewModel = MockMainViewModel()


        //WHEN
        //Check Deposit Status
        mockMainViewModel.mainViewModel.setPixApprovalTime(null)


        //check the value returned to livedata
        Assert.assertEquals(
            PixApprovalTimeResponse(
                "", 0,
                "", null
            ),
            mockMainViewModel.mainViewModel.pixApprovalTime.getOrAwaitValueTest()
        )
    }
}