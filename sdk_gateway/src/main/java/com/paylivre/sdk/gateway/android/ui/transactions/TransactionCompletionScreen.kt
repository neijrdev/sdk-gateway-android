package com.paylivre.sdk.gateway.android.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.OrderData
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.WithdrawalData
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentTransactionCompletionScreenBinding
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.domain.model.CurrencyPrefix
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.utils.toCurrencyBRL

class TransactionCompletionScreen : Fragment() {

    private var _binding: FragmentTransactionCompletionScreenBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransactionCompletionScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun getDescriptionTypeById(typeId: Int? = -1): String {
        return when (typeId) {
            Types.PIX.code -> " - PIX"
            Types.WALLET.code -> " - ${getString(R.string.type_wallet)}"
            Types.WIRETRANSFER.code -> " - ${getString(R.string.type_wiretransfer)}"
            Types.BILLET.code -> " - ${getString(R.string.type_billet)}"
            else -> ""
        }
    }

    fun getWithdrawData(withdraw: WithdrawalData?, orderData: OrderData?): String {
        return "Id: ${withdraw?.id}, Status Merchant: ${orderData?.merchant_approval_status_name}"
    }

    fun getCurrencyFormated(amount: Float = 0F): String {
        return amount.toDouble().toCurrencyBRL()
    }

    fun getAmountData(transactionResponse: ResponseCommonTransactionData?): String {
        val original_amount = if (transactionResponse?.original_amount == null) {
            transactionResponse?.origin_amount?.toFloat()?.div(100)
        } else {
            transactionResponse.original_amount?.toFloat()?.div(100)
        }

        val original_currency = transactionResponse?.original_currency
        val original_currency_prefix = if (original_currency == Currency.USD.toString())
            CurrencyPrefix.USD.prefix
        else CurrencyPrefix.BRL.prefix
        val converted_amount =
            if (original_currency == Currency.USD.toString())
                "\n\nConverted Amount: ${CurrencyPrefix.BRL.prefix} " + transactionResponse.converted_amount?.toFloat()
                    ?.div(100)!!
            else ""

        val final_amount = "\n\nTotal: ${CurrencyPrefix.BRL.prefix} " + getCurrencyFormated(
            transactionResponse?.final_amount?.toFloat()?.div(100)!!
        )
        val taxesInt = if (transactionResponse.taxes == null) 0 else transactionResponse.taxes
        val taxes = "\n\nTaxas: ${CurrencyPrefix.BRL.prefix} " + taxesInt.toFloat()
            ?.div(100)?.let {
                getCurrencyFormated(
                    it
                )
            }
        return "Original Amount: $original_currency_prefix ${original_amount}$converted_amount${taxes}$final_amount"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner, {
            val responseData = it.data
            val isNotDeposit = responseData?.deposit_type_id == null
            val isDepositPix = responseData?.deposit_type_id == Types.PIX.code
            val isDepositBillet = responseData?.deposit_type_id == Types.BILLET.code

            val operation =
                if (isNotDeposit) getString(R.string.withdraw) else getString(R.string.deposit)

            val type = if (isNotDeposit) " - PIX" else getDescriptionTypeById(responseData?.deposit_type_id)

            val additionalData =
                if (isNotDeposit) getWithdrawData(
                    responseData?.withdrawal,
                    responseData?.order
                ) else if (isDepositPix || isDepositBillet) responseData?.receivable_url else ""

            binding.textViewTypeTransaction.text = "$operation$type"
            binding.textViewDataTransaction1.text = additionalData
            binding.textViewAmount.text = getAmountData(responseData)

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}