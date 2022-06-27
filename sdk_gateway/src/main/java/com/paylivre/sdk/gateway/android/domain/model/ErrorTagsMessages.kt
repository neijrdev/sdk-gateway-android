package com.paylivre.sdk.gateway.android.domain.model

enum class ErrorTags {
    //Parameters Errors
    RX001, //Parameter merchant_id do not exist in the URL or are not valid
    RX002, //Parameter operation do not exist in the URL or are not valid
    RX003, //Parameter merchant_transaction_id do not exist in the URL or are not valid
    RX004, //Parameter amount do not exist in the URL or are not valid
    RX005, //Parameter currency do not exist in the URL or are not valid
    RX006, //Parameter account_id do not exist in the URL or are not valid
    RX007, //Parameter callback_url do not exist in the URL or are not valid
    RX008, //Parameter signature do not exist in the URL or are not valid
    RX009, //Parameter order do not exist in the URL or are not valid
    RX010, //Parameter token do not exist in the URL or are not valid
    RX011, //Parameter type do not exist in the URL, or are not valid
    RX012, //Parameter auto_approve do not exist in the URL, or are not valid
    RX013, //Parameter email not valid
    RX014, //Parameter document_number not valid

    RX015, //Parameter pix_key_type not valid
    RX016, //Parameter pix_key not valid

    //Error parameters SDK StartCheckoutByParams
    RP001, //Parameter base_url do not exist or are not valid
    RP002, //Parameter gateway_token do not exist or are not valid

    //Response Errors
    UX000, //Unknown error
    UX005, //Request failed with status code 500
    UA001, //The given document number is already associated to another user at Paylivre.
    UA113, //Something went wrong while creating your order receivable.
    UA515, //Partner Wallet not found or insufficient funds.
    UB104, //401 Error
    UC001, //Merchant not found.
    UC002, //Invalid signature.
    UC003, //This action is unauthorized.
    UD001, //Deposit not found.
    UC004, //Could not verify the merchant signature.

    RT001, //The given data was invalid: email
    RT002, //The given data was invalid: amount
    RT003, //The given data was invalid: currency
    RT004, //The given data was invalid: document_number
    RT005, //The given data was invalid: callback_url
    RT006, //The given data was invalid: operation
    RT007, //The given data was invalid: merchant_transaction_id
    RT008, //The given data was invalid: auto_approve


}

fun getErrorTagResponseError(errorMessage: String): String {
    return when (errorMessage) {
        "Request failed with status code 500" -> ErrorTags.UX005.toString()
        "Merchant not found." -> ErrorTags.UC001.toString()
        "Invalid signature." -> ErrorTags.UC002.toString()
        "401" -> ErrorTags.UB104.toString()
        "Something went wrong while creating your order receivable." -> ErrorTags.UA113.toString()
        "This action is unauthorized." -> ErrorTags.UC003.toString()
        "Deposit not found." -> ErrorTags.UD001.toString()
        "Partner Wallet not found or insufficient funds." -> ErrorTags.UA515.toString()
        "Could not verify the merchant signature." -> ErrorTags.UC004.toString()
        else -> "UX000"
    }
}

data class KeysResponseError(
    val keyMessage: String?,
    val keyMessageDetails: String?,
)

fun getStringKeyResponseError(error: String): KeysResponseError {
    val genericError = "invalid_data_error"

    if (error.contains(
            "The given document number does not match the document number associated with user with email",
            true
        )
    ) {
        return KeysResponseError(
            keyMessage = genericError, keyMessageDetails = "document_not_match"
        )

    }

    if (error.contains(
            "Our document validation service could not approve your document validation.",
            true
        )
    ) {
        return KeysResponseError(
            keyMessage = genericError, keyMessageDetails = "validation_service_error"
        )
    }

    if (error.contains("User not found with document number", true)
    ) {
        return KeysResponseError(
            keyMessage = genericError, keyMessageDetails = "user_not_found_with_document_number"
        )
    }

    return when (error) {
        "The user has reached the limit of 3 orders with equal amount in the last 24 hours." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_3_orders_with_equal_amount"
            )
        }
        "Insufficient Wallet funds." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_insufficient_wallet_funds"
            )
        }
        "The given document number is already associated to another user at Paylivre." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "document_match_another_user"
            )
        }
        "The given document number does not match the user's document." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "document_not_match"
            )
        }
        "The given document number does not match" -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "document_not_match"
            )
        }
        "The withdrawal amount must be a value between 500 and 10000000" -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_withdrawal_amount_between"
            )
        }
        "Invalid deposit amount. The minimum amount allowed is 500" -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_deposit_min_amount"
            )
        }
        "Could not complete your transfer due to insufficient funds." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_insufficient_wallet_funds"
            )
        }
        "The requested withdrawal exceeds the maximum amount allowed." -> {
            KeysResponseError(
                keyMessage = "exceeded_withdrawal_limit_title",
                keyMessageDetails = "exceeded_withdrawal_limit_value"
            )
        }
        "The informed PIX key CPF must be the same as the CPF from user acccount." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_pix_key_cpf_divergent"
            )
        }
        "The informed PIX key CNPJ must be the same as the CNPJ from user acccount." -> {
            KeysResponseError(
                keyMessage = genericError, keyMessageDetails = "error_pix_key_cnpj_divergent"
            )
        }
        "The version of the paylivre gateway sdk used is outdated" -> {
            KeysResponseError(
                keyMessage = "paylivre_gateway_sdk_is_outdated", keyMessageDetails = ""
            )
        }
        else -> KeysResponseError(
            keyMessage = genericError, keyMessageDetails = ""
        )
    }
}