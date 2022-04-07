package com.example.paylivre.sdk.gateway.utils

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

data class TypesChecked(
    var WALLET: Int,
    var WIRETRANSFER: Int,
    var BILLET: Int,
    var PIX: Int,
)

enum class Types(val code: Int) {
    WIRETRANSFER(0),
    BILLET(1),
    PIX(4),
    WALLET(5),
}

fun convertBinaryToDecimal(num: Long): Int {
    var num = num
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}


fun getNumberByTypesChecked(types: TypesChecked): Int {
    val valueTypeWireTransfer = if (types.WIRETRANSFER == 1) "1" else "0"
    val valueTypeBillet = if (types.BILLET == 1) "1" else "0"
    val valueTypePix = if (types.PIX == 1) "1" else "0"
    val valueTypeWallet = if (types.WALLET == 1) "1" else "0"

    val valuesTypesInBinary =
        valueTypeWallet + valueTypeWireTransfer + valueTypeBillet + valueTypePix

    return convertBinaryToDecimal(valuesTypesInBinary.toLong())
}