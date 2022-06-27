package com.paylivre.sdk.gateway.android

import android.os.Build
import com.paylivre.sdk.gateway.android.domain.model.*
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
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers="pt-port")
class ExtractDataFromURLTest {

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    private val mockUrlEncodedValidDeposit =
        "https%3A%2F%2Fdev.gateway.paylivre.com%3Fmerchant_transaction_id%3D7e68641841%26merchant_id%3D302%26operation%3D0%26email%3Duser_gateway_test%40tutanota.com%26document_number%3D61317581075%26amount%3D500%26currency%3DBRL%26type%3D1%26account_id%3D123654asd%26callback_url%3Dhttps%3A%2F%2Fwww.google.com%26redirect_url%3Dhttps%3A%2F%2Fwww.merchant_to_you.com%26auto_approve%3D1%26signature%3DJGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB%26logo_url%3Dhttps%3A%2F%2Fgithub.com%2Fpaylivre%2Fgateway-example-react-js%2Fblob%2Fmaster%2Fassets%2Flogo_jackpot_new.png%3Fraw%3Dtrue"
    private val mockUrlDecodedValidDeposit =
        "https://dev.gateway.paylivre.com?merchant_transaction_id=7e68641841&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=123654asd&callback_url=https://www.google.com&redirect_url=https://www.merchant_to_you.com&auto_approve=1&signature=JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true"

    private val mockDataStartCheckoutDepositFromMockedUrlValid = DataStartCheckout(
        merchant_id = 302,
        operation = 0,
        merchant_transaction_id = "7e68641841",
        amount = 500,
        currency = "BRL",
        type = 1,
        account_id = "123654asd",
        callback_url = "https://www.google.com",
        email_address = "user_gateway_test@tutanota.com",
        document = "61317581075",
        base_url = "https://dev.gateway.paylivre.com",
        auto_approve = 1,
        signature = "JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB",
        redirect_url = "https://www.merchant_to_you.com",
        logo_url = "https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true",
        gateway_token = "",
        url = mockUrlDecodedValidDeposit,
        pix_key = null,
        pix_key_type = -1
    )


    private val mockUrlDecodedValidWithdraw =
        "https://dev.gateway.paylivre.com?merchant_transaction_id=7e68641841&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=123654asd&callback_url=https://www.google.com&pix_key_type=0&pix_key=61317581075&redirect_url=https://www.merchant_to_you.com&auto_approve=1&signature=JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true"

    private val mockDataStartCheckoutWithdrawFromMockedUrlValid = DataStartCheckout(
        merchant_id = 302,
        operation = Operation.WITHDRAW.code,
        merchant_transaction_id = "7e68641841",
        amount = 500,
        currency = "BRL",
        type = 1,
        account_id = "123654asd",
        callback_url = "https://www.google.com",
        email_address = "user_gateway_test@tutanota.com",
        document = "61317581075",
        base_url = "https://dev.gateway.paylivre.com",
        auto_approve = 1,
        signature = "JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB",
        redirect_url = "https://www.merchant_to_you.com",
        logo_url = "https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true",
        gateway_token = "",
        url = mockUrlDecodedValidDeposit,
        pix_key = "61317581075",
        pix_key_type = 0
    )

    private val mockDataStartCheckoutFromMockedUrlInvalid = DataStartCheckout(
        merchant_id = -1,
        gateway_token = "",
        operation = -1,
        merchant_transaction_id = "",
        amount = -1,
        currency = "",
        type = -1,
        account_id = "",
        callback_url = "",
        email_address = "",
        document = "",
        base_url = "",
        auto_approve = -1
    )


    @Test
    fun `Test stringUrlDecode with url encoded`() {
        Assert.assertEquals(
            mockUrlDecodedValidDeposit,
            getDecodedUrl(
                mockUrlEncodedValidDeposit,
            )
        )
    }

    @Test
    fun `Test stringUrlDecode with url decoded`() {
        Assert.assertEquals(
            mockUrlDecodedValidDeposit,
            getDecodedUrl(
                mockUrlDecodedValidDeposit,
            )
        )
    }

    @Test
    fun `Test getBaseHost with base url https valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.base_url,
            getBaseHost(
                mockUrlDecodedValidDeposit,
            )
        )
    }

    @Test
    fun `Test getBaseHost with base url invalid`() {
        val mockUrl = "http:dev.gateway.paylivre.com?merchant_transaction_id=dc3609713"
        Assert.assertEquals(
            "",
            getBaseHost(
                mockUrl,
            )
        )
    }

    @Test
    fun `Test extract base_url from a valid url with the function extractDataFromUrl`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.base_url,
            extractDataFromUrl(
                mockUrlEncodedValidDeposit,
            ).base_url
        )
    }

    @Test
    fun `Test extract base_url from a invalid url with the function extractDataFromUrl`() {
        val mockUrlInvalid = "https:dev.gateway.paylivre.com?merchant_transaction_id=dc3609713"
        Assert.assertEquals(
            "",
            extractDataFromUrl(
                mockUrlInvalid,
            ).base_url
        )
    }

    @Test
    fun `Test extract merchant_id invalid with url valid`() {
        val mockUrl = "https://dev.gateway.paylivre.com?merchant_id="
        Assert.assertEquals(
            -1,
            extractDataFromUrl(
                mockUrl,
            ).merchant_id
        )
    }


    @Test
    fun `Test getSignature with signature valid with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.signature,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).signature
        )
    }

    @Test
    fun `Test getSignature with signature empty with url valid`() {
        val mockUrl = "https://dev.gateway.paylivre.com?signature="
        Assert.assertEquals(
            "",
            extractDataFromUrl(
                mockUrl,
            ).signature
        )
    }

    @Test
    fun `Test extract signature with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.signature,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).signature
        )
    }

    @Test
    fun `Test extract merchant_id with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.merchant_id,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).merchant_id
        )
    }

    @Test
    fun `Test extract merchant_id with url invalid`() {
        val mockUrl = "https:dev.gateway.paylivre.com?signature="
        Assert.assertEquals(
            -1,
            extractDataFromUrl(
                mockUrl,
            ).merchant_id
        )
    }

    @Test
    fun `Test extract operation with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.operation,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).operation
        )
    }

    @Test
    fun `Test extract amount with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.amount,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).amount
        )
    }

    @Test
    fun `Test extract type with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.type,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).type
        )
    }

    @Test
    fun `Test extract auto_approve with url valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid.auto_approve,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            ).auto_approve
        )
    }

    @Test
    fun `Test extract pix_key_type and pix_key of data with url of withdraw valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutWithdrawFromMockedUrlValid.pix_key_type,
            extractDataFromUrl(
                mockUrlDecodedValidWithdraw,
            ).pix_key_type
        )

        Assert.assertEquals(
            mockDataStartCheckoutWithdrawFromMockedUrlValid.pix_key,
            extractDataFromUrl(
                mockUrlDecodedValidWithdraw,
            ).pix_key
        )
    }






    @Test
    fun `Test extractDataFromUrl with url all params valid`() {
        Assert.assertEquals(
            mockDataStartCheckoutDepositFromMockedUrlValid,
            extractDataFromUrl(
                mockUrlDecodedValidDeposit,
            )
        )
    }

    @Test
    fun `Test getUrlWithoutSignature`() {
        val mockUrlWithSignature = "https://dev.gateway.paylivre.com?merchant_transaction_id=7e68641841&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=123654asd&callback_url=https://www.google.com&redirect_url=https://www.merchant_to_you.com&auto_approve=1&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true"
        Assert.assertEquals(
            mockUrlWithSignature,
            getUrlWithoutSignature(
                mockUrlDecodedValidDeposit
            )
        )
    }
}