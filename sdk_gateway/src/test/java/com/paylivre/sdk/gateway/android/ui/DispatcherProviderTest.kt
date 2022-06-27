package com.paylivre.sdk.gateway.android.ui

import android.os.Build
import kotlinx.coroutines.Dispatchers
import com.paylivre.sdk.gateway.android.DefaultDispatcherProvider
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class DispatcherProviderTest {
    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `CASE 1, test DispatcherProvider`() {
        val defaultDispatcherProvider  = DefaultDispatcherProvider()
        Assert.assertEquals(Dispatchers.Main,defaultDispatcherProvider.main())
        Assert.assertEquals(Dispatchers.Default,defaultDispatcherProvider.default())
        Assert.assertEquals(Dispatchers.IO,defaultDispatcherProvider.io())
        Assert.assertEquals(Dispatchers.Unconfined,defaultDispatcherProvider.unconfined())
    }
}