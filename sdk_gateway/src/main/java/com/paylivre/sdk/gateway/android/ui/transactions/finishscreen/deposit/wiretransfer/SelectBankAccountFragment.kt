package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccount
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts
import com.paylivre.sdk.gateway.android.databinding.FragmentSelectBankAccountBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception


class SelectBankAccountFragment : Fragment() {
    private var _binding: FragmentSelectBankAccountBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService: LogEventsService by inject()
    private val binding get() = _binding!!

    private fun setButtonBankField(
        containerResourceId: Int,
        label: String,
        value: String,
        isShowDividerLine: Boolean = true,
    ) {
        val bundle = Bundle()
        bundle.putString("label", label)
        bundle.putString("value", value)
        bundle.putBoolean("isShowDividerLine", isShowDividerLine)

        val viewFragment = ButtonBankInfoRow()
        viewFragment.arguments = bundle

        childFragmentManager.beginTransaction().apply {
            replace(containerResourceId, viewFragment)
            commit()
        }
    }

    fun setOnBankClick(position: Int, banksList: List<BankAccount>? = null) {
        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Spinner_SelectBankAccount")

        val selectedItemText = banksList?.elementAt(position)

        mainViewModel.setSelectedBankAccountWireTransfer(selectedItemText)

        binding.containerBankInfo.visibility = View.VISIBLE

        //Set bank name
        setButtonBankField(
            R.id.bankName,
            getString(R.string.label_bank),
            "${selectedItemText?.bank_number} - ${selectedItemText?.account_name}"
        )

        //Set bank office number and digit
        setButtonBankField(
            R.id.bankOffice,
            getString(R.string.label_bank_office),
            getBankOffice(selectedItemText?.office_number, selectedItemText?.office_digit)
        )

        //Set bank account number and digit
        setButtonBankField(
            R.id.bankAccount,
            getString(R.string.label_account),
            getBankAccountNumber(selectedItemText?.account_number, selectedItemText?.account_digit)
        )


        //Set bank owner
        setButtonBankField(
            R.id.bankOwner,
            getString(R.string.label_favored),
            selectedItemText?.account_holder_full_name.toString()
        )

        //Set bank document
        setButtonBankField(
            R.id.bankDocument,
            "CNPJ",
            getDocumentAccountNumber(selectedItemText?.account_holder_document),
        )


        binding.editSpinnerBanks.clearFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectBankAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.editSpinnerBanks.inputType = InputType.TYPE_NULL

        val bundle = this.arguments

        var banksList: List<BankAccount>? = null

        if (bundle != null) {
            try {
                val bankAccountsString = bundle.getString("bankAccounts")

                val bankAccounts = Gson().fromJson(
                    bankAccountsString,
                    BankAccounts::class.java
                )

                //Filter bank accounts with hidden = 1 and display = 0
                banksList = getEnabledBanksAccounts(bankAccounts)

                if (banksList != null) {
                    val banksListName = getNameBanksAccountsList(banksList)
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.dropdown_item_text, banksListName)
                    binding.editSpinnerBanks.setAdapter(arrayAdapter)
                }

            } catch (e: Exception) {
                println(e)
            }
        }


        binding.editSpinnerBanks.setOnItemClickListener { _, _, position, _ ->
            setOnBankClick(position, banksList)
        }

        mainViewModel.clearAllFocus.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.editSpinnerBanks.clearFocus()
                mainViewModel.setClearAllFocus(false)
            }
        }

        return root
    }
}