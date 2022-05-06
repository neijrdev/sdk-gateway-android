package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.BankAccounts
import com.paylivre.sdk.gateway.android.databinding.FragmentDepositWireTransferBinding
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.StatusTransactionFragment
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTextAcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTransactionData
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTransactionStatus
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel


fun setSelectBankFragment(
    currentViewFragment: Fragment,
    viewFragment: Fragment,
    containerResourceId: Int,
    container: ConstraintLayout,
    bankAccounts: BankAccounts? = null
) {
    //Data StartCheckout
    val bankAccountsString = Gson().toJson(bankAccounts)

    val bundle = Bundle()
    if (bankAccounts != null) {
        bundle.putString("bankAccounts", bankAccountsString)
    }

    viewFragment.arguments = bundle

    currentViewFragment.childFragmentManager.beginTransaction().apply {
        replace(containerResourceId, viewFragment)
        commit()
    }
    container.visibility = View.VISIBLE
}

class WireTransferFragment : Fragment() {
    private var _binding: FragmentDepositWireTransferBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var language: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositWireTransferBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setTextAcceptTerms(
            this,
            AcceptTerms(),
            R.id.containerAcceptTerms,
            getString(R.string.text_accept_terms_finishscreen)
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set Log Analytics
        LogEvents.setLogFinishScreen(Operation.DEPOSIT, Types.WIRETRANSFER)

        mainViewModel.language.observe(viewLifecycleOwner, { language = it })

        mainViewModel.checkStatusDepositLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.containerLoadingStatusDeposit.visibility = View.VISIBLE
                binding.fragmentDepositStatus.visibility = View.GONE
            }
        }

        mainViewModel.transfer_proof_response.observe(viewLifecycleOwner) {
            if (it?.loading == true) {
                binding.containerFormScrollView.scrollTo(0, binding.containerFormScrollView.top)
                binding.containerLoadingStatusDeposit.visibility = View.VISIBLE
                binding.fragmentDepositStatus.visibility = View.GONE
                binding.containerInsertProof.visibility = View.GONE
                binding.containerSelectBank.visibility = View.GONE
                binding.textErrorUploadProofFile.visibility = View.GONE
                //Set Log Analytics
                LogEvents.setLogEventAnalytics("LoadingInsertTransferProof")
            } else {
                if (it?.isSuccess == true) {
                    binding.fragmentDepositStatus.visibility = View.VISIBLE
                    binding.containerLoadingStatusDeposit.visibility = View.GONE
                    binding.containerInsertProof.visibility = View.GONE
                    binding.containerSelectBank.visibility = View.GONE
                    binding.textErrorUploadProofFile.visibility = View.GONE
                    binding.containerBackMerchantAndInstructions.visibility = View.VISIBLE

                    setTransactionStatus(
                        this,
                        StatusTransactionFragment(),
                        R.id.fragmentDepositStatus,
                        binding.fragmentDepositStatus,
                        it.deposit_status_id
                    )
                    //Set Log Analytics
                    LogEvents.setLogEventAnalytics("SuccessInsertTransferProof")

                } else {
                    binding.containerLoadingStatusDeposit.visibility = View.GONE
                    binding.fragmentDepositStatus.visibility = View.VISIBLE
                    binding.containerInsertProof.visibility = View.VISIBLE
                    binding.containerSelectBank.visibility = View.VISIBLE
                    binding.containerBackMerchantAndInstructions.visibility = View.GONE
                    binding.textErrorUploadProofFile.visibility = View.VISIBLE
                    //Set Log Analytics
                    LogEvents.setLogEventAnalytics("ErrorInsertTransferProof")
                }

            }
        }


        //ClearFocus selectBank
        binding.container.setOnClickListener {
            mainViewModel.setClearAllFocus(true)
        }

        binding.CardView.setOnClickListener {
            mainViewModel.setClearAllFocus(true)
        }

        mainViewModel.checkStatusDepositResponse.observe(viewLifecycleOwner) {
            val transactionStatusId = it.data?.deposit_status_id

            binding.containerLoadingStatusDeposit.visibility = View.GONE

            setTransactionStatus(
                this,
                StatusTransactionFragment(),
                R.id.fragmentDepositStatus,
                binding.fragmentDepositStatus,
                transactionStatusId
            )
        }


        mainViewModel.selectedBankAccountWireTransfer.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.containerInsertProof.visibility = View.VISIBLE
            } else {
                binding.containerInsertProof.visibility = View.GONE
            }

        }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            val limitsKycString = Gson().toJson(it.data?.kyc_limits)
            binding.containerLoadingStatusDeposit.visibility = View.GONE

            setSelectBankFragment(
                this,
                SelectBankAccountFragment(),
                R.id.containerSelectBank,
                binding.containerSelectBank,
                BankAccounts(bank_accounts = it.data?.bank_accounts)

            )

            if (it.data?.deposit_id != null) {
                mainViewModel.checkStatusDeposit(it.data?.deposit_id)
            }

            val currency = it.data?.original_currency


            setTransactionData(
                this,
                TransactionDataFragment(),
                R.id.containerFragmentTransactionData,
                it.data?.deposit_id,
                it.data?.original_amount,
                currency,
                it.data?.taxes,
                Currency.BRL.currency,
                it.data?.final_amount,
                Currency.BRL.currency,
                null,
                limitsKycString,
                language
            )
        }
    }

}