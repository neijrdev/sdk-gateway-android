package com.paylivre.sdk.gateway.android

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.utils.getColorById
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class GetColorByIdTest {
    private val activityMocked: Activity =
        Robolectric.buildActivity(Activity::class.java).create().get()

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
        activityMocked.setTheme(R.style.Theme_MaterialComponents_Light)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test getColorByIdTest`() {
        Assert.assertEquals(
            activityMocked.getColor(R.color.primary_color_paylivre_sdk),
            getColorById(activityMocked, R.color.primary_color_paylivre_sdk)
        )
    }
}