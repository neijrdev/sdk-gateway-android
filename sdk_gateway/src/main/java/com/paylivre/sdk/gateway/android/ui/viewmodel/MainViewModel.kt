package com.paylivre.sdk.gateway.android.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paylivre.sdk.gateway.android.data.PaymentRepository
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.data.model.servicesStatus.ServiceStatusResponseAdapter
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.utils.getNumberByTypes
import kotlinx.coroutines.*

class MainViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {
    private val _clearAllFocus = MutableLiveData<Boolean>()
    val clearAllFocus: MutableLiveData<Boolean> get() = _clearAllFocus

    private val _dataStartCheckout = MutableLiveData<DataStartCheckout>()
    val dataStartCheckout: MutableLiveData<DataStartCheckout> get() = _dataStartCheckout

    private val _type_start_checkout = MutableLiveData<Int>()
    val type_start_checkout: MutableLiveData<Int> get() = _type_start_checkout

    private val _order_data = MutableLiveData<OrderDataRequest?>()
    val order_data: MutableLiveData<OrderDataRequest?> get() = _order_data

    private val _data_start_payment = MutableLiveData<DataGenerateSignature?>()
    val data_start_payment: MutableLiveData<DataGenerateSignature?> get() = _data_start_payment

    private val _checkStatusDepositLoading = MutableLiveData<Boolean?>()
    val checkStatusDepositLoading: MutableLiveData<Boolean?> get() = _checkStatusDepositLoading


    private val _checkStatusTransactionLoading = MutableLiveData<Boolean?>()
    val checkStatusTransactionLoading: MutableLiveData<Boolean?> get() = _checkStatusTransactionLoading


    private val _checkStatusDepositResponse = MutableLiveData<CheckStatusDepositResponse>()
    val checkStatusDepositResponse: LiveData<CheckStatusDepositResponse> get() = _checkStatusDepositResponse

    private val _checkStatusOrderDataResponse = MutableLiveData<CheckStatusOrderResponse>()
    val checkStatusOrderDataResponse: MutableLiveData<CheckStatusOrderResponse> get() = _checkStatusOrderDataResponse

    private val _checkStatusTransactionResponse = MutableLiveData<CheckStatusTransactionResponse>()
    val checkStatusTransactionResponse: MutableLiveData<CheckStatusTransactionResponse> get() = _checkStatusTransactionResponse

    private val _statusResponseTransaction = MutableLiveData<StatusTransactionResponse>()
    val statusResponseTransaction: LiveData<StatusTransactionResponse> get() = _statusResponseTransaction

    private val _statusResponseCheckServices = MutableLiveData<CheckEnablerServices?>()
    val statusResponseCheckServices: LiveData<CheckEnablerServices?> get() = _statusResponseCheckServices

    private val _statusWithdrawOrder = MutableLiveData<StatusWithdrawOrder>()
    val statusWithdrawOrder: LiveData<StatusWithdrawOrder> get() = _statusWithdrawOrder

    private val _base_url = MutableLiveData<String>()
    val base_url: LiveData<String> get() = _base_url

    private val _isCloseKeyboard = MutableLiveData<Boolean>()
    val isCloseKeyboard: LiveData<Boolean> get() = _isCloseKeyboard

    private val _language = MutableLiveData<String>()
    val language: LiveData<String> get() = _language

    private val _gatewayToken = MutableLiveData<String>()
    val editGatewayToken: LiveData<String> get() = _gatewayToken

    private val _editEmail = MutableLiveData<String>()
    val editEmail: LiveData<String> get() = _editEmail

    private val _enableEditEmail = MutableLiveData(false)
    val enableEditEmail: LiveData<Boolean> get() = _enableEditEmail

    private val _editDocument = MutableLiveData<String>()
    val editDocument: LiveData<String> get() = _editDocument

    private val _enableEditDocument = MutableLiveData(false)
    val enableEditDocument: LiveData<Boolean> get() = _enableEditDocument

    private val _amount = MutableLiveData<Int>()
    val amount: LiveData<Int> get() = _amount

    private val _auto_approve = MutableLiveData<Int>()
    val autoApprove: LiveData<Int> get() = _auto_approve

    private val _transactionError = MutableLiveData<ErrorTransaction?>()
    val transactionError: LiveData<ErrorTransaction?> get() = _transactionError

    private val _isFatalError = MutableLiveData<Boolean>()
    val isFatalError: LiveData<Boolean> get() = _isFatalError

    private val _keyMsgFatalError = MutableLiveData<String>()
    val keyMsgFatalError: LiveData<String> get() = _keyMsgFatalError

    private val _msgDetailsError = MutableLiveData<String>()
    val msgDetailsError: LiveData<String> get() = _msgDetailsError

    private val _errorTags = MutableLiveData<String>()
    val errorTags: LiveData<String> get() = _errorTags

    private val _isCloseSDK = MutableLiveData<Boolean>()
    val isCloseSDK: LiveData<Boolean> get() = _isCloseSDK

    private val _operation = MutableLiveData<Int>()
    val operation: LiveData<Int> get() = _operation

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String> get() = _currency

    private val _logoResId = MutableLiveData<Int>()
    val logoResId: LiveData<Int> get() = _logoResId

    private val _logoUrl = MutableLiveData<String>()
    val logoUrl: LiveData<String> get() = _logoUrl

    private val _merchant_transaction_id = MutableLiveData<String>()
    val merchant_transaction_id: LiveData<String> get() = _merchant_transaction_id

    private val _callback_url = MutableLiveData<String>()
    val callback_url: LiveData<String> get() = _callback_url

    private val _selectedBankAccountWireTransfer = MutableLiveData<BankAccount?>()
    val selectedBankAccountWireTransfer: LiveData<BankAccount?> get() = _selectedBankAccountWireTransfer

    private val _origin_type_insert_proof = MutableLiveData<OriginTypeInsertProof?>()
    val origin_type_insert_proof: LiveData<OriginTypeInsertProof?> get() = _origin_type_insert_proof

    private val _transfer_proof_response = MutableLiveData<InsertTransferProofDataResponse?>()
    val transfer_proof_response: LiveData<InsertTransferProofDataResponse?> get() = _transfer_proof_response

    private val _proof_image_uri = MutableLiveData<Uri?>()
    val proof_image_uri: LiveData<Uri?> get() = _proof_image_uri

    private val _merchant_id = MutableLiveData<Int>()
    val merchant_id: LiveData<Int> get() = _merchant_id

    private val _editEmailWallet = MutableLiveData<String>()
    val editEmailWallet: LiveData<String> get() = _editEmailWallet

    private val _account_id = MutableLiveData<String>()
    val account_id: LiveData<String> get() = _account_id

    private val _editApiToken = MutableLiveData<String>()
    val editApiToken: LiveData<String> get() = _editApiToken

    private val _pixApprovalTime = MutableLiveData<PixApprovalTimeResponse>()
    val pixApprovalTime: LiveData<PixApprovalTimeResponse> get() = _pixApprovalTime

    private val _getServicesStatusLoading = MutableLiveData<Boolean>()
    val getServicesStatusLoading: LiveData<Boolean> get() = _getServicesStatusLoading

    private val _getServicesStatusSuccess = MutableLiveData<ServicesStatusSuccess>()
    val getServicesStatusSuccess: LiveData<ServicesStatusSuccess> get() = _getServicesStatusSuccess

    private val _type = MutableLiveData<Int>()
    val type: LiveData<Int> get() = _type

    private val _type_status_services = MutableLiveData<Int>()
    val type_status_services: LiveData<Int> get() = _type_status_services

    private val _editPixKeyValue = MutableLiveData("")
    val editPixKeyValue: LiveData<String> get() = _editPixKeyValue

    private val _buttonPixKeyTypeSelected = MutableLiveData<Int>()
    val buttonPixKeyTypeSelected: LiveData<Int> get() = _buttonPixKeyTypeSelected

    private val _buttonTypeSelected = MutableLiveData<Int>()
    val buttonTypeSelected: LiveData<Int> get() = _buttonTypeSelected

    fun setIsCloseKeyboard(isClose: Boolean) {
        _isCloseKeyboard.value = isClose
    }

    fun setEnabledEditEmail(isEnable: Boolean) {
        _enableEditEmail.value = isEnable
    }

    fun setClearAllFocus(value: Boolean) {
        _clearAllFocus.value = value
    }

    fun setEnabledEditDocument(isEnable: Boolean) {
        _enableEditDocument.value = isEnable
    }

    fun setType(typeValue: Int) {
        _type.value = typeValue
    }

    fun setTypeStatusServices(typeValue: Int) {
        _type_status_services.value = typeValue
    }

    fun setGetServicesStatusSuccess(data: ServicesStatusSuccess?) {
        if (data == null) {
            _getServicesStatusSuccess.value = ServicesStatusSuccess()
        } else {
            _getServicesStatusSuccess.value = data!!
        }

    }

    fun setLogoResId(value: Int) {
        _logoResId.value = value
    }

    fun setLogoUrl(url: String) {
        _logoUrl.value = url
    }


    fun setEditApiToken(value: String) {
        _editApiToken.value = value
    }

    fun setEditEmailWallet(value: String) {
        _editEmailWallet.value = value
    }

    fun setAccountId(value: String) {
        _account_id.value = value
    }


    fun setMerchantTransactionId(value: String) {
        _merchant_transaction_id.value = value
    }

    fun setCallbackUrl(value: String) {
        _callback_url.value = value
    }

    fun setMerchantId(value: Int) {
        _merchant_id.value = value
    }

    fun setCurrency(currencyValue: String) {
        _currency.value = currencyValue
    }

    fun setIsCloseSDK(isClose: Boolean) {
        _isCloseSDK.value = isClose
    }


    fun setButtonTypeSelected(buttonTypeName: Int) {
        _buttonTypeSelected.value = buttonTypeName
    }

    fun setButtonPixKeyTypeSelected(buttonTypeKeyCode: Int) {
        _buttonPixKeyTypeSelected.value = buttonTypeKeyCode
    }

    fun setEditPixKeyValue(value: String) {
        _editPixKeyValue.value = value
    }

    fun setIsFatalError(isError: Boolean, messageError: String) {
        _isFatalError.value = isError
        _keyMsgFatalError.value = messageError
    }

    fun setMessageDetailsError(messageError: String) {
        _msgDetailsError.value = messageError
    }

    fun setErrorTags(errorTags: String) {
        _errorTags.value = errorTags
    }

    fun setOperation(operation: Int) {
        if (validateOperation(operation).isValid) {
            _operation.value = operation
        }
    }

    fun setBaseUrl(value: String) {
        _base_url.value = value
    }

    fun setTypeStartCheckout(value: Int) {
        _type_start_checkout.value = value
    }


    fun setDataRequestOrder(orderDataRequest: OrderDataRequest?) {
        _order_data.value = orderDataRequest
    }

    fun setDataStartPayment(dataGenerateSignature: DataGenerateSignature?) {
        _data_start_payment.value = dataGenerateSignature
    }

    fun setAmount(value: Int) {
        _amount.value = value
    }

    fun setAutoApprove(value: Int) {
        _auto_approve.value = value
    }

    fun setGatewayToken(value: String) {
        _gatewayToken.value = value
    }

    fun setEditEmail(value: String) {
        _editEmail.value = value
    }

    fun setEditDocument(value: String) {
        _editDocument.value = value
    }

    fun setLanguage(value: String) {
        _language.value = value
    }

    fun setPixApprovalTime(pixApprovalTimeResponse: PixApprovalTimeResponse?) {
        if (pixApprovalTimeResponse == null) {
            _pixApprovalTime.value = PixApprovalTimeResponse(
                "", 0,
                "", null
            )
        } else {
            _pixApprovalTime.value = pixApprovalTimeResponse!!
        }

    }

    fun getPixApprovalTimeSuccess(response: PixApprovalTimeResponse?) {
        setPixApprovalTime(response)
    }

    private fun getPixApprovalTimeFailure(error: Throwable) {
        _pixApprovalTime.value = PixApprovalTimeResponse(
            "error", 0,
            "", null
        )
    }

    fun getPixApprovalTime() {
        paymentRepository.getPixApprovalTime(
            ::getPixApprovalTimeSuccess,
            ::getPixApprovalTimeFailure
        )
    }

    fun setServicesStatusLoading(isLoading: Boolean) {
        _getServicesStatusLoading.value = isLoading
    }


    data class ServicesStatusSuccess(
        val isSuccess: Boolean? = null,
        val typeStatusServices: Int = 0,
    )

    private fun getServicesStatusSuccess(response: ServiceStatusResponseAdapter) {
        val typeStatusServices = getNumberByTypes(response.data)
        setTypeStatusServices(typeStatusServices)
        setServicesStatusLoading(false)
        setGetServicesStatusSuccess(ServicesStatusSuccess(true, typeStatusServices))
    }

    private fun getServicesStatusFailure(error: Throwable) {
        setServicesStatusLoading(false)
        setGetServicesStatusSuccess(ServicesStatusSuccess(false))
    }

    fun getServicesStatus() {
        setServicesStatusLoading(true)
        paymentRepository.getServiceStatus(
            ::getServicesStatusSuccess,
            ::getServicesStatusFailure
        )
    }

    fun setStatusTransactionResponse(data: StatusTransactionResponse?) {
        if (data == null) {
            _statusResponseTransaction.value =
                StatusTransactionResponse(null, null, null, null)
        } else {
            _statusResponseTransaction.value = data!!
        }
    }


    private fun transactionSuccess(response: ResponseCommonTransactionData) {
        _statusResponseTransaction.value = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = response,
            error = null
        )
    }


    fun transactionFailure(error: ErrorTransaction) {
        _transactionError.value = error

        _statusResponseTransaction.value = StatusTransactionResponse(
            isLoading = false,
            isSuccess = false,
            data = null,
            error = error
        )

        error.message?.let {
            val messageTitleError = it
            setIsFatalError(true, messageTitleError)
        }

        error.messageDetails?.let {
            val messageDetailsError = it
            setMessageDetailsError(messageDetailsError)
        }

        error.errorTags?.let {
            val errorCodes = it
            setErrorTags(errorCodes)
        }
    }

    fun newOrderGateway(data: OrderDataRequest) {
        paymentRepository.newTransaction(
            data,
            ::transactionSuccess,
            ::transactionFailure
        )
    }

    fun setOriginTypeInsertProof(originTypeInsertProof: OriginTypeInsertProof?) {
        _origin_type_insert_proof.value = originTypeInsertProof
    }

    fun setProofImageUri(uri: Uri?) {
        _proof_image_uri.value = uri
    }

    fun setSelectedBankAccountWireTransfer(bankAccount: BankAccount?) {
        _selectedBankAccountWireTransfer.value = bankAccount
    }

    fun setCheckStatusDepositLoading(isLoading: Boolean) {
        _checkStatusDepositLoading.value = isLoading
    }

    fun checkStatusDepositSuccess(response: CheckStatusDepositResponse) {
        _checkStatusDepositResponse.value = response
        setCheckStatusDepositLoading(false)
    }

    private fun checkStatusDepositFailure(error: ErrorTransaction) {
        _checkStatusDepositResponse.value =
            CheckStatusDepositResponse(
                "error",
                0,
                error.message.toString(),
                null
            )
        setCheckStatusDepositLoading(false)
    }

    fun checkStatusDeposit(depositId: Int) {
        setCheckStatusDepositLoading(true)
        paymentRepository.checkStatusDeposit(
            depositId,
            ::checkStatusDepositSuccess,
            ::checkStatusDepositFailure
        )
    }

    fun checkStatusOrder(checkStatusOrderDataRequest: CheckStatusOrderDataRequest) {
        paymentRepository.checkStatusOrder(
            checkStatusOrderDataRequest,
            ::checkStatusOrderSuccess,
            ::checkStatusOrderFailure
        )
    }

    private fun checkStatusOrderSuccess(response: CheckStatusOrderDataResponse) {
        _checkStatusOrderDataResponse.value =
            CheckStatusOrderResponse(
                isLoading = false,
                isSuccess = true,
                data = response,
                error = null,
            )

    }


    private fun checkStatusOrderFailure(error: ErrorTransaction) {
        transactionFailure(error)

        _checkStatusOrderDataResponse.value =
            CheckStatusOrderResponse(
                isLoading = false,
                isSuccess = false,
                data = null,
                error = error,
            )
    }


    fun setCheckStatusTransactionLoading(isLoading: Boolean) {
        _checkStatusTransactionLoading.value = isLoading
    }

    private fun checkStatusTransactionSuccess(response: CheckStatusTransactionResponse) {
        _checkStatusTransactionResponse.value = response
        setCheckStatusDepositLoading(false)
    }

    private fun checkStatusTransactionFailure(error: ErrorTransaction) {
        _checkStatusTransactionResponse.value =
            CheckStatusTransactionResponse(
                "error",
                0,
                error.message.toString(),
                null
            )
        setCheckStatusDepositLoading(false)
    }


    fun checkStatusTransaction(transactionId: Int) {
        setCheckStatusTransactionLoading(true)
        paymentRepository.checkStatusTransaction(
            transactionId,
            ::checkStatusTransactionSuccess,
            ::checkStatusTransactionFailure
        )
    }


    fun startPayment(
        dataGenerateSignatureStartRequest: DataGenerateSignature,
        generateSignatureService: GenerateSignature = GenerateSignature(),
    ) {
        //Reset statusResponseTransaction
        _statusResponseTransaction.value = StatusTransactionResponse(
            isLoading = true,
            isSuccess = null,
            data = null,
            error = null,
        )

        CoroutineScope(Dispatchers.Main).launch {
            val generatedSignature = withContext(Dispatchers.Default) {
                generateSignatureService.generateSignature(dataGenerateSignatureStartRequest)
            }

            val processedData = generatedSignature.requiredDataGenerateSignature

            setDataStartPayment(processedData)

            var orderData = OrderDataRequest(
                processedData.base_url,
                processedData.merchant_id,
                processedData.merchant_transaction_id,
                processedData.amount,
                processedData.currency,
                processedData.operation,
                processedData.callback_url,
                processedData.type,
                processedData.selected_type,
                processedData.account_id,
                processedData.email,
                processedData.document_number,
                processedData.login_email,
                processedData.api_token,
                processedData.pix_key_type,
                processedData.pix_key,
                generatedSignature.signature,
                generatedSignature.url,
                processedData.auto_approve.toString()
            )

            val orderDataRequest = addDdiInPixKeyCellPhoneWithdraw(orderData)

            setDataRequestOrder(orderDataRequest)

            newOrderGateway(orderDataRequest)
        }
    }

    fun startPaymentByURL(dataStartPayment: OrderDataRequest) {
        //Reset statusResponseTransaction
        _statusResponseTransaction.value = StatusTransactionResponse(
            isLoading = true,
            isSuccess = null,
            data = null,
            error = null,
        )

        val handledDataOrderRequest = getHandledDataOrderRequestUrl(dataStartPayment)
        val orderDataRequest = addDdiInPixKeyCellPhoneWithdraw(handledDataOrderRequest)

        setDataRequestOrder(orderDataRequest)
        newOrderGateway(orderDataRequest)
    }

    fun setDataStartCheckout(data: DataStartCheckout) {
        _dataStartCheckout.value = data
    }

    fun insertTransferProofSuccess(responseData: InsertTransferProofDataResponse) {
        _transfer_proof_response.value = InsertTransferProofDataResponse(
            responseData.id,
            responseData.proof,
            responseData.wallet_id,
            responseData.user_id,
            responseData.deposit_status_id,
            isSuccess = true,
            loading = false,
            error = null,
        )
    }

    fun insertTransferProofFailure(responseErrorData: Throwable) {
        _transfer_proof_response.value = InsertTransferProofDataResponse(
            null,
            null,
            null,
            null,
            null,
            isSuccess = false,
            loading = false,
            error = responseErrorData.message,
        )
    }

    fun insertTransferProofLoading(isLoading: Boolean){
        _transfer_proof_response.value = InsertTransferProofDataResponse(
            null,
            null,
            null,
            null,
            null,
            loading = isLoading,
            error = null,
        )
    }

    fun insertTransferProof(
        insertTransferProofDataRequest: InsertTransferProofDataRequest,
    ) {

        insertTransferProofLoading(true)
        paymentRepository.transferProof(
            insertTransferProofDataRequest,
            ::insertTransferProofSuccess,
            ::insertTransferProofFailure
        )
    }

    fun setStatusResponseCheckServices(checkEnablerServices: CheckEnablerServices?) {
        _statusResponseCheckServices.value = checkEnablerServices
    }

    fun setStatusWithdrawOrder(statusWithdrawOrder: StatusWithdrawOrder) {
        _statusWithdrawOrder.value = statusWithdrawOrder
    }

}