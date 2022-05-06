package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccount
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts
import com.paylivre.sdk.gateway.android.databinding.FragmentSelectBankAccountBinding
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.DataMakeBold
import com.paylivre.sdk.gateway.android.utils.makeBold
import java.lang.Exception


class SelectBankAccountFragment : Fragment() {
    private var _binding: FragmentSelectBankAccountBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

                //Filter bank accounts with hidden = 1
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
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Spinner_SelectBankAccount")

            val selectedItemText = banksList?.elementAt(position)
            val infoSelectedBankText = selectedItemText?.let {
                getBankAccountInfo(requireContext(),
                    it
                )
            }

            mainViewModel.setSelectedBankAccountWireTransfer(selectedItemText)

            binding.textViewBankInfo.visibility = View.VISIBLE
            binding.textViewBankInfo.text = infoSelectedBankText

            binding.textViewBankInfo.makeBold(
                DataMakeBold(
                    getString(R.string.label_bank)+":",
                    infoSelectedBankText.toString(),
                ),
                DataMakeBold(
                    getString(R.string.label_bank_office)+":",
                    infoSelectedBankText.toString(),
                ),
                DataMakeBold(
                    getString(R.string.label_account)+":",
                    infoSelectedBankText.toString(),
                )
            )

            binding.editSpinnerBanks.clearFocus()
        }

        mainViewModel.clearAllFocus.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.editSpinnerBanks.clearFocus()
                mainViewModel.setClearAllFocus(false)
            }
        })

        return root
    }
}