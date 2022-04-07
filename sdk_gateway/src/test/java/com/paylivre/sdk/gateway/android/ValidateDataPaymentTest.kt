package com.paylivre.sdk.gateway.android

import android.os.Build
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.BASE_URL_ENVIRONMENT_DEV
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers="pt-port")
class ValidateDataPaymentTest {
    private val mockDataStartCheckoutAllValidByParams = DataStartCheckout(
        10, "123asd4a56sf4a56s4d65as4d",
        Operation.DEPOSIT.code, "12asd323", 500,
        Currency.BRL.currency, 1, "1d2a3sd", "https://google.com",
        "user_gateway_test@tutanota.com", "61317581075",
        BASE_URL_ENVIRONMENT_DEV, 1,
    )

    private val mockDataStartCheckoutAllInvalidByParams = DataStartCheckout(
        -1, "", -1, "", -1,
        "", 0, "", "", "user_gateway_test@",
        "613175", "", -1,
    )


    @Test
    fun `Test validateDataPaymentTest startCheckout by params all valid`() {
        Assert.assertEquals(
            true,
            validateDataPayment(
                mockDataStartCheckoutAllValidByParams,
                TypesStartCheckout.BY_PARAMS.code
            ).isValid
        )
    }


    @Test
    fun `Test validateDataPaymentTest startCheckout by params all invalid`() {

        Assert.assertEquals(
            false,
            validateDataPayment(
                mockDataStartCheckoutAllInvalidByParams,
                TypesStartCheckout.BY_PARAMS.code
            ).isValid
        )

        Assert.assertEquals(
            "RX013, RX014, RX006, RP001, RX011, RX002, RX005, RX004, RX003, RX012, RX001, RX007, RP002",
            validateDataPayment(
                mockDataStartCheckoutAllInvalidByParams,
                TypesStartCheckout.BY_PARAMS.code
            ).errorTags
        )
    }


    @Test
    fun `Test validateDataPaymentTest startCheckout by url all valid`() {
        val mockDataStartCheckout = DataStartCheckout(
            10,
            "123asd4a56sf4a56s4d65as4d",
            Operation.DEPOSIT.code,
            "12asd323",
            500,
            Currency.BRL.currency,
            1,
            "1d2a3sd",
            "https://google.com",
            "user_gateway_test@tutanota.com",
            "61317581075",
            BASE_URL_ENVIRONMENT_DEV,
            1,
            signature = "123asd4a56s4f65a4sd1as32d1gfasfgafdgsdfg"
        )

        Assert.assertEquals(
            true,
            validateDataPayment(
                data = mockDataStartCheckout,
                typesStartCheckout = TypesStartCheckout.BY_URL.code,
            ).isValid
        )
    }

    @Test
    fun `Test validateDataPaymentTest startCheckout by url all invalid`() {
        Assert.assertEquals(
            false,
            validateDataPayment(
                data = mockDataStartCheckoutAllInvalidByParams,
                typesStartCheckout = TypesStartCheckout.BY_URL.code,
            ).isValid
        )

        Assert.assertEquals(
            "RX013, RX014, RX006, RP001, RX011, RX002, RX005, RX004, RX003, RX012, RX001, RX007, RX008",
            validateDataPayment(
                mockDataStartCheckoutAllInvalidByParams,
                TypesStartCheckout.BY_URL.code
            ).errorTags
        )
    }


    @Test
    fun `Test validateDataPix startCheckout with data pix valid, auto withdraw`() {
        Assert.assertEquals(
            true,
            validateDataPix(
                Operation.WITHDRAW.code,
                TypePixKey.DOCUMENT.code,
                "61317581075"
            )
        )

        Assert.assertEquals(
            true,
            validateDataPix(
                Operation.WITHDRAW.code,
                TypePixKey.EMAIL.code,
                "user_gateway_test@tutanota.com"
            )
        )

        Assert.assertEquals(
            true,
            validateDataPix(
                Operation.WITHDRAW.code,
                TypePixKey.PHONE.code,
                "99999999999"
            )
        )
    }

    @Test
    fun `Test validateDataPix startCheckout with data pix invalid, not auto withdraw`() {
        Assert.assertEquals(
            false,
            validateDataPix(
                Operation.WITHDRAW.code,
                -1, //default
                "" // empty
            )
        )

        Assert.assertEquals(
            false,
            validateDataPix(
                Operation.WITHDRAW.code,
                0, //Cpf/Cnpj
                "" // empty
            )
        )
    }

    @Test
    fun `Test validateDataPix startCheckout with data pix default, not auto withdraw`() {
        Assert.assertEquals(
            true,
            validateDataPix(
                Operation.WITHDRAW.code,
                -1, //default
                null //default
            )
        )


    }

}