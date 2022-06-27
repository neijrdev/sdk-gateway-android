package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet

import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentDepositBilletBinding
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


fun setBilletBarCodeFragment(
    currentViewFragment: Fragment,
    viewFragment: Fragment,
    containerResourceId: Int,
    container: ConstraintLayout,
    receivableUrl: String? = "",
    billetDigitableLine: String? = "",
) {
    val bundle = Bundle()
    if (receivableUrl != null) {
        bundle.putString("receivable_url", receivableUrl)
    }
    if (billetDigitableLine != null) {
        bundle.putString("billet_digitable_line", billetDigitableLine)
    }
    viewFragment.arguments = bundle

    currentViewFragment.childFragmentManager.beginTransaction().apply {
        replace(containerResourceId, viewFragment)
        commit()
    }
    container.visibility = View.VISIBLE
}

class DepositBilletFragment : Fragment() {
    private var _binding: FragmentDepositBilletBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()
    private val binding get() = _binding!!
    private var language: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDepositBilletBinding.inflate(inflater, container, false)
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
        logEventsService.setLogFinishScreen(Operation.DEPOSIT, Types.BILLET)

        mainViewModel.language.observe(viewLifecycleOwner, { language = it })

        mainViewModel.checkStatusDepositLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.containerLoadingStatusDeposit.visibility = View.VISIBLE
                binding.fragmentDepositStatus.visibility = View.GONE
            }
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


        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {

            val limitsKycString = Gson().toJson(it.data?.kyc_limits)

            binding.containerLoadingStatusDeposit.visibility = View.GONE

            binding.containerBackMerchantAndInstructions.visibility = View.VISIBLE

            if (it.data?.deposit_id != null) {
                mainViewModel.checkStatusDeposit(it.data?.deposit_id)
            }

            val currency = it.data?.original_currency

            val billetDueDateFormatted = getBilletDueDateFormatted(it.data?.billet_due_date)



            setBilletBarCodeFragment(
                this,
                BilletBarCodeFragment(),
                R.id.containerFragmentBarCodeBillet,
                binding.containerFragmentBarCodeBillet,
                it.data?.receivable_url, //link pdf to download
                it.data?.billet_digitable_line//bar code billet
            )

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
                billetDueDateFormatted,
                limitsKycString,
                language
            )
        }

    }

}