package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImplTest
import com.paylivre.sdk.gateway.android.services.postdelayed.PostDelayedImpl
import com.paylivre.sdk.gateway.android.services.postdelayed.PostDelayedService
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class WithdrawLoadingFragmentTest {

    var fileTestsUtils = FileTestsUtils()

    private var statusTransactionResponseMock = StatusTransactionResponse(
        isLoading = false,
        isSuccess = false,
        data = null,
        error = null
    )

    private var checkStatusOrderResponseMock = CheckStatusOrderResponse(
        isLoading = false,
        isSuccess = false,
        data = null,
        error = null
    )

    interface PostDelayedServiceMock : PostDelayedService {
        var timerIntervalSpy: Long?
        var runnableSpy: Runnable?
        override fun postDelayed(runnable: Runnable, timerInterval: Long)

        fun postRunnable() {
            runnableSpy?.run()
        }
    }

    data class DataMockedInstances(
        val mockMainViewModel: MainViewModel,
        val logEventsServiceImpl: LogEventsService,
        val mockNavController: NavController,
        val postDelayedImplMock: PostDelayedImplMock,
    )

    class PostDelayedImplMock(private val isRunDelayOnStart: Boolean) : PostDelayedServiceMock {
        override var timerIntervalSpy: Long? = null
        override var runnableSpy: Runnable? = null

        override
        fun postDelayed(runnable: Runnable, timerInterval: Long) {
            timerIntervalSpy = timerInterval
            runnableSpy = runnable

            if (isRunDelayOnStart) {
                runnable.run()
            }
        }
    }


    private fun getMockedInstances(
        statusTransactionResponse: StatusTransactionResponse? = statusTransactionResponseMock,
        checkStatusOrderResponse: CheckStatusOrderResponse = checkStatusOrderResponseMock,
        mockNavController: NavController = mockk(),
        isRunDelayOnStart: Boolean = false,
    ): DataMockedInstances {
        val mockMainViewModel: MainViewModel = mockk()
        val logEventsServiceImpl = LogEventsServiceImplTest()
        val postDelayedImplMock = PostDelayedImplMock(isRunDelayOnStart)
        mockkStatic(Navigation::class)
        mockkStatic(Looper::class)

        loadKoinModules(module(override = true) {
            single<LogEventsService> {
                logEventsServiceImpl
            }
            viewModel {
                mockMainViewModel
            }
            single<PostDelayedService> {
                postDelayedImplMock
            }
        })

        coEvery {
            mockMainViewModel.language
        } returns MutableLiveData("pt_BR")

        coEvery {
            mockMainViewModel.statusResponseTransaction
        } returns MutableLiveData(statusTransactionResponse)

        coEvery {
            mockMainViewModel.checkStatusOrderDataResponse
        } returns MutableLiveData(checkStatusOrderResponse)

        every {
            Navigation.findNavController(any())
        } returns mockNavController

        every {
            mockNavController.navigate(any<Int>())
        } returns Unit

        every {
            mockMainViewModel.checkStatusOrder(any())
        } returns Unit


        return DataMockedInstances(
            mockMainViewModel,
            logEventsServiceImpl,
            mockNavController,
            postDelayedImplMock
        )

    }

    @Test
    fun `CASE 01, Withdraw type PIX, transaction = success and checkStatusOrder = success`() {

        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_pix_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)

        val checkStatusOrderResponseDataString =
            fileTestsUtils.loadJsonAsString("res_gateway_status_withdraw_pix_success.json")
        val checkStatusOrderResponseDataSuccess = Gson().fromJson(
            checkStatusOrderResponseDataString, CheckStatusOrderDataResponse::class.java)

        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = true,
            data = checkStatusOrderResponseDataSuccess,
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )

        val mockedInstances = getMockedInstances(
            statusTransactionResponse = statusTransactionResponse,
            checkStatusOrderResponse = checkStatusOrderResponse
        )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawLoadingFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawLoadingFragment ->
            //WHEN -> post Delayed is run
            mockedInstances.postDelayedImplMock.postRunnable()

            //THEN
            Assert.assertEquals(10000L, mockedInstances.postDelayedImplMock.timerIntervalSpy)

            verify {
                mockedInstances.mockMainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(17393,
                        "JDJ5JDEwJHIxVWk0RnhCNklLbVBTUG9KdHZWSWVFcmRVSE5XUTQ0MFBtL0JZZDk1dEFYYmxHMUV1TFRp"))
            }

            verify {
                mockedInstances.mockNavController.navigate(R.id.navigation_finish_screen_withdraw_pix)
            }
        }
    }

    @Test
    fun `CASE 02, Withdraw type PIX, transaction = success and checkStatusOrder = error`() {

        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_pix_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)


        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = false,
            data = null,
            error = ErrorTransaction(message = "Generic error")
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )

        val mockedInstances = getMockedInstances(
            statusTransactionResponse = statusTransactionResponse,
            checkStatusOrderResponse = checkStatusOrderResponse
        )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawLoadingFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawLoadingFragment ->
            //WHEN -> post Delayed is run
            mockedInstances.postDelayedImplMock.postRunnable()

            //THEN
            Assert.assertEquals(10000L, mockedInstances.postDelayedImplMock.timerIntervalSpy)

            verify {
                mockedInstances.mockMainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(17393,
                        "JDJ5JDEwJHIxVWk0RnhCNklLbVBTUG9KdHZWSWVFcmRVSE5XUTQ0MFBtL0JZZDk1dEFYYmxHMUV1TFRp"))
            }

            verify {
                mockedInstances.mockNavController.navigate(R.id.navigation_fatal_error)
            }
        }
    }

    @Test
            fun `CASE 03, Withdraw type Wallet, transaction = success and checkStatusOrder = success`() {

                val statusTransactionResponseDataString =
                    fileTestsUtils.loadJsonAsString("res_withdraw_wallet_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)

        val checkStatusOrderResponseDataString =
            fileTestsUtils.loadJsonAsString("res_gateway_status_withdraw_wallet_success.json")
        val checkStatusOrderResponseDataSuccess = Gson().fromJson(
            checkStatusOrderResponseDataString, CheckStatusOrderDataResponse::class.java)

        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = true,
            data = checkStatusOrderResponseDataSuccess,
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )

        val mockedInstances = getMockedInstances(
            statusTransactionResponse = statusTransactionResponse,
            checkStatusOrderResponse = checkStatusOrderResponse
        )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawLoadingFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawLoadingFragment ->
            //WHEN -> post Delayed is run
            mockedInstances.postDelayedImplMock.postRunnable()

            //THEN
            Assert.assertEquals(10000L, mockedInstances.postDelayedImplMock.timerIntervalSpy)

            verify {
                mockedInstances.mockMainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(17419,
                        "JDJ5JDEwJE0uemhQNHdJUzFFdm9OaEdwNW1LZGVPUlQyNzJNZ1ouMEhiSzdDTzRiVnRGaUY5RDRGVks2"))
            }

            verify {
                mockedInstances.mockNavController.navigate(R.id.navigation_finish_screen_withdraw_pix)
            }
        }
    }

    @Test
    fun `CASE 04, Withdraw type Wallet, transaction = success and checkStatusOrder = error`() {

        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_wallet_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)

        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = false,
            data = null,
            error = ErrorTransaction(message = "generic error")
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )

        val mockedInstances = getMockedInstances(
            statusTransactionResponse = statusTransactionResponse,
            checkStatusOrderResponse = checkStatusOrderResponse
        )

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawLoadingFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawLoadingFragment ->
            //WHEN -> post Delayed is run
            mockedInstances.postDelayedImplMock.postRunnable()

            //THEN
            Assert.assertEquals(10000L, mockedInstances.postDelayedImplMock.timerIntervalSpy)

            verify {
                mockedInstances.mockMainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(17419,
                        "JDJ5JDEwJE0uemhQNHdJUzFFdm9OaEdwNW1LZGVPUlQyNzJNZ1ouMEhiSzdDTzRiVnRGaUY5RDRGVks2"))
            }

            verify {
                mockedInstances.mockNavController.navigate(R.id.navigation_fatal_error)
            }
        }
    }
}