package com.paylivre.sdk.gateway.android.ui.error.fragment_fatal_error

import android.os.Build
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.ui.error.checkIsErrorKycLimits
import com.paylivre.sdk.gateway.android.ui.error.handleNavigateToErrorScreen
import com.paylivre.sdk.gateway.android.ui.header.HeaderFragment
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class ErrorKycLimitExceededTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `test checkIsErrorKycLimits, given message error is "User Kyc Limit exceeded"`() {
        Assert.assertEquals(true, checkIsErrorKycLimits("User Kyc Limit exceeded"))
        Assert.assertEquals(true, checkIsErrorKycLimits("Kyc validation errors"))
        Assert.assertEquals(true,
            checkIsErrorKycLimits("The amount exceeds the limit available for your KYC Level."))
    }

    @Test
    fun `test HandleNavigateErrorScreen, given original_message error is "User Kyc Limit exceeded"`() {
        val fragment = launchFragmentInContainer<HeaderFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        fragment.onFragment { fragmentTest ->
            //GIVEN
            Navigation.setViewNavController(fragmentTest.requireView(), mockNavController)

            //WHEN
            fragmentTest.view?.let {
                handleNavigateToErrorScreen(it,
                    ErrorTransaction(original_message = "User Kyc Limit exceeded"))
            }

            //THEN
            Mockito.verify(mockNavController).navigate(R.id.navigation_error_kyc_limit)
        }
    }

    @Test
    fun `HandleNavigateErrorScreen, original_message error is "The amount exceeds the limit available for your KYC Level"`() {
        val fragment = launchFragmentInContainer<HeaderFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        fragment.onFragment { fragmentTest ->
            //GIVEN
            Navigation.setViewNavController(fragmentTest.requireView(), mockNavController)

            //WHEN
            fragmentTest.view?.let {
                handleNavigateToErrorScreen(it,
                    ErrorTransaction(
                        original_message = "The amount exceeds the limit available for your KYC Level."))
            }

            //THEN
            Mockito.verify(mockNavController).navigate(R.id.navigation_error_kyc_limit)
        }
    }


    @Test
    fun `HandleNavigateErrorScreen, original_message error is "Kyc validation errors"`() {
        val fragment = launchFragmentInContainer<HeaderFragment>(Bundle(),
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        fragment.onFragment { fragmentTest ->
            //GIVEN
            Navigation.setViewNavController(fragmentTest.requireView(), mockNavController)

            //WHEN
            fragmentTest.view?.let {
                handleNavigateToErrorScreen(it,
                    ErrorTransaction(
                        original_message = "Kyc validation errors"))
            }

            //THEN
            Mockito.verify(mockNavController).navigate(R.id.navigation_error_kyc_limit)
        }

    }

}