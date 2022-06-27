package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.utils.getBlockReasonByLocale
import org.junit.Assert
import org.junit.Test

class KycUtilsTest {
    val MOCK_ERROR_TRANSACTION = ErrorTransaction(
        message = "User is blocked by Kyc",
        errors = Errors(
            blocked_by_kyc = "PT-br blocking reason test",
            public_reason_pt = "Teste motivo de bloqueio PT-br",
            public_reason_en = "Test reason for blocking EN-en",
            public_reason_es = "Prueba de razón de bloqueo ES-es"
        )

    )

    @Test
    fun `Test getBlockReasonByLocale by locale valid is PT`() {
        Assert.assertEquals(
            "Teste motivo de bloqueio PT-br",
            getBlockReasonByLocale(
                errors = MOCK_ERROR_TRANSACTION.errors,
                locale = "pt"
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale by locale valid is ES`() {
        Assert.assertEquals(
            "Prueba de razón de bloqueo ES-es",
            getBlockReasonByLocale(
                errors = MOCK_ERROR_TRANSACTION.errors,
                locale = "es"
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale by locale valid is EN`() {
        Assert.assertEquals(
            "Test reason for blocking EN-en",
            getBlockReasonByLocale(
                errors = MOCK_ERROR_TRANSACTION.errors,
                locale = "en"
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale by locale is invalid`() {
        Assert.assertEquals(
            "Test reason for blocking EN-en",
            getBlockReasonByLocale(
                errors = MOCK_ERROR_TRANSACTION.errors,
                locale = ""
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale by locale is null`() {
        Assert.assertEquals(
            "Test reason for blocking EN-en",
            getBlockReasonByLocale(
                errors = MOCK_ERROR_TRANSACTION.errors,
                locale = null
            )
        )
    }


    @Test
    fun `Test getBlockReasonByLocale with error null and locale is valid`() {
        Assert.assertEquals(
            "",
            getBlockReasonByLocale(
                errors = null,
                locale = "pt"
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale with error null and locale is null`() {
        Assert.assertEquals(
            "",
            getBlockReasonByLocale(
                errors = null,
                locale = null
            )
        )
    }

    @Test
    fun `Test getBlockReasonByLocale with errors is null`() {
        Assert.assertEquals(
            "",
            getBlockReasonByLocale()
        )
    }
}