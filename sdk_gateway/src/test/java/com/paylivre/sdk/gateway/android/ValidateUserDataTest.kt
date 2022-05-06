package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.validateUserData
import org.junit.Assert
import org.junit.Test

class ValidateUserDataTest {
    @Test
    fun `Test validateUserData with valid values`() {
        Assert.assertEquals(
            true,
            validateUserData(
                "test@test.com",
                "558.544.550-21",
                "1234"
            ).isValid
        )


        Assert.assertEquals(
            "",
            validateUserData(
                "test@test.com",
                "558.544.550-21",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            true,
            validateUserData(
                "test@test.com",
                "19733219045",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "",
            validateUserData(
                "test@test.com",
                "19733219045",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            true,
            validateUserData(
                "test@test.com",
                "15.335.981/0001-03",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "",
            validateUserData(
                "test@test.com",
                "15.335.981/0001-03",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            true,
            validateUserData(
                "test@test.com",
                "45762426000110",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "",
            validateUserData(
                "test@test.com",
                "45762426000110",
                "1234"
            ).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `Test validateUserData with invalid email`() {
        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.",
                "558.544.550-21",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX013",
            validateUserData(
                "test@test.",
                "558.544.550-21",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test",
                "558.544.550-21",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX013",
            validateUserData(
                "test@test",
                "558.544.550-21",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@",
                "558.544.550-21",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX013",
            validateUserData(
                "test@",
                "558.544.550-21",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test.test.com",
                "558.544.550-21",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX013",
            validateUserData(
                "test.test.com",
                "558.544.550-21",
                "1234"
            ).errorTags?.joinToString { it }
        )

    }

    @Test
    fun `Test validateUserData with invalid documents`() {
        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.",
                "558.544.550-2",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX014",
            validateUserData(
                "test@test.com",
                "558.544.550-2",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.",
                "558.544.550-222",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX014",
            validateUserData(
                "test@test.com",
                "558.544.550-222",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.",
                "558.544.550-28",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX014",
            validateUserData(
                "test@test.com",
                "558.544.550-28",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.com",
                "4576242600011",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.com",
                "45762426000111",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX014",
            validateUserData(
                "test@test.com",
                "45762426000111",
                "1234"
            ).errorTags?.joinToString { it }
        )

        Assert.assertEquals(
            false,
            validateUserData(
                "test@test.",
                "45762426000118",
                "1234"
            ).isValid
        )

        Assert.assertEquals(
            "RX014",
            validateUserData(
                "test@test.com",
                "45762426000118",
                "1234"
            ).errorTags?.joinToString { it }
        )
    }

    @Test
    fun `Test validateUserData invalid all data`() {
        val validateUserDataToTest = validateUserData(
            "test@",
            "4576242",
            ""
        )
        Assert.assertEquals(
            false,
            validateUserDataToTest.isValid
        )
        Assert.assertEquals(
            "RX013, RX014, RX006",
            validateUserDataToTest.errorTags?.joinToString { it }
        )
    }

    @Test
    fun `Test validateUserData with empty account_id`() {
        val validateUserDataToTest = validateUserData(
            "test@test.com",
            "45762426000110",
            ""
        )
        Assert.assertEquals(
            false,
            validateUserDataToTest.isValid
        )
        Assert.assertEquals(
            "RX006",
            validateUserDataToTest.errorTags?.joinToString { it }
        )
    }

    @Test
    fun `Test validateUserData with null string account_id`() {
        val validateUserDataToTest = validateUserData(
            "test@test.com",
            "45762426000110",
            ""
        )
        Assert.assertEquals(
            false,
            validateUserDataToTest.isValid
        )
        Assert.assertEquals(
            "RX006",
            validateUserDataToTest.errorTags?.joinToString { it }
        )
    }

}