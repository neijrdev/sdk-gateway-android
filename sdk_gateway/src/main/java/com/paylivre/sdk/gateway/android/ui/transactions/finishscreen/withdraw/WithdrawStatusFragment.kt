package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentStatusWithdrawBinding
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.getDataStatusTransactionById
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel

class WithdrawStatusFragment : Fragment() {
    private var _binding: FragmentStatusWithdrawBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatusWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner, {
            val paymentStatusId = it.data?.withdrawal?.id
            val merchantApprovalStatusId = it.data?.order?.merchant_approval_status_id

            //Status Merchant Approval
            if (merchantApprovalStatusId != null) {
                val merchantApprovalStatusData =
                    getDataStatusTransactionById(merchantApprovalStatusId)
                val merchantApprovalStatus = getString(merchantApprovalStatusData.title_string_id)
                val sLabelStatus = getString(R.string.label_status_approval_in_merchant) + ":"
                binding.txtStatusInMerchant.text = merchantApprovalStatus
                binding.txtLabelStatusInMerchant.text = sLabelStatus
                binding.iconStatusInMerchant.setImageResource(merchantApprovalStatusData.icon_drawable_id)
                binding.containerStatusApprovalMerchant.visibility = View.VISIBLE
            }

            //Status Payment
            if (paymentStatusId != null) {
                val paymentStatusData =
                    getDataStatusTransactionById(paymentStatusId)
                val paymentStatus = getString(paymentStatusData.title_string_id)
                val sLabelStatus = getString(R.string.payment_label_status) + ":"
                binding.txtLabelStatusPayment.text = sLabelStatus
                binding.txtStatusPayment.text = paymentStatus
                binding.iconStatusPayment.setImageResource(paymentStatusData.icon_drawable_id)
                binding.containerStatusPayment.visibility = View.VISIBLE
            }
        })

        return root
    }
}
