package com.example.paylivre.sdk.gateway

import com.example.paylivre.sdk.gateway.data.*
import com.example.paylivre.sdk.gateway.model.BasesUrl
import com.example.paylivre.sdk.gateway.model.DataGenerateUrl
import com.example.paylivre.sdk.gateway.model.GenerateUrlToCheckout
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GenerateUrlToCheckoutTest {
    @Test
    fun `Test GenerateUrlToCheckoutTest with valid data`() = runBlocking {
        val argon2iHash: Argon2iHash = mockk()
        val expectedHash =
            "\$argon2i\$v=19\$m=16,t=2,p=1\$NndoSFd4Sm9XQWlmaTU0bQ\$EWSS3teGY77TPKuaAn9g4g"
        val saltRandonString = "6whHWxJoWAifi54m"

        val dataGenerateUrl = DataGenerateUrl(
            base_url = BasesUrl.BASE_URL_DEV.base_url,
            merchant_id = dataMerchantDev.merchant_id,
            merchant_transaction_id = "12654asd",
            amount = "500",
            currency = Currency.BRL.currency,
            operation = Operation.DEPOSIT.code.toString(),
            callback_url = CALLBACK_URL,
            type = "1",
            account_id = "1a23sd",
            email = USER_EMAIL,
            document_number = USER_DOCUMENT_NUMBER,
            auto_approve = "1",
            redirect_url = REDIRECT_URL,
            logo_url = LOGO_URL,
            gateway_token = dataMerchantDev.gateway_token
        )

        val generateUrlToCheckout = GenerateUrlToCheckout(
            dataGenerateUrl,
            argon2iHash,
            saltRandonString
        )

        val urlToSignature = generateUrlToCheckout.getURLToSignature()

        val mockStringPassArgon2iHash =
            "${dataGenerateUrl.gateway_token}$urlToSignature"

        coEvery {
            argon2iHash.generateArgon2iHash(
                mockStringPassArgon2iHash,
                "6whHWxJoWAifi54m"
            )
        } returns expectedHash

        val expectedUrl = "https://dev.gateway.paylivre.com?merchant_transaction_id=12654asd&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=1a23sd&callback_url=https://www.google.com&auto_approve=1&redirect_url=https://www.merchant_to_you.com&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true&signature=JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTm5kb1NGZDRTbTlYUVdsbWFUVTBiUSRFV1NTM3RlR1k3N1RQS3VhQW45ZzRn"

        Assert.assertEquals(
            expectedUrl,
            generateUrlToCheckout.getUrlWithSignature()
        )

        Assert.assertEquals(
            "https://dev.gateway.paylivre.com?merchant_transaction_id=12654asd&merchant_id=302&operation=0&email=user_gateway_test@tutanota.com&document_number=61317581075&amount=500&currency=BRL&type=1&account_id=1a23sd&callback_url=https://www.google.com&auto_approve=1&redirect_url=https://www.merchant_to_you.com&logo_url=https://github.com/paylivre/gateway-example-react-js/blob/master/assets/logo_jackpot_new.png?raw=true",
            urlToSignature
        )

    }


}