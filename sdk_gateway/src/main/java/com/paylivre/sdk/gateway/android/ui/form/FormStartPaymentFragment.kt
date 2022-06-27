package com.paylivre.sdk.gateway.android.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FormStartPaymentBinding
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.paylivre.sdk.gateway.android.IOnBackPressed
import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.checkIsErrorApiToken
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.howToGenerateApiToken.HowToGenerateApiToken
import com.paylivre.sdk.gateway.android.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FormStartPaymentFragment : Fragment(), IOnBackPressed {

    private var _binding: FormStartPaymentBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()
    private val binding get() = _binding!!
    var enableOnchangeValidateFields = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FormStartPaymentBinding.inflate(inflater, container, false)

        val root: View = binding.root


        var pixKeyTypeSelected: Int = -1
        val editDocument: EditText = binding.editDocument
        val editEmail: EditText = binding.editEmail
        val editEmailWallet: EditText = binding.editEmailWallet
        val editApiToken: EditText = binding.editApiToken

        mainViewModel.buttonPixKeyTypeSelected.observe(viewLifecycleOwner) {
            pixKeyTypeSelected = it
        }

        mainViewModel.editDocument.observe(viewLifecycleOwner) {
            editDocument.setText(it)
        }

        mainViewModel.editEmail.observe(viewLifecycleOwner) {
            editEmail.setText(it)
        }

        mainViewModel.editEmailWallet.observe(viewLifecycleOwner) {
            editEmailWallet.setText(it)
        }

        mainViewModel.editApiToken.observe(viewLifecycleOwner) {
            editApiToken.setText(it)
        }

        mainViewModel.editPixKeyValue.observe(viewLifecycleOwner) {
            if (pixKeyTypeSelected == TypePixKey.EMAIL.code) {
                binding.editPixKeyEmail.setText(it)
            }

            if (pixKeyTypeSelected == TypePixKey.PHONE.code) {
                binding.editPixKeyCellPhone.setText(it)
            }
        }

        return root
    }

    private fun View.hideKeyboard() {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(applicationWindowToken, 0)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Screen_FormStartPayment")

        var documentPassedByParam = false
        val editDocument: EditText = binding.editDocument
        val editEmail: EditText = binding.editEmail
        val editEmailWallet: EditText = binding.editEmailWallet
        val editApiToken: EditText = binding.editApiToken
        var operationName = ""
        var operation: Int = -1
        var pixKeyType: Int = -1
        val errorEmail = binding.textViewErrorEmail
        val errorDocument = binding.textViewErrorDocument
        val errorEmailWallet = binding.textViewErrorEmailWallet
        val errorApiToken = binding.textViewErrorApiToken
        val errorTypeSelect = binding.textViewErrorTypeForm
        val errorTypePixKeyValue = binding.textViewErrorPixKeyValue
        var typeSelected = -1
        var type = -1
        var typeString = ""

        mainViewModel.type.observe(viewLifecycleOwner) {
            typeString = it.toString()
            type = it
        }

        fun setPixKeyValueDefault(typePixKey: Int) {
            when (typePixKey) {
                TypePixKey.EMAIL.code -> binding.editPixKeyEmail.setText(editEmail.text.toString())
                else -> binding.editPixKeyCellPhone.setText("")
            }
        }

        val slideDownAnimationAlert: Animation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_down_alert_withdraw_limit
        )

        val slideDownAnimationForm: Animation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_down_form
        )

        mainViewModel.operation.observe(viewLifecycleOwner) {
            operation = it
            if (it == Operation.WITHDRAW.code) {
                //Timer to show Alert about Withdraw Limit
                Handler().postDelayed({
                    binding.alertLimitWithdraw.visibility = View.VISIBLE
                    binding.containerForm.startAnimation(slideDownAnimationForm)
                    binding.alertLimitWithdraw.startAnimation(slideDownAnimationAlert)
                }, 250)

                if (type == TypesToSelect.PIX.code) {

                    binding.containerButtonsPixKeyTypeSelect.visibility = View.VISIBLE

                    //Set default pix key type - CPF/CNPJ
                    mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.DOCUMENT.code)
                    pixKeyType = TypePixKey.DOCUMENT.code
                } else {
                    binding.editPixKeyEmailLayout.visibility = View.GONE
                    binding.editPixKeyCellPhoneLayout.visibility = View.GONE
                    binding.containerButtonsPixKeyTypeSelect.visibility = View.GONE
                }
            } else {

                binding.editPixKeyEmailLayout.visibility = View.GONE
                binding.editPixKeyCellPhoneLayout.visibility = View.GONE
                binding.containerButtonsPixKeyTypeSelect.visibility = View.GONE
            }


            operationName =
                if (it == Operation.DEPOSIT.code) getString(R.string.deposit) else getString(R.string.withdraw)
        }


        //Mostra ou Esconde campo api token caso seja depostio do tipo WALLET
        mainViewModel.buttonTypeSelected.observe(viewLifecycleOwner) {
            if (it == Type.WALLET.code && operation == Operation.DEPOSIT.code) {
                binding.editApiTokenLayout.visibility = View.VISIBLE
                binding.editEmailWalletLayout.visibility = View.VISIBLE
                binding.instructionsDepositWallet.visibility = View.VISIBLE
                editEmailWallet.text = editEmail.text
                binding.textViewHotGenerateApiToken.visibility = View.VISIBLE
            } else {
                binding.editApiTokenLayout.visibility = View.GONE
                binding.editEmailWalletLayout.visibility = View.GONE
                binding.instructionsDepositWallet.visibility = View.GONE
                binding.textViewHotGenerateApiToken.visibility = View.GONE
                editEmailWallet.setText("")
            }
        }


        //Set Mask in EditText Document (CPF/CNPJ)
        editDocument.addTextChangedListener(MaskDocumentUtil.insert(editDocument, null))

        //Set Mask in EditText CellPhone
        binding.editPixKeyCellPhone.addTextChangedListener(
            MaskPhoneUtil.insert(binding.editPixKeyCellPhone)
        )

        mainViewModel.buttonTypeSelected.observe(viewLifecycleOwner) {
            typeSelected = it
            if (validateTypesNumber(it, operation).isValid) {
                errorTypeSelect.visibility = View.GONE
            }

            if (it != Type.WALLET.code) {
                errorEmailWallet.visibility = View.GONE
                errorApiToken.visibility = View.GONE
            }
        }



        mainViewModel.enableEditEmail.observe(viewLifecycleOwner) {
            editEmail.isEnabled = it
        }

        mainViewModel.enableEditDocument.observe(viewLifecycleOwner) {
            editDocument.isEnabled = it
            documentPassedByParam = !it
        }

        fun errorMessageFormatted(fieldErrorStatus: FieldErrorStatus): String {
            return try {
                val isErrorEmpty = fieldErrorStatus.isErrorEmpty
                val fieldLabelName =
                    getStringByKey(requireContext(), fieldErrorStatus.keyLabelField)
                val messageError =
                    getStringByKey(requireContext(), fieldErrorStatus.keyStringErrorDescription)
                if (isErrorEmpty) messageError else "$fieldLabelName $messageError"
            } catch (error: Exception) {
                ""
            }
        }

        fun handleValidatePixKeyValue(editedPixKey: String) {
            val isPixKeyValue = getFieldStatusPixKeyValue(pixKeyType, editedPixKey)
            if (operation == Operation.WITHDRAW.code &&
                typeSelected == Type.PIX.code &&
                !isPixKeyValue.isValidated &&
                pixKeyType != TypePixKey.DOCUMENT.code
            ) {
                errorTypePixKeyValue.text = errorMessageFormatted(isPixKeyValue)
                errorTypePixKeyValue.visibility = View.VISIBLE
            } else {
                errorTypePixKeyValue.visibility = View.GONE
            }
        }

        mainViewModel.buttonPixKeyTypeSelected.observe(viewLifecycleOwner) {
            setPixKeyValueDefault(it)
            pixKeyType = it
            //Reset error pixKeyType
            binding.textViewErrorPixKeyValue.visibility = View.GONE
            binding.textViewErrorPixKeyType.visibility = View.GONE

            if (enableOnchangeValidateFields) {
                if (it == TypePixKey.EMAIL.code) {
                    handleValidatePixKeyValue(binding.editPixKeyEmail.text.toString())
                }
                if (it == TypePixKey.PHONE.code) {
                    handleValidatePixKeyValue(binding.editPixKeyCellPhone.text.toString())
                }
                if (it == TypePixKey.DOCUMENT.code) {
                    binding.textViewErrorPixKeyValue.visibility = View.GONE
                }
            }


            //Enable input by PixKeyType
            if (operation == Operation.WITHDRAW.code) {
                when (it) {
                    TypePixKey.DOCUMENT.code -> {
                        //Hidden PixKey Input if TypePixKey == Document
                        binding.editPixKeyEmailLayout.visibility = View.GONE
                        binding.editPixKeyCellPhoneLayout.visibility = View.GONE
                    }
                    TypePixKey.PHONE.code -> {
                        binding.editPixKeyCellPhoneLayout.visibility = View.VISIBLE
                        binding.editPixKeyEmailLayout.visibility = View.GONE
                    }
                    TypePixKey.EMAIL.code -> {
                        binding.editPixKeyEmailLayout.visibility = View.VISIBLE
                        binding.editPixKeyCellPhoneLayout.visibility = View.GONE
                    }
                }

            }
        }



        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    if (enableOnchangeValidateFields) {
                        afterTextChanged.invoke(editable.toString())
                    }
                }
            })
        }


        fun handleValidateInputEmail(editedEmail: String) {
            val isEmailValidated = getFieldStatusEmail(editedEmail)
            if (!isEmailValidated.isValidated) {
                errorEmail.text = errorMessageFormatted(isEmailValidated)
                errorEmail.visibility = View.VISIBLE
            } else {
                errorEmail.visibility = View.GONE
            }
        }

        fun handleValidateInputDocument(editedDocument: String) {
            val isDocumentValidated = getFieldStatusDocument(editedDocument)
            if (!isDocumentValidated.isValidated) {
                errorDocument.text = errorMessageFormatted(isDocumentValidated)
                errorDocument.visibility = View.VISIBLE
            } else {
                errorDocument.visibility = View.GONE
            }
        }

        fun handleValidateInputApiToken(editedPassword: String) {
            val isApiTokenValid = getFieldStatusApiToken(editedPassword)
            if (!isApiTokenValid.isValidated &&
                typeSelected == Type.WALLET.code &&
                operation == Operation.DEPOSIT.code
            ) {
                errorApiToken.text = errorMessageFormatted(isApiTokenValid)
                errorApiToken.visibility = View.VISIBLE
            } else {
                errorApiToken.visibility = View.GONE
            }
        }

        fun handleValidateInputEmailWallet(editedEmail: String) {
            val isEmailWalletValidated = getFieldStatusEmail(editedEmail)
            if (!isEmailWalletValidated.isValidated &&
                typeSelected == Type.WALLET.code &&
                operation == Operation.DEPOSIT.code
            ) {
                errorEmailWallet.text = errorMessageFormatted(isEmailWalletValidated)
                errorEmailWallet.visibility = View.VISIBLE
            } else {
                errorEmailWallet.visibility = View.GONE
            }
        }

        editEmail.afterTextChanged { handleValidateInputEmail(it) }
        editDocument.afterTextChanged { handleValidateInputDocument(it) }
        editEmailWallet.afterTextChanged { handleValidateInputEmailWallet(it) }
        editApiToken.afterTextChanged { handleValidateInputApiToken(it) }
        binding.editPixKeyEmail.afterTextChanged { handleValidatePixKeyValue(it) }
        binding.editPixKeyCellPhone.afterTextChanged { handleValidatePixKeyValue(it) }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            if (it.isSuccess == false) {
                if (checkIsErrorApiToken(it?.error)) {
                    enableOnchangeValidateFields = true
                    errorApiToken.text = getString(R.string.error_invalid_api_token)
                    errorApiToken.visibility = View.VISIBLE
                    binding.textViewHotGenerateApiToken.visibility = View.VISIBLE
                }
            }
        }

        var base_url = ""
        var merchant_id = 0
        var gateway_token = ""
        var merchant_transaction_id = ""
        var amount = ""
        var currency = ""
        var callback_url = ""

        var selected_type = ""
        var account_id = ""
        var auto_approve = 0
        var dataStartCheckout: DataStartCheckout? = null
        var typeStartCheckout = -1
        var documentStartCheckout = ""

        mainViewModel.base_url.observe(viewLifecycleOwner, { base_url = it })
        mainViewModel.merchant_id.observe(viewLifecycleOwner, { merchant_id = it })
        mainViewModel.editGatewayToken.observe(viewLifecycleOwner, { gateway_token = it })
        mainViewModel.merchant_transaction_id.observe(
            viewLifecycleOwner
        ) { merchant_transaction_id = it }
        mainViewModel.amount.observe(viewLifecycleOwner, { amount = it.toString() })
        mainViewModel.currency.observe(viewLifecycleOwner, { currency = it })
        mainViewModel.operation.observe(viewLifecycleOwner, { operation = it })
        mainViewModel.callback_url.observe(viewLifecycleOwner, { callback_url = it })
        mainViewModel.account_id.observe(viewLifecycleOwner, { account_id = it })
        mainViewModel.autoApprove.observe(viewLifecycleOwner, { auto_approve = it })
        mainViewModel.buttonTypeSelected.observe(
            viewLifecycleOwner
        ) { selected_type = it.toString() }

        mainViewModel.dataStartCheckout.observe(viewLifecycleOwner) {
            dataStartCheckout = it
            documentStartCheckout = it.document ?: ""
        }

        mainViewModel.type_start_checkout.observe(viewLifecycleOwner) {
            typeStartCheckout = it
        }

        fun getPixKeyValue(): String {
            return if (operation == Operation.WITHDRAW.code) {
                when (pixKeyType) {
                    TypePixKey.DOCUMENT.code -> unmask(editDocument.text.toString())
                    TypePixKey.EMAIL.code -> binding.editPixKeyEmail.text.toString()
                    TypePixKey.PHONE.code -> binding.editPixKeyCellPhone.text.toString()
                    else -> ""
                }
            } else ""
        }


        fun handleSaveFormInViewModel() {
            mainViewModel.setEditEmail(editEmail.text.toString())
            mainViewModel.setEditDocument(editDocument.text.toString())
            mainViewModel.setEditEmailWallet(editEmailWallet.text.toString())
            mainViewModel.setEditApiToken(editApiToken.text.toString())

            mainViewModel.setEditPixKeyValue(getPixKeyValue())
        }


        fun handleStartPayment() {
            val data = DataGenerateSignature(
                base_url,
                merchant_id,
                gateway_token,
                merchant_transaction_id,
                amount,
                currency,
                operation.toString(),
                callback_url,
                typeString,
                selected_type,
                account_id,
                binding.editEmail.text.toString(),
                binding.editDocument.text.toString(),
                binding.editEmailWallet.text.toString(),
                binding.editApiToken.text.toString(),
                pixKeyType.toString(),
                getPixKeyValue(),
                auto_approve
            )
            handleSaveFormInViewModel()
            Navigation.findNavController(view).navigate(R.id.navigation_loading_transaction)

            mainViewModel.startPayment(data)
        }

        fun getDocument(): String {
            return if (documentPassedByParam) documentStartCheckout
            else binding.editDocument.text.toString()
        }

        fun handleStartPaymentByUrl() {
            val orderDataRequest = OrderDataRequest(
                base_url = base_url,
                merchant_id = merchant_id,
                merchant_transaction_id = merchant_transaction_id,
                amount = amount,
                currency = currency,
                operation = operation.toString(),
                callback_url = callback_url,
                type = typeString,
                selected_type = selected_type,
                account_id = account_id,
                email = binding.editEmail.text.toString(),
                document_number = getDocument(),
                login_email = binding.editEmailWallet.text.toString(),
                api_token = binding.editApiToken.text.toString(),
                pix_key_type = pixKeyType.toString(),
                pix_key = getPixKeyValue(),
                signature = dataStartCheckout?.signature.toString(),
                url = getUrlWithoutSignature(dataStartCheckout?.url.toString()),
                auto_approve = dataStartCheckout?.auto_approve.toString(),
                logo_url = dataStartCheckout?.logo_url.toString(),
                redirect_url = dataStartCheckout?.redirect_url.toString()
            )

            handleSaveFormInViewModel()
            Navigation.findNavController(view).navigate(R.id.navigation_loading_transaction)

            mainViewModel.startPaymentByURL(orderDataRequest)
        }


        fun handleValidateForm() {
            val pixKey = getPixKeyValue()
            val formValidatedData = validateForm(
                ValidateForm(
                    editEmail.text.toString(),
                    editDocument.text.toString(),
                    typeSelected,
                    editEmailWallet.text.toString(),
                    editApiToken.text.toString(),
                    pixKeyType,
                    pixKey
                ),
                operation,
                typeSelected
            )
            val isFormValidated = formValidatedData.statusFormDataValidated

            if (typeSelected == -1) {
                binding.textViewErrorTypeForm.text =
                    getString(R.string.type_required_error, operationName)
                errorTypeSelect.visibility = View.VISIBLE
            } else {
                errorTypeSelect.visibility = View.GONE
            }

            if (operation == Operation.WITHDRAW.code) {
                if (pixKeyType == -1) {
                    binding.textViewErrorPixKeyType.visibility = View.VISIBLE
                } else {
                    binding.textViewErrorPixKeyType.visibility = View.GONE
                    handleValidatePixKeyValue(pixKey)
                }

            }

            handleValidateInputEmail(editEmail.text.toString())
            handleValidateInputDocument(editDocument.text.toString())
            handleValidateInputEmailWallet(editEmailWallet.text.toString())
            handleValidateInputApiToken(editApiToken.text.toString())

            if (isFormValidated) {
                enableOnchangeValidateFields = false
                if (typeStartCheckout == TypesStartCheckout.BY_PARAMS.code) {
                    handleStartPayment()
                } else {
                    handleStartPaymentByUrl()
                }
                //Set Log Analytics
                logEventsService.setLogEventAnalyticsWithParams(
                    "Validation_FormStartPayment",
                    Pair("validation_status", "Ok"),
                )

            } else {
                enableOnchangeValidateFields = true

                //Set Log Analytics
                logEventsService.setLogEventAnalyticsWithParams(
                    "Validation_FormStartPayment",
                    Pair("validation_status", "Error"),
                )
            }
        }

        fun onBlurAllInputs() {
            editEmail.clearFocus()
            editDocument.clearFocus()
            editEmailWallet.clearFocus()
            editApiToken.clearFocus()
            binding.editPixKeyEmail.clearFocus()
            binding.editPixKeyCellPhone.clearFocus()
        }


        mainViewModel.isCloseKeyboard.observe(viewLifecycleOwner) {
            if (it) {
                onBlurAllInputs()
                binding.containerFormScrollView.hideKeyboard()
            }
        }

        binding.headerTitle.setOnClickListener {
            onBlurAllInputs()
            it.hideKeyboard()
        }

        binding.containerForm.setOnClickListener {
            onBlurAllInputs()
            it.hideKeyboard()
        }

        fun isShowLanguageDialog(isShow: Boolean) {
            val dialog = HowToGenerateApiToken()
            if (isShow) {
                dialog.show(childFragmentManager, dialog.tag)
            }
        }

        binding.textViewHotGenerateApiToken.setOnClickListener {
            isShowLanguageDialog(true)
            mainViewModel.setEditEmailWallet(editEmailWallet.text.toString())
        }

        binding.btnStartPayment.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_FormStartPayment")

            handleValidateForm()
            onBlurAllInputs()
            it.hideKeyboard()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}