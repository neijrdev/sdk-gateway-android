package com.paylivre.sdk.gateway.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.databinding.StartCheckoutBinding
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.domain.model.*


interface IOnBackPressed {
    fun onBackPressed(): Boolean
}


class PaymentActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: StartCheckoutBinding
    private var insertRegisterResultData = InsertRegisterResultData(this)

    /**
     * Does not allow returning to previous navigation within SDK screens
     */
    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.navigation_form_start_payment)
        (fragment as? IOnBackPressed)?.onBackPressed().let {
            // Put the String to pass back into an Intent and close this activity
            val intent = Intent()
            insertRegisterResultData.getIntentWithResultData(intent)

            setResult(Activity.RESULT_OK, intent)
            finish()
            // super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //--> Remove Header Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        //<-- Remove Header Title

        //Set Theme do SDK Gateway Paylivre
        setTheme(R.style.Theme_SDKGatewayAndroid)

        //Set StatusBar
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_bg)

        binding = StartCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.order_data.observe(this, {
            insertRegisterResultData.typeSelect = it?.selected_type?.toInt()
        })

        mainViewModel.currency.observe(this, {
            insertRegisterResultData.currency = it?.toString()
        })

        mainViewModel.checkStatusDepositResponse.observe(this, {
            if (it.status == "success") {
                insertRegisterResultData.transactionStatusId = it.data?.transaction_status_id
                insertRegisterResultData.depositStatusId = it.data?.deposit_status_id
            }
        })

        mainViewModel.statusResponseTransaction.observe(this, {
            insertRegisterResultData.setIntentWithTransactionResponse(it)
        })


        /**
         * Insert in register result data the status check services errors
         */
        mainViewModel.statusResponseCheckServices.observe(this, {
            insertRegisterResultData.isErrorTransaction = 1
            insertRegisterResultData.isErrorTransactionTranslatedMessage = true
            insertRegisterResultData.errorTransactionMessage = it?.messageError
            insertRegisterResultData.errorTransactionMessageDetails = it?.messageErrorDetails
            insertRegisterResultData.errorTransactionCode = it?.errorTagsMessage
        })

        mainViewModel.transfer_proof_response.observe(this, {
            insertRegisterResultData.setIntentWithTransferInsertProofResponse(it)
        })


        mainViewModel.isCloseSDK.observe(this, {
            if (it) {
                println("mainViewModel.closeCheckout()-> " + it)
                // Put the String to pass back into an Intent and close this activity
                val intent = Intent()
                insertRegisterResultData.getIntentWithResultData(intent)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        })

        //getData PaymentData
        val typeStartCheckout: Int = intent.getIntExtra("type_start_checkout", -1)
        val baseUrl = intent.getStringExtra("base_url").toString()
        val email: String? = intent.getStringExtra("email")
        val document: String? = intent.getStringExtra("document")
        val currency: String = intent.getStringExtra("currency").toString()
        val type: Int = intent.getIntExtra("type", -1)
        val merchantTransactionId: String =
            intent.getStringExtra("merchant_transaction_id").toString()
        val accountId: String = intent.getStringExtra("account_id").toString()
        val merchantId: Int = intent.getIntExtra("merchant_id", -1)
        val autoApprove: Int = intent.getIntExtra("auto_approve", -1)
        val gatewayToken: String = intent.getStringExtra("gateway_token").toString()
        val callbackUrl: String = intent.getStringExtra("callback_url").toString()
        val logoResId: Int = intent.getIntExtra("logoResId", -1)
        val logoUrl: String = intent.getStringExtra("logo_url").toString()
        val language: String = intent.getStringExtra("language").toString()

        val pixKey: String? = intent.getStringExtra("pix_key")
        val pixKeyType: Int = intent.getIntExtra("pix_key_type", -1)

        mainViewModel.setLogoResId(logoResId)
        mainViewModel.setLogoUrl(logoUrl)
        mainViewModel.setLanguage(language)

        val amount: Int = intent.getIntExtra("amount", -1)
        val operation: Int = intent.getIntExtra("operation", -1)

        //URL Data
        val signature: String = intent.getStringExtra("signature").toString()

        val dataStartCheckoutString = intent.getStringExtra("dataStartCheckout").toString()
        val dataStartCheckout = Gson().fromJson(
            dataStartCheckoutString,
            DataStartCheckout::class.java
        )

        //validate Data PaymentData
        val dataToStartPaymentIsValid =
            validateDataPayment(
                DataStartCheckout(
                    merchantId,
                    gatewayToken,
                    operation,
                    merchantTransactionId,
                    amount,
                    currency,
                    type,
                    accountId,
                    callbackUrl,
                    email,
                    document,
                    baseUrl,
                    autoApprove,
                    signature = signature,
                    pix_key_type = pixKeyType,
                    pix_key = pixKey

                ),
                typeStartCheckout
            )


        //Insert errors in MainViewModel and hostApi in Shared Preferences
        if (dataToStartPaymentIsValid.isValid) {

            val hostApi = getHostApiByBaseUrl(baseUrl)
            App.setHostAPI(hostApi)

            mainViewModel.setDataStartCheckout(dataStartCheckout)

            mainViewModel.setTypeStartCheckout(typeStartCheckout)

            mainViewModel.setEditEmail(email ?: "")
            mainViewModel.setEditDocument(document ?: "")

            mainViewModel.setEnabledEditEmail(email == null)
            mainViewModel.setEnabledEditDocument(document == null)

            mainViewModel.setGatewayToken(gatewayToken)
            mainViewModel.setCallbackUrl(callbackUrl)
            mainViewModel.setAccountId(accountId)

            mainViewModel.setOperation(operation)
            mainViewModel.setCurrency(currency)
            mainViewModel.setType(type)
            mainViewModel.setAmount(amount)
            mainViewModel.setMerchantTransactionId(merchantTransactionId)
            mainViewModel.setMerchantId(merchantId)

            mainViewModel.setAutoApprove(autoApprove)

            mainViewModel.setBaseUrl(baseUrl)

            mainViewModel.setDataStartPayment(
                DataGenerateSignature(
                    baseUrl,
                    merchantId,
                    gatewayToken,
                    merchantTransactionId,
                    amount.toString(),
                    currency,
                    operation.toString(),
                    callbackUrl,
                    type.toString(),
                    "",
                    accountId,
                    email ?: "",
                    document ?: "",
                    null,
                    null,
                    pix_key_type = pixKeyType.toString(),
                    pix_key = pixKey,
                    autoApprove
                )
            )

        } else {
            //Error Params
            dataToStartPaymentIsValid.messageMainError?.let {
                val msgTitleError = it
                mainViewModel.setIsFatalError(true, msgTitleError)

                insertRegisterResultData.isErrorTransaction = 1
                insertRegisterResultData.errorTransactionMessage = msgTitleError
            }

            dataToStartPaymentIsValid.messageDetailsError?.let {
                val msgDetailsError = it
                mainViewModel.setMessageDetailsError(msgDetailsError)
                insertRegisterResultData.errorTransactionMessageDetails = msgDetailsError

            }

            dataToStartPaymentIsValid.errorTags?.let {
                mainViewModel.setErrorTags(it)
                insertRegisterResultData.errorTransactionCode = it
            }
        }


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_check_services,
                R.id.navigation_form_start_payment,
                R.id.navigation_fatal_error,
                R.id.navigation_loading,
                R.id.navigation_loading_transaction,
                R.id.navigation_transaction_completion,
                R.id.navigation_finish_screen_deposit_pix,
                R.id.navigation_finish_screen_withdraw_pix,
                R.id.navigation_finish_screen_deposit_wallet,
                R.id.navigation_error_kyc_limit,
                R.id.navigation_finish_screen_deposit_billet,
                R.id.navigation_finish_screen_deposit_wiretransfer
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(
                context,
                PaymentActivity::class.java
            )
        }
    }


}