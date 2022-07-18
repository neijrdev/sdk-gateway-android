package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wallet

import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentDepositWalletBinding
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DepositWalletFragment : Fragment() {
    private var _binding: FragmentDepositWalletBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()
    private val binding get() = _binding!!
    private var language: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositWalletBinding.inflate(inflater, container, false)
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
        logEventsService.setLogFinishScreen(Operation.DEPOSIT, Types.WALLET)

        mainViewModel.language.observe(viewLifecycleOwner) { language = it }

        mainViewModel.checkStatusTransactionResponse.observe(viewLifecycleOwner) {
            val transactionStatusId = it.data?.transaction_status_id
            binding.containerBackMerchantAndInstructions.visibility = View.VISIBLE

            binding.containerLoadingStatusDeposit.visibility = View.GONE

            setTransactionStatus(
                this,
                StatusTransactionFragment(),
                R.id.fragmentDepositStatus,
                binding.fragmentDepositStatus,
                transactionStatusId
            )
        }


        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {

            val limitsKycString = Gson().toJson(it.data?.kyc_limits)

            binding.containerLoadingStatusDeposit.visibility = View.GONE

            if (it.data?.transaction_id != null) {
                mainViewModel.checkStatusTransaction(it.data.transaction_id)
            }

            val currency = it.data?.original_currency

            setTransactionData(
                this,
                TransactionDataFragment(),
                R.id.containerFragmentTransactionData,
                it.data?.transaction_id,
                it.data?.original_amount,
                currency,
                it.data?.taxes,
                currency,
                it.data?.final_amount,
                currency,
                null,
                limitsKycString,
                language
            )
        }

        mainViewModel.checkStatusTransactionLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.containerLoadingStatusDeposit.visibility = View.VISIBLE
                binding.fragmentDepositStatus.visibility = View.GONE
            }
        }

    }

}