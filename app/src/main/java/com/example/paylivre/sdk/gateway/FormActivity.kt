package com.example.paylivre.sdk.gateway

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.paylivre.sdk.gateway.data.*
import com.example.paylivre.sdk.gateway.databinding.ActivityFormBinding
import com.example.paylivre.sdk.gateway.model.DataGenerateUrl
import com.example.paylivre.sdk.gateway.model.GenerateUrlToCheckout
import com.example.paylivre.sdk.gateway.utils.*
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import com.paylivre.sdk.gateway.android.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.domain.model.TypePixKey


class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set Theme do SDK Gateway Paylivre
        setTheme(R.style.Theme_SDKGatewayAndroid)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Form Generate Checkout - ${BuildConfig.VERSION_NAME}"
        }


        val id_merchant = binding.editMerchantId
        val merchant_transaction_id = binding.editMerchantTransactionId
        val gateway_token = binding.editGatewayToken
        val user_email = binding.editUserEmail
        val account_id = binding.editAccountId
        val user_document = binding.editUserDocument
        val amount = binding.editAmount
        val checkPix = binding.checkPix
        val checkBillet = binding.checkBillet
        val checkWireTransfer = binding.checkWireTransfer
        val checkWallet = binding.checkWallet
        var operation = Operation.DEPOSIT.code
        val callback_url = binding.editCallbackUrl
        val base_url = binding.editBaseUrl
        var type = 1
        var typesChecked = TypesChecked(0, 0, 0, 1)

        fun View.hideKeyboard() {
            val imm =
                context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(applicationWindowToken, 0)
        }

        fun setAllOptionsDeposit(visibility: Int) {
            checkPix.visibility = visibility
            checkBillet.visibility = visibility
            checkWallet.visibility = visibility
            checkWireTransfer.visibility = visibility
        }

        fun getVisibility(isVisible: Boolean): Int {
            return if (isVisible) View.VISIBLE else View.GONE
        }

        fun setIsShowPixFields(isShow: Boolean) {
            binding.containerCheckAutoWithdraw.visibility = getVisibility(isShow)
            binding.containerCheckBoxPixKeyType.visibility = getVisibility(isShow)
            binding.checkIgnorePixKeyType.visibility = getVisibility(isShow)
            binding.layoutEditPixKeyValue.visibility = getVisibility(isShow)
            binding.checkIgnorePixKey.visibility = getVisibility(isShow)

            binding.layoutEditPixKeyValue.isEnabled = isShow
            binding.editPixKeyValue.isEnabled = isShow
            binding.checksPixKeyType.isEnabled = isShow
        }

        fun setValuesDefaultPixFields() {
            binding.checksPixKeyTypeDocument.isChecked = true
            binding.editPixKeyValue.setText(USER_DOCUMENT_NUMBER)
            binding.checkIgnorePixKeyType.isChecked = false
            binding.checkIgnorePixKey.isChecked = false
        }

        fun setCheckedAllTypes(isChecked: Boolean) {
            checkPix.isChecked = isChecked
            checkBillet.isChecked = isChecked
            checkWallet.isChecked = isChecked
            checkWireTransfer.isChecked = isChecked
            type = if (isChecked) 15 else 0
            typesChecked = if (isChecked)
                TypesChecked(1, 1, 1, 1) else
                TypesChecked(0, 0, 0, 0)
        }

        fun getBaseUrlByEnvironment(environment: String): String {
            return when (environment) {
                Environments.PLAYGROUND.toString() -> BASE_URL_ENVIRONMENT_PLAYGROUND
                Environments.PRODUCTION.toString() -> BASE_URL_ENVIRONMENT_PRODUCTION
                Environments.DEVELOPMENT.toString() -> BASE_URL_ENVIRONMENT_DEV
                else -> "INVALID_ENVIRONMENT"
            }
        }


        fun setDataMerchantByEnvironment(environment: String) {
            when (environment) {
                Environments.DEVELOPMENT.toString() -> {
                    id_merchant.setText(dataMerchantDev.merchant_id)
                    gateway_token.setText(dataMerchantDev.gateway_token)
                    base_url.setText(
                        getBaseUrlByEnvironment(
                            Environments.DEVELOPMENT.toString()
                        )
                    )
                }
                Environments.PLAYGROUND.toString() -> {
                    id_merchant.setText(dataMerchantPlayground.merchant_id)
                    gateway_token.setText(dataMerchantPlayground.gateway_token)
                    base_url.setText(
                        getBaseUrlByEnvironment(
                            Environments.PLAYGROUND.toString()
                        )
                    )
                }
                else -> {
                    id_merchant.setText(dataMerchantPlayground.merchant_id)
                    gateway_token.setText(dataMerchantPlayground.gateway_token)
                    base_url.setText(
                        getBaseUrlByEnvironment(
                            Environments.PLAYGROUND.toString()
                        )
                    )
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun setDefaultValue() {
            setDataMerchantByEnvironment(Environments.PLAYGROUND.toString())
            binding.checkPlayground.isChecked = true

            merchant_transaction_id.setText(getRandomString(10))
            user_email.setText(USER_EMAIL)
            user_document.setText(USER_DOCUMENT_NUMBER)
            account_id.setText("123654asd")
            amount.setText("500")
            binding.checkGroupCurrency.check(R.id.checkBRL)
            callback_url.setText(CALLBACK_URL)
            binding.checkGroupOperation.check(R.id.checkTypeDeposit)
            setAllOptionsDeposit(View.VISIBLE)
            setCheckedAllTypes(false)
            checkPix.isChecked = true
            type = 1
            typesChecked = TypesChecked(0, 0, 0, 1)

            binding.editRedirectUrl.setText(REDIRECT_URL)

            binding.editLogoUrl.setText(LOGO_URL)

            binding.checkGroupLanguage.clearCheck()
            binding.checkAutoApprove1.isChecked = true

            binding.checkIgnoreLogoUrl.isChecked = false
            binding.checkIgnoreEmail.isChecked = false
            binding.checkIgnoreDocument.isChecked = false
            binding.checkIgnoreRedirectUrl.isChecked = false

            setIsShowPixFields(false)
        }

        setDefaultValue()


        fun enableOptionsWithdraw() {
            setCheckedAllTypes(false)
            setAllOptionsDeposit(View.INVISIBLE)
            checkPix.visibility = View.VISIBLE
            checkPix.isChecked = true
            type = 1
            typesChecked = TypesChecked(0, 0, 0, 1)
            binding.editPixKeyValue.setText(binding.editUserDocument.text.toString())
        }

        fun validateAmount(amountValue: String): Boolean {
            println("amountValue.toIntOrNull() != null " + amountValue.toIntOrNull() != null)
            return amountValue.toIntOrNull() != null
        }

        data class ResponseValidateDataForm(
            val isValid: Boolean,
            val messageError: String,
        )

        fun isNumber(value: String?): Boolean {
            return try {
                value?.toInt()
                true
            } catch (ex: NumberFormatException) {
                false
            }
        }


        fun validateDataForm(
            amountValue: String,
            merchantId: String,
            types: TypesChecked,
        ): ResponseValidateDataForm {
            val isValidAmount = isNumber(amountValue)
            val isValidMerchantId = isNumber(merchantId)
            val isValidType = getNumberByTypesChecked(types) != 0
            val msgErrorInvalidAmount =
                if (isValidAmount) "" else "Invalid Amount"
            val msgErrorInvalidType =
                if (isValidType) "" else "\nChoose at least one type"
            val msgErrorInvalidMerchantId =
                if (isValidMerchantId) "" else "\nInvalid Merchant Id"

            val isValidDataForm =
                isValidAmount && isValidType && isValidMerchantId
            val messageError =
                "$msgErrorInvalidAmount$msgErrorInvalidType$msgErrorInvalidMerchantId"

            return ResponseValidateDataForm(isValidDataForm, messageError)
        }


        fun getLanguageChecked(): String? {
            return when {
                binding.checkLangPt.isChecked -> Languages.PT.toString()
                    .lowercase()
                binding.checkLangEn.isChecked -> Languages.EN.toString()
                    .lowercase()
                binding.checkLangEs.isChecked -> Languages.ES.toString()
                    .lowercase()
                else -> null
            }
        }

        fun getPixKeyTypeChecked(): Int? {
            if (!checkAllEnablesRadiosInRadioGroup(binding.checksPixKeyType)) {
                return null
            }
            return when {
                binding.checksPixKeyTypeDocument.isChecked -> TypePixKey.DOCUMENT.code
                binding.checksPixKeyTypePhone.isChecked -> TypePixKey.PHONE.code
                binding.checksPixKeyTypeEmail.isChecked -> TypePixKey.EMAIL.code
                else -> null
            }
        }

        fun getAutoApproveChecked(): Int {
            return when {
                binding.checkAutoApprove1.isChecked -> 1
                binding.checkAutoApprove0.isChecked -> 0
                else -> 1
            }
        }

        fun getCurrencyChecked(): String {
            return when {
                binding.checkBRL.isChecked -> Currency.BRL.toString()
                binding.checkUSD.isChecked -> Currency.USD.toString()
                else -> Currency.BRL.toString()
            }
        }

        fun getInputTextValueOrNull(textInputEditText: TextInputEditText): String? {
            return if (!textInputEditText.isEnabled || textInputEditText.text == null) {
                null
            } else textInputEditText.text.toString()
        }

        fun startIntentPreview(
            typeStartCheckout: Int,
            dataStartCheckoutByParams: DataStartCheckoutByParams? = null,
            url: String? = "",
            logoUrl: String? = null,
            language: String? = null,
        ) {
            val intent = Intent(this, PreviewDataActivity::class.java).apply {
                putExtra("type_start_checkout", typeStartCheckout)
                putExtra("language", language)

                if (!logoUrl.isNullOrEmpty()) {
                    putExtra("use_logo_url", 1)
                }

                if (typeStartCheckout == TypesStartCheckout.BY_PARAMS.code) {
                    val dataStartCheckoutByParamsString = Gson().toJson(dataStartCheckoutByParams)
                    putExtra(
                        "data_start_checkout_by_params",
                        dataStartCheckoutByParamsString
                    )
                } else {
                    putExtra("url", url)
                }

            }

            //generate new merchant_transaction_id random
            merchant_transaction_id.setText(getRandomString(10))


            startActivity(intent)
        }


        fun startCheckout() {
            val isValidDataForm = validateDataForm(
                amount.text.toString(),
                id_merchant.text.toString(),
                typesChecked,
            )
            if (!isValidDataForm.isValid) {
                Toast.makeText(
                    applicationContext,
                    isValidDataForm.messageError,
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

            val dataStartCheckoutByParams = DataStartCheckoutByParams(
                merchant_id = id_merchant.text.toString().toInt(),
                gateway_token = gateway_token.text.toString(),
                operation = operation,
                merchant_transaction_id = merchant_transaction_id.text.toString(),
                amount = amount.text.toString().toInt(),
                currency = getCurrencyChecked(),
                type = type,
                account_id = account_id.text.toString(),
                callback_url = callback_url.text.toString(),
                base_url = base_url.text.toString(),
                email_address = getInputTextValueOrNull(user_email),//Optional,
                document = getInputTextValueOrNull(user_document), //Optional
                auto_approve = getAutoApproveChecked(),
                logo_url = getInputTextValueOrNull(binding.editLogoUrl), //Optional
                pix_key_type = getPixKeyTypeChecked(), //Optional in Withdraw
                pix_key = getInputTextValueOrNull(binding.editPixKeyValue) //Optional in Withdraw
            )

            startIntentPreview(
                TypesStartCheckout.BY_PARAMS.code,
                dataStartCheckoutByParams = dataStartCheckoutByParams,
                language = getLanguageChecked(),
                logoUrl = getInputTextValueOrNull(binding.editLogoUrl), //Optional
            )
        }


        suspend fun startCheckoutByURL() {
            val isValidDataForm = validateDataForm(
                amount.text.toString(),
                id_merchant.text.toString(),
                typesChecked,
            )
            if (!isValidDataForm.isValid) {
                Toast.makeText(
                    applicationContext,
                    isValidDataForm.messageError,
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

            val dataGenerateUrl = DataGenerateUrl(
                base_url = base_url.text.toString(),
                merchant_id = id_merchant.text.toString(),
                merchant_transaction_id = merchant_transaction_id.text.toString(),
                amount = amount.text.toString(),
                currency = getCurrencyChecked(),
                operation = operation.toString(),
                callback_url = callback_url.text.toString(),
                type = type.toString(),
                account_id = account_id.text.toString(),
                email = getInputTextValueOrNull(user_email),//Optional
                document_number = getInputTextValueOrNull(user_document),//Optional
                auto_approve = getAutoApproveChecked().toString(),
                redirect_url = getInputTextValueOrNull(binding.editRedirectUrl), //Optional
                logo_url = getInputTextValueOrNull(binding.editLogoUrl),//Optional
                gateway_token = gateway_token.text.toString(),
                pix_key_type = getPixKeyTypeChecked(),//Optional in Withdraw
                pix_key = getInputTextValueOrNull(binding.editPixKeyValue)//Optional in Withdraw
            )

//            println(dataGenerateUrl)


            val generateUrlToCheckout = GenerateUrlToCheckout(
                dataGenerateUrl,
                Argon2iHash(),
            )

            withContext(Dispatchers.Default) {
                val url = withContext(Dispatchers.Default) {
                    generateUrlToCheckout.getUrlWithSignature()
                }
                startIntentPreview(
                    TypesStartCheckout.BY_URL.code,
                    url = url,
                    language = getLanguageChecked()
                )
            }


        }


        fun onBluInputs() {
            binding.editMerchantId.clearFocus()
            binding.editMerchantTransactionId.clearFocus()
            binding.editGatewayToken.clearFocus()
            binding.editUserEmail.clearFocus()
            binding.editUserDocument.clearFocus()
            binding.editAccountId.clearFocus()
            binding.editAmount.clearFocus()
            binding.editCallbackUrl.clearFocus()
            binding.editRedirectUrl.clearFocus()
            binding.editLogoUrl.clearFocus()
            binding.editBaseUrl.clearFocus()
            binding.editPixKeyValue.clearFocus()
            binding.containerForm.hideKeyboard()

        }

        binding.containerForm.setOnClickListener {
            onBluInputs()
        }

        with(binding)
        {
            checkBRL.setOnClickListener {
                onBluInputs()
            }

            checkUSD.setOnClickListener {
                onBluInputs()
            }

            buttonNewMerchantTransactionId.setOnClickListener {
                merchant_transaction_id.setText(getRandomString(10))
            }

            checkTypeDeposit.setOnClickListener {
                onBluInputs()
                setAllOptionsDeposit(View.VISIBLE)
                setCheckedAllTypes(false)
                checkPix.isChecked = true
                type = 1
                typesChecked = TypesChecked(0, 0, 0, 1)
                operation = Operation.DEPOSIT.code
                setIsShowPixFields(false)
            }

            checkTypeWithdraw.setOnClickListener {
                onBluInputs()
                enableOptionsWithdraw()
                operation = Operation.WITHDRAW.code
                setValuesDefaultPixFields()
                setIsShowPixFields(true)
            }

//            buttonClearPixData.setOnClickListener {
//                binding.checksPixKeyType.clearCheck()
//                binding.editPixKeyValue.setText("")
//            }

            checksPixKeyTypeDocument.setOnClickListener {
                editPixKeyValue.setText(editUserDocument.text.toString())
            }

            checksPixKeyTypePhone.setOnClickListener {
                editPixKeyValue.setText("")
            }

            checksPixKeyTypeEmail.setOnClickListener {
                editPixKeyValue.setText(editUserEmail.text.toString())
            }

            buttonStartCheckout.setOnClickListener {
                onBluInputs()
                startCheckout()
            }

            buttonStartCheckoutByURl.setOnClickListener {
                onBluInputs()
                runBlocking {
                    startCheckoutByURL()
                }

            }

            //Types to Transaction
            checkPix.setOnCheckedChangeListener { _, isChecked ->
                onBluInputs()
                typesChecked.PIX = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkBillet.setOnCheckedChangeListener { _, isChecked ->
                onBluInputs()
                typesChecked.BILLET = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkWireTransfer.setOnCheckedChangeListener { _, isChecked ->
                onBluInputs()
                typesChecked.WIRETRANSFER = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkWallet.setOnCheckedChangeListener { _, isChecked ->
                onBluInputs()
                typesChecked.WALLET = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkPlayground.setOnClickListener {
                onBluInputs()
                setDataMerchantByEnvironment(Environments.PLAYGROUND.toString())
            }

//            checkProduction.setOnClickListener {
//                onBluInputs()
//                setDataMerchantByEnvironment(Environments.PRODUCTION.toString())
//            }

            checkDev.setOnClickListener {
                onBluInputs()
                setDataMerchantByEnvironment(Environments.DEVELOPMENT.toString())
            }

            checkIgnoreEmail.setOnCheckedChangeListener { _, isChecked ->
                editUserEmail.isEnabled = !isChecked
                editUserEmailLayout.isEnabled = !isChecked
            }

            checkIgnoreDocument.setOnCheckedChangeListener { _, isChecked ->
                editUserDocument.isEnabled = !isChecked
                editUserDocumentLayout.isEnabled = !isChecked
            }

            checkIgnoreRedirectUrl.setOnCheckedChangeListener { _, isChecked ->
                editRedirectUrl.isEnabled = !isChecked
                editRedirectUrlLayout.isEnabled = !isChecked
            }

            checkIgnoreLogoUrl.setOnCheckedChangeListener { _, isChecked ->
                editLogoUrl.isEnabled = !isChecked
                editLogoUrlLayout.isEnabled = !isChecked
            }

            checkIgnorePixKeyType.setOnCheckedChangeListener { _, isChecked ->
                setIsEnableAllRadiosInRadioGroup(checksPixKeyType, !isChecked)
            }

            checkIgnorePixKey.setOnCheckedChangeListener { _, isChecked ->
                editPixKeyValue.isEnabled = !isChecked
                layoutEditPixKeyValue.isEnabled = !isChecked
            }

            buttonReloadDataDefault.setOnClickListener {
                setDefaultValue()
            }
        }

    }

}
