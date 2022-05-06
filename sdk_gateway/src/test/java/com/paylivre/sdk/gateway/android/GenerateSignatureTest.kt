package com.paylivre.sdk.gateway.android

import android.util.Base64
import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.*


val dataMock = DataGenerateSignature(
    "https://dev.gateway.paylivre.com",
    19,
    "teste",
    "123456",
    500.toString(),
    "BRL",
    0.toString(),
    "https://www.google.com",
    4.toString(),
    "1234567888",
    "test@test.com",
    "person_user_gateway@test.com",
    "60712326006",
    "",
    "",
    "",
    "",
    0
)


@ExperimentalCoroutinesApi
class GenerateSignatureTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    //Mock Base64
    @Before
    fun `Bypass android_util_Base64 to java_util_Base64`() {
        mockkStatic(Base64::class)
        val arraySlot = slot<ByteArray>()

        every {
            Base64.encodeToString(capture(arraySlot), Base64.DEFAULT)
        } answers {
            java.util.Base64.getEncoder().encodeToString(arraySlot.captured)
        }

        val stringSlot = slot<String>()
        every {
            Base64.decode(capture(stringSlot), Base64.DEFAULT)
        } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }
    }


    @Test
    fun `Get Url empty email and empty document`() {
        val data = DataGenerateSignature(
            "https://dev.gateway.paylivre.com",
            19,
            "teste",
            "19",
            500.toString(),
            "BRL",
            Operation.DEPOSIT.code.toString(),
            "www.google.com",
            Type.PIX.code.toString(),
            "4",
            "123asd6",
            "",
            "",
            "",
            "",
            "",
            "",
            0
        )
        val urlToTest = getURL(data)
        val expectedUrl =
            "https://dev.gateway.paylivre.com?merchant_transaction_id=19&merchant_id=19&operation=0&amount=500&currency=BRL&type=4&account_id=123asd6&callback_url=www.google.com&auto_approve=0"
        Assert.assertEquals(expectedUrl, urlToTest)
    }

    @Test
    fun `Get Url with valid email and empty document`() {
        val data = DataGenerateSignature(
            "https://dev.gateway.paylivre.com",
            19,
            "teste",
            "19",
            "500",
            "BRL",
            Operation.DEPOSIT.code.toString(),
            "www.google.com",
            Type.PIX.code.toString(),
            "4",
            "1das4564",
            "person_user_gateway@test.com",
            "",
            "",
            "",
            "",
            "",
            0
        )
        val urlToTest = getURL(data)
        val expectedUrl =
            "https://dev.gateway.paylivre.com?merchant_transaction_id=19&merchant_id=19&operation=0&email=person_user_gateway@test.com&amount=500&currency=BRL&type=4&account_id=1das4564&callback_url=www.google.com&auto_approve=0"
        Assert.assertEquals(expectedUrl, urlToTest)
    }

    @Test
    fun `Get Url with valid email and valid document`() {
        val data = DataGenerateSignature(
            "https://dev.gateway.paylivre.com",
            19,
            "teste",
            "19",
            "500",
            "BRL",
            Operation.DEPOSIT.code.toString(),
            "www.google.com",
            Type.PIX.code.toString(),
            "4",
            "123asd6",
            "person_user_gateway@test.com",
            "60712326006",
            "",
            "",
            "",
            "",
            0
        )
        val urlToTest = getURL(data)
        val expectedUrl =
            "https://dev.gateway.paylivre.com?merchant_transaction_id=19&merchant_id=19&operation=0&email=person_user_gateway@test.com&document_number=60712326006&amount=500&currency=BRL&type=4&account_id=123asd6&callback_url=www.google.com&auto_approve=0"
        Assert.assertEquals(expectedUrl, urlToTest)
    }

    @Test
    fun testServiceArgonHash() = runBlocking {
        val argon2iHash: Argon2iHash = mockk()
        val expectedHash =
            "\$argon2i\$v=19\$m=16,t=2,p=1\$NndoSFd4Sm9XQWlmaTU0bQ\$1uP04VY0o8A/7YMtbY7P7Q"
        coEvery {
            argon2iHash.generateArgon2iHash(
                "123456",
                "6whHWxJoWAifi54m"
            )
        } returns expectedHash

        val result = argon2iHash.generateArgon2iHash("123456", "6whHWxJoWAifi54m")
        Assert.assertEquals(expectedHash, result)
    }

    @Test
    fun testGenerateSignature() = runBlocking {
        val argon2iHashService: Argon2iHash = mockk()
        val mockStringSalt = "6whHWxJoWAifi54m"
        val mockGatewayToken = "teste"
        val mockStringUrl = getURL(dataMock)
        val mockPassString = "${mockGatewayToken}${mockStringUrl}"
        val expectedHashArgon2iHash =
            "\$argon2i\$v=19\$m=16,t=2,p=1\$NndoSFd4Sm9XQWlmaTU0bQ\$SpA8z9AnfenYeAga+jWb4g"

        coEvery {
            argon2iHashService.generateArgon2iHash(
                mockPassString,
                mockStringSalt
            )
        } returns expectedHashArgon2iHash

        val generateSignatureService = GenerateSignature(argon2iHashService, mockStringSalt)

        val expectedResponseGenerateSignature = ResponseGenerateSignature(
            dataMock,
            "JGFyZ29uMmkkdj0xOSRtPTE2LHQ9MixwPTEkTm5kb1NGZDRTbTlYUVdsbWFUVTBiUSRTcEE4ejlBbmZlblllQWdhK2pXYjRn",
            mockStringUrl
        )

        val result = generateSignatureService.generateSignature(dataMock)

        Assert.assertEquals(expectedResponseGenerateSignature, result)
    }


}