package com.paylivre.sdk.gateway.android.domain.model

import com.paylivre.sdk.gateway.android.utils.convertDecimalToBinary


// 0000 - 0 - Nenhum
// 0001 - 1 - Pix
// 0010 - 2 - Boleto
// 0011 - 3 - Boleto, Pix
// 0100 - 4 - TED
// 0101 - 5 - TED, Pix
// 0110 - 6 - TED, Boleto
// 0111 - 7 - TED, Boleto, PIX
// 1000 - 8 - Wallet
// 1001 - 9 - Wallet, Pix
// 1010 - 10 - Wallet, Boleto
// 1011 - 11 - Wallet, Boleto, Pix
// 1100 - 12 - Wallet, Ted
// 1101 - 13 - Wallet, Ted, Pix
// 1110 - 14 - Wallet, Ted, Boleto
// 1111 - 15 - Wallet, Ted, Boleto, Pix (Todas)

// 0 - TED
// 1 - Boleto
// 4 - PIX
// 5 - Wallet

enum class Types(val code: Int) {
    WIRETRANSFER(0),
    BILLET(1),
    PIX(4),
    WALLET(5),
}

enum class Environments {
    PLAYGROUND,
    PRODUCTION,
    DEVELOPMENT,
}

fun getBinaryStringByDecimalNumber(types: Int): String {
    return convertDecimalToBinary(types).toString().padStart(4, '0')
}

fun checkTypeEnable(types: Int, typeCode: Int): Boolean {
    val typesBinaryString = getBinaryStringByDecimalNumber(types)

    when (typeCode){
        Type.WIRETRANSFER.code -> {
            val typeWireTransfer = typesBinaryString.subSequence(1, 2).toString()
            return typeWireTransfer == "1"
        }
        Type.BILLET.code -> {
            val typeBillet = typesBinaryString.subSequence(2, 3).toString()
            return typeBillet == "1"
        }
        Type.PIX.code -> {
            val typePix = typesBinaryString.subSequence(3, 4).toString()
            return typePix == "1"
        }
        Type.WALLET.code -> {
            val typeWallet = typesBinaryString.subSequence(0, 1).toString()
            return typeWallet == "1"
        }
        else -> {
            return false
        }
    }
}

