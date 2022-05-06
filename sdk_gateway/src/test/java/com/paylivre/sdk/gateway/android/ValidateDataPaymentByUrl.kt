package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.extractDataFromUrl
import com.paylivre.sdk.gateway.android.domain.model.validateDataPayment
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import org.junit.Assert
import org.junit.Test

class ValidateDataPaymentByUrl {
    private val mockUrlEncodedValid =
        "https%3A%2F%2Fdev.gateway.paylivre.com%3Fmerchant_transaction_id%3D7e68641841%26merchant_id%3D302%26operation%3D0%26email%3Duser_gateway_test%40tutanota.com%26document_number%3D61317581075%26amount%3D500%26currency%3DBRL%26type%3D1%26account_id%3D123654asd%26callback_url%3Dhttps%3A%2F%2Fwww.google.com%26redirect_url%3Dhttps%3A%2F%2Fwww.merchant_to_you.com%26auto_approve%3D1%26signature%3DJGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB%26logo_url%3Dhttps%3A%2F%2Fgithub.com%2Fpaylivre%2Fgateway-example-react-js%2Fblob%2Fmaster%2Fassets%2Flogo_jackpot_new.png%3Fraw%3Dtrue"
    private val mockUrlDecodedValid =
        "https://dev.gateway.paylivre.com?merchant_transaction_id=7e68641841&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=123654asd&callback_url=https://www.google.com&redirect_url=https://www.merchant_to_you.com&auto_approve=1&signature=JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTlRFNVpqVXdOakV4T0RBek5HVSRBMWZnNTJPak5qSm5IQU1JSXAvRXBB&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true"


    @Test
    fun `Test validateDataPaymentTest startCheckout by url encoded all params valid`() {
        val mockData = extractDataFromUrl(
            mockUrlEncodedValid,
        )

        Assert.assertEquals(
            false,
            validateDataPayment(
                data = mockData,
                typesStartCheckout = TypesStartCheckout.BY_URL.code,
            ).isValid
        )
    }

    @Test
    fun `Test validateDataPaymentTest startCheckout by url decoded all params valid`() {
        val mockData = extractDataFromUrl(
            mockUrlDecodedValid,
        )

        Assert.assertEquals(
            false,
            validateDataPayment(
                data = mockData,
                typesStartCheckout = TypesStartCheckout.BY_URL.code,
            ).isValid
        )
    }

}