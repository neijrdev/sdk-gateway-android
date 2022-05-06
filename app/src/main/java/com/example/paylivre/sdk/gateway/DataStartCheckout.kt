package com.example.paylivre.sdk.gateway

data class DataStartCheckout(
    val merchant_id: String,
    val gateway_token: String,
    val email_address: String,
    val document: String,
    val operation: Int,
    val merchant_transaction_id: String,
    val amount: Int,
    val currency: String,
    val type: List<Int>,
    val account_id: String,
    val callback_url: String,

)

enum class Operation(val code: Int) {
    DEPOSIT (0),
    WITHDRAW(5),
}

enum class Environments {
    PLAYGROUND,
    PRODUCTION,
    DEVELOPMENT,
}

enum class Languages {
    PT,
    EN,
    ES,
}


enum class Type(val code: Int) {
    WIRETRANSFER(0), //TED
    BILLET(1), //Boleto
    PIX(4),
    WALLET(5) //Saldo Paylivre
}

enum class Currency(val currency: String) {
    BRL("BRL"), //TED
    USD("USD"), //Boleto
}