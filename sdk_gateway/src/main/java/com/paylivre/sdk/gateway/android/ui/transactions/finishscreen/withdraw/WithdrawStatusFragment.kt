package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.StatusWithdrawOrder
import com.paylivre.sdk.gateway.android.databinding.FragmentStatusWithdrawBinding
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel

class WithdrawStatusFragment : Fragment() {
    private var _binding: FragmentStatusWithdrawBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentStatusWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root


        fun getDataPaymentByWithdrawTypeId(
            withdrawalTypeId: Int?,
            statusWithdrawOrder: StatusWithdrawOrder?,
        ): DataStatusWithdrawResponse {
            return if (withdrawalTypeId == WithdrawTypes.PIX.code) {
                getDataStatusWithdrawById(statusWithdrawOrder?.withdrawal_status_id)
            } else {
                getDataStatusWithdrawOrderById(statusWithdrawOrder?.order_status_id)
            }
        }

        fun getDataMerchantApprovalByWithdrawTypeId(
            statusWithdrawOrder: StatusWithdrawOrder?,
        ): DataStatusWithdrawResponse {
            return getDataStatusMerchantApprovalById(statusWithdrawOrder?.merchant_approval_status_id)
        }

        mainViewModel.statusWithdrawOrder.observe(viewLifecycleOwner) {
            val statusWithdrawDataPayment =
                getDataPaymentByWithdrawTypeId(it.withdrawal_type_id, it)
            val statusWithdrawDataMerchantApproval =
                getDataMerchantApprovalByWithdrawTypeId(it)

            //Status Merchant Approval
            if (it.merchant_approval_status_id != null) {
                val merchantApprovalStatus =
                    getString(statusWithdrawDataMerchantApproval.title_string_id)
                val sLabelStatus = getString(R.string.label_status_approval_in_merchant) + ":"
                binding.txtStatusInMerchant.text = merchantApprovalStatus
                binding.txtLabelStatusInMerchant.text = sLabelStatus
                binding.iconStatusInMerchant.setImageResource(statusWithdrawDataMerchantApproval.icon_drawable_id)
                binding.containerStatusApprovalMerchant.visibility = View.VISIBLE
            }

            //Status Payment
            if (it.withdrawal_status_id != null || it.order_status_id != null) {
                val paymentStatus = getString(statusWithdrawDataPayment.title_string_id)
                val sLabelStatus = getString(R.string.payment_label_status) + ":"
                binding.txtLabelStatusPayment.text = sLabelStatus
                binding.txtStatusPayment.text = paymentStatus
                binding.iconStatusPayment.setImageResource(statusWithdrawDataPayment.icon_drawable_id)
                binding.containerStatusPayment.visibility = View.VISIBLE
            }
        }

        return root
    }
}
