package com.paylivre.sdk.gateway.android

import android.app.Activity
import android.os.Build
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServicesStatus
import com.paylivre.sdk.gateway.android.utils.*
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
class TypesUtilsTest {

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
    fun `Test BitwiseTypes`() {
        Assert.assertEquals(1, BitwiseTypes.PIX.decimal)
        Assert.assertEquals(4, BitwiseTypes.WIRETRANSFER.decimal)
        Assert.assertEquals(2, BitwiseTypes.BILLET.decimal)
        Assert.assertEquals(8, BitwiseTypes.WALLET.decimal)
    }

    @Test
    fun `Test getNumberByTypes`() {
        Assert.assertEquals(1, getNumberByTypes(ServicesStatus(
            statusWallet = false, statusWiretransfer = false, statusBillet = false, statusPix = true
        )))
        Assert.assertEquals(2, getNumberByTypes(ServicesStatus(
            statusWallet = false, statusWiretransfer = false, statusBillet = true, statusPix = false
        )))
        Assert.assertEquals(4, getNumberByTypes(ServicesStatus(
            statusWallet = false, statusWiretransfer = true, statusBillet = false, statusPix = false
        )))
        Assert.assertEquals(8, getNumberByTypes(ServicesStatus(
            statusWallet = true, statusWiretransfer = false, statusBillet = false, statusPix = false
        )))
    }

    @Test
    fun `Test getTypesKeyNameInNumberTypes`() {
        Assert.assertEquals("[type_pix]", getTypesKeyNameInNumberTypes(1).toString())
        Assert.assertEquals("[type_billet]", getTypesKeyNameInNumberTypes(2).toString())
        Assert.assertEquals("[title_wire_transfer]", getTypesKeyNameInNumberTypes(4).toString())
        Assert.assertEquals("[]", getTypesKeyNameInNumberTypes(8).toString())
    }

    @Test
    fun `Test getNameByTypesKeys`() {
        val mockList: MutableList<String> = mutableListOf()
        mockList.add("type_pix")
        Assert.assertEquals("Pix", getNameByTypesKeys(activityMocked, mockList))
    }

    @Test
    fun `Test getStringByKey`() {
        Assert.assertEquals("Email", getStringByKey(activityMocked, "pix_key_type_email"))
        Assert.assertEquals("", getStringByKey(activityMocked, ""))
    }

    @Test
    fun `Test getStringIdByKey`() {
        Assert.assertEquals(R.string.app_name, getStringIdByKey(activityMocked, "app_name"))
        Assert.assertEquals(0, getStringIdByKey(activityMocked, ""))
    }

    @Test
    fun `Test checkValidDrawableId`() {
        Assert.assertEquals(true, checkValidDrawableId(activityMocked, R.drawable.logo_example))
        Assert.assertEquals(false, checkValidDrawableId(activityMocked, -1))
        Assert.assertEquals(false, checkValidDrawableId(activityMocked, -10))
    }


}