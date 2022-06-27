package com.paylivre.sdk.gateway.android.domain.model

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class InsertRegisterResultDataTest {

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `Test insertRegisterResultData initial data`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)
        Assert.assertEquals(null, insertRegisterResultData.typeSelect)
        Assert.assertEquals(null, insertRegisterResultData.transactionStatusId)
        Assert.assertEquals(null, insertRegisterResultData.depositStatusId)
        Assert.assertEquals(null, insertRegisterResultData.transactionResponse)
        Assert.assertEquals(null, insertRegisterResultData.isGeneratedTransaction)
        Assert.assertEquals(null, insertRegisterResultData.isErrorTransaction)
        Assert.assertEquals(null, insertRegisterResultData.isErrorWalletInvalidApiToken)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionMessage)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionMessageDetails)
        Assert.assertEquals(null, insertRegisterResultData.isUserCompletedTransaction)
        Assert.assertEquals(null, insertRegisterResultData.actionNotCompletedCode)
        Assert.assertEquals(null, insertRegisterResultData.actionNotCompletedMessage)
        Assert.assertEquals(null, insertRegisterResultData.errorCompletedTransactionMessage)
        Assert.assertEquals(null, insertRegisterResultData.currency)
    }

    @Test
    fun `Test setIntentWithTransferInsertProofResponse with transferInsert isSuccess true`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)

        //success Inserted Transfer Proof
        insertRegisterResultData.setIntentWithTransferInsertProofResponse(
            InsertTransferProofDataResponse(1, null, 1,
                1, 1, false, "", true))
        Assert.assertEquals(null, insertRegisterResultData.actionNotCompletedCode)
        Assert.assertEquals(null, insertRegisterResultData.actionNotCompletedMessage)
        Assert.assertEquals(null, insertRegisterResultData.errorCompletedTransactionMessage)
    }

    @Test
    fun `Test setIntentWithTransferInsertProofResponse with transferInsert isSuccess false`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)

        //success Inserted Transfer Proof
        insertRegisterResultData.setIntentWithTransferInsertProofResponse(
            InsertTransferProofDataResponse(null, null, null,
                null, null, false, "test error", false))

        Assert.assertEquals("RC001", insertRegisterResultData.actionNotCompletedCode)
        Assert.assertEquals("User did not insert proof of deposit type bank transfer",
            insertRegisterResultData.actionNotCompletedMessage)
        Assert.assertEquals("test error",
            insertRegisterResultData.errorCompletedTransactionMessage)
    }


    var mockResponseTransactionDataSuccess = ResponseCommonTransactionData(
        full_name = "User Test",
        document_number = "59540986036",
        kyc_limits = LimitsKyc(null, null, null, null, null),
        original_amount = 500,
        origin_amount = 500,
        original_currency = "BRL",
        final_amount = 500,
        converted_amount = 0,
        taxes = 0,
        receivable_url = "https://teste.com",
        deposit_type_id = 0,
        redirect_url = "https://teste.com",
        billet_digitable_line = "",
        billet_due_date = "",
        deposit_id = 0,
        order_id = 0,
        transaction_id = 0,
        withdrawal = WithdrawalData(null, null, null),
        order = OrderData(),
        bank_accounts = null,
        verification_token = "",
        token = "",
        withdrawal_type_id = 0,
    )


    @Test
    fun `Test setIntentWithTransactionResponse with TransactionResponse isSuccess true`() {
        //GIVEN
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)
        //mock data transaction was successful
        val mockTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = mockResponseTransactionDataSuccess,
            error = ErrorTransaction(),
        )

        //WHEN
        //if the transaction was successful
        insertRegisterResultData.setIntentWithTransactionResponse(mockTransactionResponse)

        val mockIntent = Intent()
        insertRegisterResultData.getIntentWithResultData(mockIntent)

        //THEN
        Assert.assertEquals(mockTransactionResponse, insertRegisterResultData.transactionResponse)
        Assert.assertEquals(0, insertRegisterResultData.isErrorTransaction)
        Assert.assertEquals(1, insertRegisterResultData.isGeneratedTransaction)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionCode)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionMessageDetails)
        Assert.assertEquals(null, insertRegisterResultData.isErrorWalletInvalidApiToken)

        //check the data returned to activity via extras
        val extrasIntentExpected = Bundle()
        extrasIntentExpected.putString("deposit_type_id", "0")
        extrasIntentExpected.putString("error_transaction_code", "null")
        extrasIntentExpected.putString("error_transaction_message", "null")
        extrasIntentExpected.putString("deposit_status_id", "null")
        extrasIntentExpected.putString("is_generated_transaction", "1")
        extrasIntentExpected.putString("order_type_id", "null")
        extrasIntentExpected.putString("withdrawal_id", "null")
        extrasIntentExpected.putString("action_not_completed_message", "null")
        extrasIntentExpected.putString("action_not_completed_code", "null")
        extrasIntentExpected.putString("deposit_id", "0")
        extrasIntentExpected.putString("is_error_transaction", "0")
        extrasIntentExpected.putString("error_completed_transaction_message", "null")
        extrasIntentExpected.putString("withdrawal_type_id", "0")
        extrasIntentExpected.putString("order_status_id", "null")
        extrasIntentExpected.putString("transaction_id", "0")
        extrasIntentExpected.putString("is_user_completed_transaction", "1")
        extrasIntentExpected.putString("order_id", "0")
        extrasIntentExpected.putString("selected_type", "null")
        extrasIntentExpected.putString("withdrawal_status_id", "null")
        extrasIntentExpected.putString("transaction_status_id", "null")

        Assert.assertEquals(extrasIntentExpected.toString(), mockIntent.extras.toString())
    }

    @Test
    fun `Test setIntentWithTransactionResponse with TransactionResponse isSuccess true and typeSelect == WIRETRANSFER`() {
        //GIVEN
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)
        //mock data transaction was successful
        insertRegisterResultData.typeSelect = Type.WIRETRANSFER.code

        val mockTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = mockResponseTransactionDataSuccess,
            error = ErrorTransaction(),
        )

        //WHEN
        //if the transaction was successful
        insertRegisterResultData.setIntentWithTransactionResponse(mockTransactionResponse)
        val mockIntent = Intent()
        insertRegisterResultData.getIntentWithResultData(mockIntent)

        //check the data returned to activity via extras
        val extrasIntentExpected = Bundle()
        extrasIntentExpected.putString("deposit_type_id", "0")
        extrasIntentExpected.putString("error_transaction_code", "null")
        extrasIntentExpected.putString("error_transaction_message", "null")
        extrasIntentExpected.putString("deposit_status_id", "null")
        extrasIntentExpected.putString("is_generated_transaction", "1")
        extrasIntentExpected.putString("order_type_id", "null")
        extrasIntentExpected.putString("withdrawal_id", "null")
        extrasIntentExpected.putString("action_not_completed_message", "User did not insert proof of deposit type bank transfer")
        extrasIntentExpected.putString("action_not_completed_code", "RC001")
        extrasIntentExpected.putString("deposit_id", "0")
        extrasIntentExpected.putString("is_error_transaction", "0")
        extrasIntentExpected.putString("error_completed_transaction_message", "null")
        extrasIntentExpected.putString("withdrawal_type_id", "0")
        extrasIntentExpected.putString("order_status_id", "null")
        extrasIntentExpected.putString("transaction_id", "0")
        extrasIntentExpected.putString("is_user_completed_transaction", "0")
        extrasIntentExpected.putString("order_id", "0")
        extrasIntentExpected.putString("selected_type", "0")
        extrasIntentExpected.putString("withdrawal_status_id", "null")
        extrasIntentExpected.putString("transaction_status_id", "null")

        //THEN
        Assert.assertEquals(mockTransactionResponse, insertRegisterResultData.transactionResponse)
        Assert.assertEquals(0, insertRegisterResultData.isErrorTransaction)
        Assert.assertEquals(1, insertRegisterResultData.isGeneratedTransaction)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionCode)
        Assert.assertEquals(null, insertRegisterResultData.errorTransactionMessageDetails)
        Assert.assertEquals(null, insertRegisterResultData.isErrorWalletInvalidApiToken)
        Assert.assertEquals(extrasIntentExpected.toString(), mockIntent.extras.toString())

    }


    @Test
    fun `Test setIntentWithTransactionResponse with TransactionResponse isSuccess false`() {
        //GIVEN
        val context = ApplicationProvider.getApplicationContext<Context>()
        var insertRegisterResultData = InsertRegisterResultData(context)
        //mock data transaction was successful
        val mockTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = false,
            data = mockResponseTransactionDataSuccess,
            error = ErrorTransaction(
                messageDetails = "error_details",
                errorTags = "ERROR_TAGS_TEST"
            ),
        )

        //WHEN
        //if the transaction was failure
        insertRegisterResultData.setIntentWithTransactionResponse(mockTransactionResponse)


        //THEN
        Assert.assertEquals(null, insertRegisterResultData.transactionResponse)
        Assert.assertEquals(1, insertRegisterResultData.isErrorTransaction)
        Assert.assertEquals(0, insertRegisterResultData.isGeneratedTransaction)
        Assert.assertEquals("ERROR_TAGS_TEST", insertRegisterResultData.errorTransactionCode)
        Assert.assertEquals("error_details",
            insertRegisterResultData.errorTransactionMessageDetails)
        Assert.assertEquals(false, insertRegisterResultData.isErrorWalletInvalidApiToken)

    }

}