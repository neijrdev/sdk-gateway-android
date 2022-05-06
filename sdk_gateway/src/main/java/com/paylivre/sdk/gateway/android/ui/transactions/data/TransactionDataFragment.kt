package com.paylivre.sdk.gateway.android.ui.transactions.data


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.databinding.FragmentTransactionDataBinding
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyBRL
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyUSD


class TransactionDataFragment : Fragment() {

    private var _binding: FragmentTransactionDataBinding? = null
    private var transactionId: Int? = 0
    private var originalAmount: Int? = 0
    private var originalCurrency: String? = ""
    private var taxAmount: Int? = 0
    private var taxCurrency: String? = ""
    private var totalAmount: Int? = 0
    private var dueDate: String? = null
    private var limitsKyc: LimitsKyc? = null
    private var finalCurrency: String? = ""
    private var language: String? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransactionDataBinding.inflate(inflater, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            transactionId = bundle.getInt("transactionId", -1)
            originalAmount = bundle.getInt("originalAmount", 0)
            originalCurrency = bundle.getString("originalCurrency").toString()
            taxAmount = bundle.getInt("taxAmount", 0)
            taxCurrency = bundle.getString("taxCurrency").toString()
            dueDate = bundle.getString("dueDate")
            totalAmount = bundle.getInt("totalAmount", 0)
            finalCurrency = bundle.getString("finalCurrency").toString()
            language = bundle.getString("language").toString()


            val limitsKycString = bundle.getString("limitsKyc").toString()
            limitsKyc = Gson().fromJson(
                limitsKycString,
                LimitsKyc::class.java
            )

        }

        return binding.root
    }

    private fun showDialogLimit() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.label_available_limit))
            .setMessage(getString(R.string.label_available_limit_about))
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Transaction ID
        if (transactionId != null && transactionId!! > 0) {
            binding.rowIdentifier.visibility = View.VISIBLE
            binding.textIdValue.text = transactionId.toString()
        }

        //Original Amount
        if (originalAmount != null) {

            var originalAmountFormatted = if (originalCurrency == Currency.USD.toString()) {
                formatToCurrencyUSD(originalAmount.toString(), 100, language.toString())
            } else {
                formatToCurrencyBRL(originalAmount.toString(), 100, language.toString())
            }
            binding.rowOriginalAmount.visibility = View.VISIBLE
            binding.textOriginalAmountValue.text = originalAmountFormatted
        }

        //Tax Amount
        if (taxAmount != null) {
            var originalAmountFormatted = if (taxCurrency == Currency.USD.toString()) {
                formatToCurrencyUSD(taxAmount.toString(), 100, language.toString())
            } else {
                formatToCurrencyBRL(taxAmount.toString(), 100, language.toString())
            }
            binding.rowTaxAmount.visibility = View.VISIBLE
            binding.textTaxAmountValue.text = originalAmountFormatted
        }

        //Total Amount
        if (totalAmount != null) {
            var originalAmountFormatted = if (finalCurrency == Currency.USD.toString()) {
                formatToCurrencyUSD(totalAmount.toString(), 100, language.toString())
            } else {
                formatToCurrencyBRL(totalAmount.toString(), 100, language.toString())
            }
            binding.rowTotalAmount.visibility = View.VISIBLE
            binding.textTotalAmountValue.text = originalAmountFormatted
        }

        //DueData
        if (dueDate != null && dueDate != "") {
            binding.textDueDateValue.text = dueDate
            binding.rowDueDate.visibility = View.VISIBLE
        }


        //limitsKyc Amount
        if (limitsKyc != null) {
            binding.rowLimitAmount.visibility = View.VISIBLE
            binding.textLimitAmountValue.text =
                formatToCurrencyBRL(
                    limitsKyc!!.available_amount.toString(),
                    100,
                    language.toString()
                )
        }


        binding.btnShowAboutLimit.setOnClickListener {
            showDialogLimit()
        }
    }

}