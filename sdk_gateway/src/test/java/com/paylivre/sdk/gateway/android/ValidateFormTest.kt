package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.*
import org.junit.Assert
import org.junit.Test

class ValidateFormTest {

    @Test
    fun `Test ValidateFormTest`() {
        Assert.assertEquals(
            "ValidateForm(email=, document=, typeSelected=0, emailWalletPaylivre=, api_token=, pixKeyTypeSelected=0, pixKeyValue=)",
            ValidateForm(
                email = "",
                document = "",
                typeSelected = 0,
                emailWalletPaylivre = "",
                api_token = "",
                pixKeyTypeSelected = 0,
                pixKeyValue = ""
            ).toString()
        )
    }

    @Test
    fun `Test ResponseValidateForm`() {
        val mockFieldErrorStatus = FieldErrorStatus(
            isValidated = false,
            isErrorEmpty = false,
            keyLabelField = "",
            keyStringErrorDescription = ""
        )

        Assert.assertEquals(
            "ResponseValidateForm(statusFormDataValidated=true, statusEmail=FieldErrorStatus(isValidated=false, isErrorEmpty=false, keyLabelField=, keyStringErrorDescription=), statusDocument=FieldErrorStatus(isValidated=false, isErrorEmpty=false, keyLabelField=, keyStringErrorDescription=), statusEmailWalletPaylivre=FieldErrorStatus(isValidated=false, isErrorEmpty=false, keyLabelField=, keyStringErrorDescription=), statusPassword=FieldErrorStatus(isValidated=false, isErrorEmpty=false, keyLabelField=, keyStringErrorDescription=))",
            ResponseValidateForm(
                statusFormDataValidated = true,
                statusEmail = mockFieldErrorStatus,
                statusDocument = mockFieldErrorStatus,
                statusEmailWalletPaylivre = mockFieldErrorStatus,
                statusPassword = mockFieldErrorStatus
            ).toString()
        )
    }

    @Test
    fun `Test getKeyStringErrorField`() {
        Assert.assertEquals(
            "ResponseGetKeyStringErrorField(isErrorEmpty=true, keyLabelField=, keyErrorMessage=required_field)",
            getKeyStringErrorField(
                true, "", "", false
            ).toString()
        )

        Assert.assertEquals(
            "ResponseGetKeyStringErrorField(isErrorEmpty=false, keyLabelField=, keyErrorMessage=error_short_password)",
            getKeyStringErrorField(
                true, "not empty", "", true
            ).toString()
        )

        Assert.assertEquals(
            "ResponseGetKeyStringErrorField(isErrorEmpty=false, keyLabelField=, keyErrorMessage=error_key_invalid)",
            getKeyStringErrorField(
                true, "not empty", "", false
            ).toString()
        )
    }

    @Test
    fun `Test getFieldStatusApiToken`() {
        Assert.assertEquals(
            FieldErrorStatus(isValidated = false, isErrorEmpty = true,
                keyLabelField = "api_token", keyStringErrorDescription = "required_field"),
            getFieldStatusApiToken(
                ""
            )
        )

        Assert.assertEquals(
            FieldErrorStatus(isValidated = true,
                isErrorEmpty = false,
                keyLabelField = "api_token",
                keyStringErrorDescription = ""),
            getFieldStatusApiToken(
                "123"
            )
        )
    }

    @Test
    fun `Test validateTypesSelected`() {
        Assert.assertEquals(
            true,
            validateTypesSelected(
                Type.PIX.code,
                Operation.WITHDRAW.code
            )
        )

        Assert.assertEquals(
            true,
            validateTypesSelected(
                Type.WALLET.code,
                Operation.WITHDRAW.code
            )
        )

        Assert.assertEquals(
            true,
            validateTypesSelected(
                Type.WALLET.code,
                Operation.DEPOSIT.code
            )
        )

        Assert.assertEquals(
            true,
            validateTypesSelected(
                Type.PIX.code,
                Operation.DEPOSIT.code
            )
        )

        Assert.assertEquals(
            true,
            validateTypesSelected(
                Type.WIRETRANSFER.code,
                Operation.DEPOSIT.code
            )
        )

        Assert.assertEquals(
            false,
            validateTypesSelected(
                -1,
                Operation.DEPOSIT.code
            )
        )

        Assert.assertEquals(
            false,
            validateTypesSelected(
                Type.WIRETRANSFER.code,
                Operation.WITHDRAW.code
            )
        )
    }


    @Test
    fun `Test validateForm`() {
        var mockDataForm = ValidateForm(
            email = "teste@teste.com",
            document = "61995053015",
            typeSelected = Type.PIX.code,
            emailWalletPaylivre = "teste@teste.com",
            api_token = "123dsdfgd45a6sd456adas",
            pixKeyTypeSelected = TypePixKey.DOCUMENT.code,
            pixKeyValue = "61995053015"
        )

        Assert.assertEquals(
            ResponseValidateForm(
                true,
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "document",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "api_token",
                    keyStringErrorDescription = ""
                )
            ),
            validateForm(
                mockDataForm,
                Operation.DEPOSIT.code,
                Type.PIX.code
            )
        )
    }

    @Test
    fun `Test validateForm Deposit Wallet`() {
        var mockDataForm = ValidateForm(
            email = "teste@teste.com",
            document = "61995053015",
            typeSelected = Type.WALLET.code,
            emailWalletPaylivre = "teste@teste.com",
            api_token = "123dsdfgd45a6sd456adas",
            pixKeyTypeSelected = -1,
            pixKeyValue = ""
        )

        Assert.assertEquals(
            ResponseValidateForm(
                true,
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "document",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "api_token",
                    keyStringErrorDescription = ""
                )
            ),
            validateForm(
                mockDataForm,
                Operation.DEPOSIT.code,
                Type.WALLET.code
            )
        )
    }

    @Test
    fun `Test validateForm with Withdraw Pix Invalid pixKey`() {
        var mockDataForm = ValidateForm(
            email = "teste@teste.com",
            document = "61995053015",
            typeSelected = Type.PIX.code,
            emailWalletPaylivre = "teste@teste.com",
            api_token = "123dsdfgd45a6sd456adas",
            pixKeyTypeSelected = -1,
            pixKeyValue = ""
        )

        Assert.assertEquals(
            ResponseValidateForm(
                false,
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "document",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "email",
                    keyStringErrorDescription = ""
                ),
                FieldErrorStatus(
                    isValidated = true,
                    isErrorEmpty = false,
                    keyLabelField = "api_token",
                    keyStringErrorDescription = ""
                )
            ),
            validateForm(
                mockDataForm,
                Operation.WITHDRAW.code,
                Type.PIX.code
            )
        )
    }

    @Test
    fun `Test validatePixKeyType`() {
        Assert.assertEquals(false, validatePixKeyType(null))
        Assert.assertEquals(false, validatePixKeyType(-1))
        Assert.assertEquals(true, validatePixKeyType(TypePixKey.DOCUMENT.code))
        Assert.assertEquals(true, validatePixKeyType(TypePixKey.EMAIL.code))
        Assert.assertEquals(true, validatePixKeyType(TypePixKey.PHONE.code))
    }




}