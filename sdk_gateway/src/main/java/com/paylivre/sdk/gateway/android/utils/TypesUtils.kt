package com.paylivre.sdk.gateway.android.utils

import android.content.Context
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServicesStatus
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.domain.model.checkTypeEnable

enum class BitwiseTypes(val decimal: Int) {
    WIRETRANSFER(4),
    BILLET(2),
    PIX(1),
    WALLET(8),
}

fun getNumberByTypes(types: ServicesStatus): Int {
    val valueTypeWireTransfer = if (types.statusWiretransfer == true) "1" else "0"
    val valueTypeBillet = if (types.statusBillet == true) "1" else "0"
    val valueTypePix = if (types.statusPix == true) "1" else "0"
    val valueTypeWallet = if (types.statusWallet == true) "1" else "0"

    val valuesTypesInBinary =
        valueTypeWallet + valueTypeWireTransfer + valueTypeBillet + valueTypePix

    return convertBinaryToDecimal(valuesTypesInBinary.toLong())
}


fun getTypesKeyNameInNumberTypes(types: Int): MutableList<String> {
    var typesKeyName: MutableList<String> = mutableListOf()

    if (checkTypeEnable(types, Types.PIX.code)) {
        typesKeyName.add("type_pix")
    }
    if (checkTypeEnable(types, Types.BILLET.code)) {
        typesKeyName.add("type_billet")
    }
    if (checkTypeEnable(types, Types.WIRETRANSFER.code)) {
        typesKeyName.add("title_wire_transfer")
    }

    return typesKeyName
}

fun getNameByTypesKeys(context: Context, typesKeyNames: MutableList<String>): String {
    return typesKeyNames.joinToString { getStringByKey(context, it) }
}