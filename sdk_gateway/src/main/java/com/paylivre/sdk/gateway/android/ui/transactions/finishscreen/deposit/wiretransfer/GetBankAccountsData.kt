package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.content.Context
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccount
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts


fun getEnabledBanksAccounts(bankAccounts: BankAccounts): List<BankAccount>? {
    return bankAccounts.bank_accounts?.filter { it.hidden != 1 }
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

fun getBankAccountInfo(context: Context, bankAccount: BankAccount): String {
    val office = getBankOffice(bankAccount.office_number, bankAccount.office_digit)
    val account = getBankAccountNumber(bankAccount.account_number, bankAccount.account_digit)
    val bankLabel = context.resources.getString(R.string.label_bank)
    val agencyLabel = context.resources.getString(R.string.label_bank_office)
    val accountLabel = context.resources.getString(R.string.label_account)

    val bankAccountInfo =  "$bankLabel: ${bankAccount.account_name}\n$agencyLabel: $office\n$accountLabel: $account\n"
    val accountOwnerInfo = "${bankAccount.account_holder_full_name}\n${bankAccount.account_holder_document}"
    return "$bankAccountInfo$accountOwnerInfo"
}