package com.paylivre.sdk.gateway.android

import android.os.Build
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.BASE_URL_ENVIRONMENT_DEV
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.bouncycastle.util.StringList
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
class ValidateDataPaymentTest {
    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

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

    @Test
    fun `Test ResponseValidData`() {
        Assert.assertEquals(
            "ResponseValidData(isValid=true, messageMainError=null, errorTags=null, messageDetailsError=null)",
            ResponseValidData(true, null, null, null).toString()
        )
    }

    @Test
    fun `Test DataTransaction`() {
        Assert.assertEquals(
            "DataTransaction(operation=0, currency=, type=0, amount=0, merchant_transaction_id=, auto_approve=0)",
            DataTransaction(0, "", 0, 0, "", 0).toString()
        )
    }

    @Test
    fun `Test ResponseValidateDataParams`() {
        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=false, errorTags=null)",
            ResponseValidateDataParams(false, null).toString()
        )
    }

    @Test
    fun `Test Languages types`() {
        Assert.assertEquals(
            true,
            Languages.values().map { it.toString() }.contains("PT")
        )
        Assert.assertEquals(
            true,
            Languages.values().map { it.toString() }.contains("EN")
        )
        Assert.assertEquals(
            true,
            Languages.values().map { it.toString() }.contains("ES")
        )
    }

    @Test
    fun `Test getResponseValidateDataParams`() {
        Assert.assertEquals(
            ResponseValidateDataParams(true, mutableListOf()),
            getResponseValidateDataParams(true, null, null)
        )

        Assert.assertEquals(
            ResponseValidateDataParams(false, null),
            getResponseValidateDataParams(false, null, null)
        )

        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=false, errorTags=[1])",
            getResponseValidateDataParams(false, "1", null).toString()
        )
    }

    @Test
    fun `Test validateLocaleLanguage`() {
        Assert.assertEquals(
            false,
            validateLocaleLanguage("uk")
        )
    }

    @Test
    fun `Test validateGatewayToken`() {
        Assert.assertEquals(
            ResponseValidateDataParams(true, mutableListOf()),
            validateGatewayToken("123456")
        )

        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=false, errorTags=[RP002])",
            validateGatewayToken("").toString()
        )

        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=false, errorTags=[RP002])",
            validateGatewayToken(null).toString()
        )
    }

    @Test
    fun `Test validateDocument with null or empty`() {
        Assert.assertEquals(
            false, validateDocument("")
        )
        Assert.assertEquals(
            false, validateDocument(null)
        )
    }


    @Test
    fun `Test validateTypesNumber with Operation Withdraw`() {
        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=true, errorTags=[])",
            validateTypesNumber(TypesToSelect.PIX.code, Operation.WITHDRAW.code).toString()
        )
        Assert.assertEquals(
            "ResponseValidateDataParams(isValid=true, errorTags=[])",
            validateTypesNumber(TypesToSelect.WALLET.code, Operation.WITHDRAW.code).toString()
        )
    }

    @Test
    fun `Test validateDataPix with Operation Deposit`() {
        Assert.assertEquals(
            true,
            validateDataPix(Operation.DEPOSIT.code)
        )
    }

    @Test
    fun `Test addDdiInPixKeyCellPhoneWithdraw`() {
        var orderDataRequestMock = OrderDataRequest(
            base_url = "",
            merchant_id = 0,
            merchant_transaction_id = "",
            amount = "",
            currency = "",
            operation = Operation.WITHDRAW.code.toString(),
            callback_url = "",
            type = "",
            selected_type = "",
            account_id = "",
            email = "",
            document_number = "",
            login_email = "",
            api_token = "",
            pix_key_type = TypePixKey.PHONE.code.toString(),
            pix_key = "99999999999",
            signature = "",
            url = "",
            auto_approve = "",
            redirect_url = "",
            logo_url = "",
        )

        Assert.assertEquals(
            "+5599999999999",
            addDdiInPixKeyCellPhoneWithdraw(orderDataRequestMock).pix_key
        )
    }


}