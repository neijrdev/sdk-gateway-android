package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet

import java.lang.Exception
import java.text.SimpleDateFormat


fun getBilletDueDateFormatted(billet_due_date: String?): String? {
    return try {
        if (!billet_due_date.isNullOrEmpty()) {
            val parser =  SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = formatter.format(parser.parse(billet_due_date))
            formattedDate
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }


}