package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentWithdrawBinding
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTextAcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTransactionData
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel


class WithdrawFragment : Fragment() {
    private var _binding: FragmentWithdrawBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var language: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //set text custom terms
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

        mainViewModel.language.observe(viewLifecycleOwner, { language = it })

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner, {
            val withdrawId = it.data?.withdrawal?.id


            //Insert withdraw Id in title
            val withdrawTitleLabel = getString(R.string.withdrawal_request_label)
            if (withdrawId != null) {
                val orderId = getString(R.string.witidrawal_order_id_label, withdrawId.toString())
                binding.TitleWithdrawPix.text = "$withdrawTitleLabel - $orderId"
            } else {
                binding.TitleWithdrawPix.text = withdrawTitleLabel
            }

            val limitsKycString = Gson().toJson(it.data?.kyc_limits)

            binding.fragmentWithdrawStatus.visibility = View.VISIBLE

            setTransactionData(
                this,
                TransactionDataFragment(),
                R.id.containerFragmentTransactionData,
                it.data?.deposit_id,
                it.data?.origin_amount,
                it.data?.original_currency,
                it.data?.taxes,
                null,
                it.data?.final_amount,
                null,
                null,
                limitsKycString,
                language
            )
        })
    }
}