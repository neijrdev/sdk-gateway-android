package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.content.Context
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccount
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts


fun getEnabledBanksAccounts(bankAccounts: BankAccounts): List<BankAccount>? {
    return bankAccounts.bank_accounts?.filter { it.hidden != 1 && it.bank?.display != 0 }
}

fun getNameBanksAccountsList(bankAccounts: List<BankAccount>): List<String> {
    val listBanksAccounts: MutableList<String> = mutableListOf()
    bankAccounts?.map { it.account_name?.let { it1 -> listBanksAccounts.add(it1) } }
    return listBanksAccounts
}

fun getBankOffice(officeNumber: Int?, officeDigit: Int?): String {
    val officeNumberFormatted = officeNumber.toString().padStart(4, '0') ?: ""
    val officeCodeFormatted = officeDigit ?: ""

    return if (officeCodeFormatted.toString().isNullOrEmpty()) {
        officeNumberFormatted.toString()
    } else {
        "$officeNumberFormatted-$officeCodeFormatted"
    }
}

fun getBankAccountNumber(accountNumber: String?, accountDigit: String?): String {
    val officeNumberFormatted = accountNumber ?: ""
    val officeCodeFormatted = accountDigit ?: ""

    return if (officeCodeFormatted.isNullOrEmpty()) {
        officeNumberFormatted
    } else {
        "$officeNumberFormatted-$officeCodeFormatted"
    }
}

fun getDocumentAccountNumber(documentAccountNumber:String?): String {
    if(documentAccountNumber!= null){
        return documentAccountNumber.replace("CNPJ: ", "")
    }
    return ""
}