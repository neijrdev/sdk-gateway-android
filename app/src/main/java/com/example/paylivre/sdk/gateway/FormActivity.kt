package com.example.paylivre.sdk.gateway

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paylivre.sdk.gateway.data.*
import com.example.paylivre.sdk.gateway.databinding.ActivityFormBinding
import com.example.paylivre.sdk.gateway.model.DataGenerateUrl
import com.example.paylivre.sdk.gateway.model.GenerateUrlToCheckout
import com.example.paylivre.sdk.gateway.utils.*
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.domain.model.TypePixKey
import com.paylivre.sdk.gateway.android.domain.model.TypesToSelect
import com.paylivre.sdk.gateway.android.services.argon2i.Argon2iHash
import com.paylivre.sdk.gateway.android.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.example.paylivre.sdk.gateway.utils.setTextThemeStatusBar


class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTextThemeStatusBar(this, "Light")
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Form Generate Checkout - v${BuildConfig.VERSION_NAME}"
        }

        val idMerchant = binding.editMerchantId
        val merchantTransactionId = binding.editMerchantTransactionId
        val gatewayToken = binding.editGatewayToken
        val userEmail = binding.editUserEmail
        val accountId = binding.editAccountId
        val userDocument = binding.editUserDocument
        val amount = binding.editAmount
        val checkPix = binding.checkPix
        val checkBillet = binding.checkBillet
        val checkWireTransfer = binding.checkWireTransfer
        val checkWallet = binding.checkWallet
        var operation = Operation.DEPOSIT.code
        val callbackUrl = binding.editCallbackUrl
        val baseUrl = binding.editBaseUrl
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
            binding.layoutEditPixKeyValue.visibility = getVisibility(isShow)
            binding.checkIgnorePixKeyType.visibility = getVisibility(isShow)
            binding.checkIgnorePixKey.visibility = getVisibility(isShow)


            binding.layoutEditPixKeyValue.isEnabled = isShow
            binding.editPixKeyValue.isEnabled = isShow
            binding.checksPixKeyType.isEnabled = isShow
            setIsEnableAllRadiosInRadioGroup(binding.checksPixKeyType, isShow)
            binding.checkIgnorePixKey.isChecked = !isShow
            binding.checkIgnorePixKeyType.isChecked = !isShow
        }

        fun setValuesDefaultPixFields() {
            binding.checkTypeWithdrawPix.isChecked = true
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
                    idMerchant.setText(dataMerchantDev.merchant_id)
                    gatewayToken.setText(dataMerchantDev.gateway_token)
                    baseUrl.setText(
                        getBaseUrlByEnvironment(
                            Environments.DEVELOPMENT.toString()
                        )
                    )
                }
                Environments.PLAYGROUND.toString() -> {
                    idMerchant.setText(dataMerchantPlayground.merchant_id)
                    gatewayToken.setText(dataMerchantPlayground.gateway_token)
                    baseUrl.setText(
                        getBaseUrlByEnvironment(
                            Environments.PLAYGROUND.toString()
                        )
                    )
                }
                else -> {
                    idMerchant.setText(dataMerchantPlayground.merchant_id)
                    gatewayToken.setText(dataMerchantPlayground.gateway_token)
                    baseUrl.setText(
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

            merchantTransactionId.setText(getRandomString(10))
            userEmail.setText(USER_EMAIL)
            userDocument.setText(USER_DOCUMENT_NUMBER)
            accountId.setText("123654asd")
            amount.setText("500")
            binding.checkGroupCurrency.check(R.id.checkBRL)
            callbackUrl.setText(CALLBACK_URL)
            operation = Operation.DEPOSIT.code
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
            binding.typesOperationContainer.visibility = View.VISIBLE
            binding.containerCheckBoxTypeWithdraw.visibility = View.GONE

            setIsShowPixFields(false)
        }

        setDefaultValue()

        fun validateAmount(amountValue: String): Boolean {
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

        fun getType(): Int {
            return if (operation == Operation.WITHDRAW.code) {
                when {
                    binding.checkTypeWithdrawPix.isChecked -> TypesToSelect.PIX.code
                    binding.checkTypeWithdrawWallet.isChecked -> TypesToSelect.WALLET.code
                    else -> -1
                }
            } else {
                type
            }
        }


        fun validateDataForm(
            amountValue: String,
            merchantId: String,
            types: TypesChecked,
        ): ResponseValidateDataForm {
            val isValidAmount = isNumber(amountValue)
            val isValidMerchantId = isNumber(merchantId)
            val isValidType =
                if (operation != Operation.WITHDRAW.code) getNumberByTypesChecked(types) != 0 else true
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
            if (operation == Operation.WITHDRAW.code && getType() == TypesToSelect.WALLET.code) {
                return null
            }
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

            startActivity(intent)
        }


        fun startCheckout() {
            val isValidDataForm = validateDataForm(
                amount.text.toString(),
                idMerchant.text.toString(),
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
                merchant_id = idMerchant.text.toString().toInt(),
                gateway_token = gatewayToken.text.toString(),
                operation = operation,
                merchant_transaction_id = merchantTransactionId.text.toString(),
                amount = amount.text.toString().toInt(),
                currency = getCurrencyChecked(),
                type = getType(),
                account_id = accountId.text.toString(),
                callback_url = callbackUrl.text.toString(),
                base_url = baseUrl.text.toString(),
                email_address = getInputTextValueOrNull(userEmail),//Optional,
                document = getInputTextValueOrNull(userDocument), //Optional
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

            //generate new merchant_transaction_id random
            merchantTransactionId.setText(getRandomString(10))
        }

        suspend fun startCheckoutByURL() {
            val isValidDataForm = validateDataForm(
                amount.text.toString(),
                idMerchant.text.toString(),
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
                base_url = baseUrl.text.toString(),
                merchant_id = idMerchant.text.toString(),
                merchant_transaction_id = merchantTransactionId.text.toString(),
                amount = amount.text.toString(),
                currency = getCurrencyChecked(),
                operation = operation.toString(),
                callback_url = callbackUrl.text.toString(),
                type = getType().toString(),
                account_id = accountId.text.toString(),
                email = getInputTextValueOrNull(userEmail),//Optional
                document_number = getInputTextValueOrNull(userDocument),//Optional
                auto_approve = getAutoApproveChecked().toString(),
                redirect_url = getInputTextValueOrNull(binding.editRedirectUrl), //Optional
                logo_url = getInputTextValueOrNull(binding.editLogoUrl),//Optional
                gateway_token = gatewayToken.text.toString(),
                pix_key_type = getPixKeyTypeChecked(),//Optional in Withdraw
                pix_key = getInputTextValueOrNull(binding.editPixKeyValue)//Optional in Withdraw
            )

            val generateUrlToCheckout = GenerateUrlToCheckout(
                dataGenerateUrl,
                Argon2iHash(),
                saltRandomString = HASH_ARGON2I
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

            //generate new merchant_transaction_id random
            merchantTransactionId.setText(getRandomString(10))
        }


        fun onBlurInputs() {
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
            onBlurInputs()
        }

        with(binding)
        {
            checkBRL.setOnClickListener {
                onBlurInputs()
            }

            checkUSD.setOnClickListener {
                onBlurInputs()
            }

            buttonNewMerchantTransactionId.setOnClickListener {
                merchantTransactionId.setText(getRandomString(10))
            }

            checkTypeDeposit.setOnClickListener {
                onBlurInputs()
                setAllOptionsDeposit(View.VISIBLE)
                setCheckedAllTypes(false)
                checkPix.isChecked = true
                type = 1
                typesChecked = TypesChecked(0, 0, 0, 1)
                operation = Operation.DEPOSIT.code
                setIsShowPixFields(false)
                binding.containerCheckBoxTypeWithdraw.visibility = View.GONE
                binding.typesOperationContainer.visibility = View.VISIBLE
            }

            checkTypeWithdraw.setOnClickListener {
                onBlurInputs()
                operation = Operation.WITHDRAW.code

                binding.containerCheckBoxTypeWithdraw.visibility = View.VISIBLE
                binding.typesOperationContainer.visibility = View.GONE

                setValuesDefaultPixFields()
                setIsShowPixFields(true)
            }

            checkTypeWithdrawPix.setOnClickListener {
                setIsShowPixFields(true)
            }

            checkTypeWithdrawWallet.setOnClickListener {
                setIsShowPixFields(false)
            }

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
                onBlurInputs()
                startCheckout()
            }

            buttonStartCheckoutByURl.setOnClickListener {
                onBlurInputs()
                runBlocking {
                    startCheckoutByURL()
                }
            }

            checkBillet.setOnCheckedChangeListener { _, isChecked ->
                onBlurInputs()
                typesChecked.BILLET = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkWireTransfer.setOnCheckedChangeListener { _, isChecked ->
                onBlurInputs()
                typesChecked.WIRETRANSFER = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            //Types to Transaction
            checkPix.setOnCheckedChangeListener { _, isChecked ->
                onBlurInputs()
                typesChecked.PIX = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkWallet.setOnCheckedChangeListener { _, isChecked ->
                onBlurInputs()
                typesChecked.WALLET = if (isChecked) 1 else 0
                type = getNumberByTypesChecked(typesChecked)
            }

            checkPlayground.setOnClickListener {
                onBlurInputs()
                setDataMerchantByEnvironment(Environments.PLAYGROUND.toString())
            }

//            checkProduction.setOnClickListener {
//                onBluInputs()
//                setDataMerchantByEnvironment(Environments.PRODUCTION.toString())
//            }

            checkDev.setOnClickListener {
                onBlurInputs()
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
