package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature

fun checkDataAutoStartDeposit(depositType: String?): Boolean {
    val isDepositPix = depositType == "1"
    val isDepositBillet = depositType == "2"
    val isDepositWiretransfer = depositType == "4"

    return isDepositPix || isDepositBillet || isDepositWiretransfer
}

fun checkDataAutoStartWithdraw(type: String?, pixKeyType: String?, pixKey: String?): Boolean {
    return if(type == TypesToSelect.PIX.code.toString()){
        val isPixKeyTypeNotDefault = pixKeyType != "-1"
        val isPixKeyValidNotDefault = pixKey != null
        isPixKeyTypeNotDefault && isPixKeyValidNotDefault
    } else {
        true
    }
}


fun validateDataAutoStartByOperation(
    operation: String?,
    type: String?,
    pixKeyType: String?,
    pixKey: String?,
): Boolean {
    return if(operation == Operation.DEPOSIT.code.toString()){
        checkDataAutoStartDeposit(type)
    } else {
        checkDataAutoStartWithdraw(type, pixKeyType, pixKey)
    }
}

//Assume-se que os valores obrigatórios para iniciar a request ja foram verificados
fun checkDataToAutoStartTransaction(dataStartCheckout: DataGenerateSignature): Boolean {

    //cases subject to automatic transaction:
    //1 deposit -> only pix or only billet or only wiretransfer, with email and document
    //1.1 - check if deposit
    //1.2 - check if type contains only (pix or billet or wiretransfer)
    //1.3 - contain email e document

    //2 withdraw -> only pix_key_type and pix_key valid

    val isDataByOperationValid = validateDataAutoStartByOperation(
        dataStartCheckout.operation,
        dataStartCheckout.type,
        dataStartCheckout.pix_key_type,
        dataStartCheckout.pix_key
    )

    val isEmailNotEmpty = !dataStartCheckout.email.isNullOrEmpty()

    val isDocumentNotEmpty = !dataStartCheckout.document_number.isNullOrEmpty()

    return isDataByOperationValid && isEmailNotEmpty && isDocumentNotEmpty
}