package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.utils.cellPhoneValidator
import com.paylivre.sdk.gateway.android.utils.isEmailValid

data class ValidateForm(
    val email: String,
    val document: String,
    val typeSelected: Int,
    val emailWalletPaylivre: String,
    val api_token: String,
    val pixKeyTypeSelected: Int,
    val pixKeyValue: String,
)

data class FieldErrorStatus(
    var isValidated: Boolean,
    var isErrorEmpty: Boolean,
    var keyLabelField: String,
    var keyStringErrorDescription: String,
)

data class ResponseValidateForm(
    var statusFormDataValidated: Boolean,
    var statusEmail: FieldErrorStatus,
    var statusDocument: FieldErrorStatus,
    var statusEmailWalletPaylivre: FieldErrorStatus,
    var statusPassword: FieldErrorStatus,
)

data class ResponseGetKeyStringErrorField(
    val isErrorEmpty: Boolean,
    val keyLabelField: String,
    val keyErrorMessage: String,
)


fun getKeyStringErrorField(
    isError: Boolean,
    fieldValue: String,
    keyLabelField: String,
    isFieldPassword: Boolean = false,
): ResponseGetKeyStringErrorField {
    return return if (isError) {
        val keyErrorMessage =
            when {
                fieldValue.isEmpty() -> "required_field"
                isFieldPassword -> "error_short_password"
                else -> "error_key_invalid"
            }
        ResponseGetKeyStringErrorField(fieldValue.isEmpty(), keyLabelField, keyErrorMessage)
    } else {
        ResponseGetKeyStringErrorField(fieldValue.isEmpty(), keyLabelField, "")
    }
}


fun getFieldStatusCellPhone(phone: String): FieldErrorStatus {
    val isPhoneValidated = cellPhoneValidator(phone)
    val keyStringErrorField = getKeyStringErrorField(!isPhoneValidated, phone, "pix_key_type_phone")
    return FieldErrorStatus(
        isPhoneValidated,
        keyStringErrorField.isErrorEmpty,
        keyStringErrorField.keyLabelField,
        keyStringErrorField.keyErrorMessage
    )
}

fun getFieldStatusEmail(email: String): FieldErrorStatus {
    val isEmailValidated = isEmailValid(email)
    val keyStringErrorField = getKeyStringErrorField(!isEmailValidated, email, "email")
    return FieldErrorStatus(
        isEmailValidated,
        keyStringErrorField.isErrorEmpty,
        keyStringErrorField.keyLabelField,
        keyStringErrorField.keyErrorMessage
    )
}

fun getFieldStatusDocument(document: String): FieldErrorStatus {
    val isDocumentValidated = validateDocument(document)
    val keyStringErrorField = getKeyStringErrorField(!isDocumentValidated, document, "document")
    return FieldErrorStatus(
        isDocumentValidated,
        keyStringErrorField.isErrorEmpty,
        keyStringErrorField.keyLabelField,
        keyStringErrorField.keyErrorMessage
    )
}

fun getFieldStatusApiToken(apiToken: String): FieldErrorStatus {
    val isApiTokenValidated = !apiToken.isNullOrEmpty()
    val keyStringErrorField =
        getKeyStringErrorField(!isApiTokenValidated, apiToken, "api_token", true)
    return FieldErrorStatus(
        isApiTokenValidated,
        keyStringErrorField.isErrorEmpty,
        keyStringErrorField.keyLabelField,
        keyStringErrorField.keyErrorMessage
    )
}

fun getFieldStatusPixKeyValue(pixKeyType: Int, pixKeyValue: String): FieldErrorStatus {
    var fieldErrorStatus = FieldErrorStatus(
        isValidated = true,
        isErrorEmpty = false,
        keyLabelField = "",
        keyStringErrorDescription = "error_key_invalid"
    )

    when (pixKeyType) {
        TypePixKey.DOCUMENT.code -> fieldErrorStatus = getFieldStatusDocument(pixKeyValue)
        TypePixKey.EMAIL.code -> fieldErrorStatus = getFieldStatusEmail(pixKeyValue)
        TypePixKey.PHONE.code -> fieldErrorStatus = getFieldStatusCellPhone(pixKeyValue)
        else -> fieldErrorStatus.isValidated = false
    }

    return fieldErrorStatus
}

fun validateTypesSelected(typeSelected: Int, operation: Int): Boolean {
    return if (operation == Operation.WITHDRAW.code) {
        typeSelected == Type.PIX.code || typeSelected == Type.WALLET.code
    } else when (typeSelected) {
        Type.PIX.code, Type.BILLET.code, Type.WALLET.code, Type.WIRETRANSFER.code -> return true
        else -> return false
    }
}

fun validatePixKeyType(pixKeyTypeSelect: Int?): Boolean {
    return if (pixKeyTypeSelect == null) {
        false
    } else {
        when (pixKeyTypeSelect) {
            TypePixKey.DOCUMENT.code, TypePixKey.EMAIL.code, TypePixKey.PHONE.code -> {
                true
            }
            else -> false
        }
    }
}

fun validateForm(
    dataForm: ValidateForm,
    operation: Int,
    typeSelected: Int,
): ResponseValidateForm {
    val validatedEmail = getFieldStatusEmail(dataForm.email)
    val validatedDocument = getFieldStatusDocument(dataForm.document)
    val validatedEmailEmailWallet = getFieldStatusEmail(dataForm.emailWalletPaylivre)
    val validatedApiToken = getFieldStatusApiToken(dataForm.api_token)
    val validatedPixKeyValue =
        getFieldStatusPixKeyValue(dataForm.pixKeyTypeSelected, dataForm.pixKeyValue)
    val validatedTypeSelected = validateTypesSelected(typeSelected, operation)
    val validatedPixKeyTypeSelected = validatePixKeyType(dataForm.pixKeyTypeSelected)


    var statusFormDataValidated =
        validatedTypeSelected && validatedEmail.isValidated && validatedDocument.isValidated

    if (operation == Operation.DEPOSIT.code && typeSelected == Type.WALLET.code) {
        statusFormDataValidated = statusFormDataValidated &&
                validatedEmailEmailWallet.isValidated &&
                validatedApiToken.isValidated
    } else if (operation == Operation.WITHDRAW.code && typeSelected == Type.PIX.code) {
        statusFormDataValidated =
            statusFormDataValidated &&
                    validatedPixKeyValue.isValidated &&
                    validatedPixKeyTypeSelected

    }


    return ResponseValidateForm(
        statusFormDataValidated,
        validatedEmail,
        validatedDocument,
        validatedEmailEmailWallet,
        validatedApiToken
    )
}