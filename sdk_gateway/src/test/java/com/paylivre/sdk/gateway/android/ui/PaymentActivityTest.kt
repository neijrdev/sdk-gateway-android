package com.paylivre.sdk.gateway.android.ui

import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.core.app.launchActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.PaymentActivity
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.deposit.DataStatusDeposit
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class PaymentActivityTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()

    private var activity: PaymentActivity? = null

    @Before
    fun setUp() {
        loadKoinModules(MockMainViewModel().mockedAppModule)

        val dataStartCheckout = DataStartCheckout(
            merchant_id = 123,
            gateway_token = "12345678",
            operation = Operation.WITHDRAW.code,
            merchant_transaction_id = "123456asd",
            amount = 500,
            currency = "BRL",
            type = 1,
            account_id = "12345646",
            callback_url = "https://callback.com",
            base_url = "https://dev.gateway.paylivre.com",
            auto_approve = 1,
            email_address = "test@test.com",
            document = "61317581075",
        )

        val intent = Intent()
        intent.putExtra("type_start_checkout", TypesStartCheckout.BY_PARAMS.code)
        intent.putExtra("merchant_id", dataStartCheckout.merchant_id)
        intent.putExtra("gateway_token", dataStartCheckout.gateway_token)
        intent.putExtra("operation", dataStartCheckout.operation)
        intent.putExtra("merchant_transaction_id", dataStartCheckout.merchant_transaction_id)
        intent.putExtra("amount", dataStartCheckout.amount)
        intent.putExtra("currency", dataStartCheckout.currency)
        intent.putExtra("type", dataStartCheckout.type) //PIX
        intent.putExtra("account_id", dataStartCheckout.account_id)
        intent.putExtra("callback_url", dataStartCheckout.callback_url)
        intent.putExtra("base_url", dataStartCheckout.base_url)
        intent.putExtra("auto_approve", dataStartCheckout.auto_approve)
//        intent.putExtra("document", dataStartCheckout.document)
//        intent.putExtra("email", dataStartCheckout.email_address)

        val dataStartCheckoutString = Gson().toJson(dataStartCheckout)
        intent?.putExtra("dataStartCheckout", dataStartCheckoutString)


        activity = Robolectric.buildActivity(PaymentActivity::class.java, intent)
            .create()
            .resume()
            .get()
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `test PaymentActivity - onBackPressed`() {
        val activityScenario = launchActivity<PaymentActivity>()

        activityScenario.onActivity {
            //GIVEN - active activity
            assertFalse(it.isFinishing);

            //WHEN - The onBackPressed event dispatch
            it.onBackPressed()


            //THEN - activity is finishing
            assertTrue(it.isFinishing);
        }
    }

    @Test
    fun `test PaymentActivity - mainViewModel isCloseSDK`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN - active activity
            assertFalse(it.isFinishing);

            //WHEN - Dispatch isCloseSDK = true
            it.mainViewModel.setIsCloseSDK(true)


            //THEN - activity is finishing
            assertTrue(it.isFinishing);
        }
    }

    @Test
    fun `test PaymentActivity - mainViewModel statusResponseCheckServices`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN - active activity

            //WHEN - Dispatch setStatusResponseCheckServices = null
            it.mainViewModel.setStatusResponseCheckServices(null)


            //THEN
            assertTrue(it.insertRegisterResultData.isErrorTransaction == 1);
            assertTrue(it.insertRegisterResultData.isErrorTransactionTranslatedMessage == true);
            assertTrue(it.insertRegisterResultData.errorTransactionMessage == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessageDetails == null);
            assertTrue(it.insertRegisterResultData.errorTransactionCode == null);
        }
    }

    @Test
    fun `test PaymentActivity - mainViewModel checkStatusDepositResponse`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN - active activity

            //WHEN - Dispatch checkStatusDepositSuccess given status = "success"
            it.mainViewModel.checkStatusDepositSuccess(CheckStatusDepositResponse(
                status = "success",
                status_code = 200,
                message = "ok",
                data = DataStatusDeposit(
                    transaction_status_id = 1,
                    deposit_status_id = 1
                )
            ))


            //THEN
            assertTrue(it.insertRegisterResultData.transactionStatusId == 1);
            assertTrue(it.insertRegisterResultData.depositStatusId == 1);
        }
    }


    @Test
    fun `test PaymentActivity - mainViewModel statusResponseTransaction  isSuccess = false`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN

            //WHEN - Dispatch setStatusTransactionResponse given data = null
            it.mainViewModel.setStatusTransactionResponse(
                StatusTransactionResponse(isLoading = false,
                    isSuccess = false,
                    data = null,
                    error = null)
            )


            //THEN - activity is finishing
            assertTrue(it.insertRegisterResultData.isGeneratedTransaction == 0);
            assertTrue(it.insertRegisterResultData.isErrorTransaction == 1);
            assertTrue(it.insertRegisterResultData.errorTransactionCode == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessage == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessageDetails == null);
            assertTrue(it.insertRegisterResultData.isErrorWalletInvalidApiToken == false);
        }
    }

    @Test
    fun `test statusResponseTransaction  isSuccess = true`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN
            val mockStatusResponseTransaction = StatusTransactionResponse(
                isLoading = false,
                isSuccess = true,
                data = null,
                error = null
            )

            //WHEN - Dispatch setStatusTransactionResponse given data = null
            it.mainViewModel.setStatusTransactionResponse(
                mockStatusResponseTransaction
            )

            //THEN - activity is finishing
            assertTrue(it.insertRegisterResultData.transactionResponse == mockStatusResponseTransaction);
            assertTrue(it.insertRegisterResultData.isGeneratedTransaction == 1);
            assertTrue(it.insertRegisterResultData.isErrorTransaction == 0);
            assertTrue(it.insertRegisterResultData.errorTransactionCode == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessage == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessageDetails == null);
            assertTrue(it.insertRegisterResultData.isErrorWalletInvalidApiToken == null);
            assertTrue(it.insertRegisterResultData.isUserCompletedTransaction == 1);
        }
    }

    @Test
    fun `test statusResponseTransaction  isSuccess = true and WIRETRANSFER`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN
            val mockStatusResponseTransaction = StatusTransactionResponse(
                isLoading = false,
                isSuccess = true,
                data = null,
                error = null
            )

            it.insertRegisterResultData.typeSelect = Type.WIRETRANSFER.code

            //WHEN - Dispatch setStatusTransactionResponse given data = null
            it.mainViewModel.setStatusTransactionResponse(
                mockStatusResponseTransaction
            )

            //THEN - activity is finishing
            assertTrue(it.insertRegisterResultData.transactionResponse == mockStatusResponseTransaction);
            assertTrue(it.insertRegisterResultData.isGeneratedTransaction == 1);
            assertTrue(it.insertRegisterResultData.isErrorTransaction == 0);
            assertTrue(it.insertRegisterResultData.errorTransactionCode == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessage == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessageDetails == null);
            assertTrue(it.insertRegisterResultData.isErrorWalletInvalidApiToken == null);
            assertTrue(it.insertRegisterResultData.isUserCompletedTransaction == 0);
            assertTrue(it.insertRegisterResultData.actionNotCompletedCode ==
                    ErrorCompletedTransaction.RC001.toString());
            assertTrue(it.insertRegisterResultData.actionNotCompletedMessage ==
                    "User did not insert proof of deposit type bank transfer");
            assertTrue(it.insertRegisterResultData.errorCompletedTransactionMessage == null);
        }
    }


    @Test
    fun `test StatusTransactionResponse  isSuccess = true`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //WHEN
            val responseExpectedString =
                fileTestsUtils.loadJsonAsString("response_deposit_pix_success.json")

            val expectedDataResponse = Gson().fromJson(
                responseExpectedString, ResponseCommonTransactionData::class.java
            )

            //GIVEN
            val mockStatusTransactionResponse = StatusTransactionResponse(
                isLoading = false,
                isSuccess = true,
                data = expectedDataResponse,
                error = null
            )


            //WHEN - Dispatch setStatusTransactionResponse given data = null
            it.mainViewModel.setStatusTransactionResponse(
                mockStatusTransactionResponse
            )

            //THEN - activity is finishing
            assertTrue(it.insertRegisterResultData.transactionResponse == mockStatusTransactionResponse);
            assertTrue(it.insertRegisterResultData.isGeneratedTransaction == 1);
            assertTrue(it.insertRegisterResultData.isErrorTransaction == 0);
            assertTrue(it.insertRegisterResultData.errorTransactionCode == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessage == null);
            assertTrue(it.insertRegisterResultData.errorTransactionMessageDetails == null);
            assertTrue(it.insertRegisterResultData.isErrorWalletInvalidApiToken == null);
            assertTrue(it.insertRegisterResultData.isUserCompletedTransaction == 1);
            assertTrue(it.insertRegisterResultData.errorCompletedTransactionMessage == null);
        }
    }


    @Test
    fun `test order_data null`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN

            //WHEN
            it.mainViewModel.setDataRequestOrder(null)

            //THEN
            assertTrue(it.insertRegisterResultData.typeSelect == null);
        }
    }


    @Test
    fun `test setIntentWithTransferInsertProofResponse isSuccess = true`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //WHEN
            val responseExpectedString =
                fileTestsUtils.loadJsonAsString("res_insert_transfer_proof_success.json")

            val expectedDataResponse = Gson().fromJson(
                responseExpectedString, InsertTransferProofDataResponse::class.java
            )

            val mockStatusTransactionResponse = InsertTransferProofDataResponse(
                expectedDataResponse.id,
                expectedDataResponse.proof,
                expectedDataResponse.wallet_id,
                expectedDataResponse.user_id,
                expectedDataResponse.deposit_status_id,
                isSuccess = true,
                loading = false,
                error = null,
            )


            //WHEN
            it.mainViewModel.insertTransferProofSuccess(
                mockStatusTransactionResponse
            )

            //THEN - activity is finishing
            assertTrue(it.insertRegisterResultData.actionNotCompletedCode == null);
            assertTrue(it.insertRegisterResultData.actionNotCompletedMessage == null);
            assertTrue(it.insertRegisterResultData.errorCompletedTransactionMessage == null);
        }
    }


    @Test
    fun `test setIntentWithTransferInsertProofResponse isSuccess = false`() {
        val activityScenario = launchActivity<PaymentActivity>()
        activityScenario.onActivity {
            //GIVEN
            val mockError = RuntimeException("error")

            //WHEN
            it.mainViewModel.insertTransferProofFailure(
                mockError
            )


            //THEN
            assertTrue(it.insertRegisterResultData.actionNotCompletedCode ==
                    ErrorCompletedTransaction.RC001.toString());
            assertTrue(it.insertRegisterResultData.actionNotCompletedMessage ==
                    "User did not insert proof of deposit type bank transfer");
            assertTrue(it.insertRegisterResultData.errorCompletedTransactionMessage ==
                    mockError.message);

        }
    }
}