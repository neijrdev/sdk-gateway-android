package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.domain.model.*
import org.junit.Assert
import org.junit.Test

class WithdrawlPixKeyTest {
    @Test
    fun `Valid function withdrawlPixKey with Operation Withdrawl and pix_key_type Phone`() {
        val mockOrderDataRequest = OrderDataRequest(
            base_url = "",
            merchant_id = 0,
            merchant_transaction_id = "",
            amount = "",
            currency = "",
            operation = Operation.WITHDRAW.code.toString(),
            callback_url = "",
            type = "",
            selected_type = "",
            account_id = "",
            email = "",
            document_number = "",
            login_email = "",
            api_token = "",
            pix_key = "+5573999229417",
            signature = "",
            pix_key_type = TypePixKey.PHONE.code.toString()
        )


        Assert.assertEquals(
            mockOrderDataRequest,
            addDdiInPixKeyCellPhoneWithdraw(OrderDataRequest(
                base_url = "",
                merchant_id = 0,
                merchant_transaction_id = "",
                amount = "",
                currency = "",
                operation = Operation.WITHDRAW.code.toString(),
                callback_url = "",
                type = "",
                selected_type = "",
                account_id = "",
                email = "",
                document_number = "",
                login_email = "",
                api_token = "",
                pix_key = "73999229417",
                signature = "",
                pix_key_type = TypePixKey.PHONE.code.toString()
            ))
        )
    }
}