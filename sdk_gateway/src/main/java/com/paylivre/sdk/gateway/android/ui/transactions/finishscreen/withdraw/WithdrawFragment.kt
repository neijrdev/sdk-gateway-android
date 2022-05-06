package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.CheckStatusOrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.KYC.LimitsKyc
import com.paylivre.sdk.gateway.android.data.model.order.StatusWithdrawOrder
import com.paylivre.sdk.gateway.android.databinding.FragmentWithdrawBinding
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTextAcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.setTransactionData
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel

const val TIMER_INTERVAL_IN_MILLIS: Long = 1000;
const val TIMER_FINAL_CHECK_IN_MILLIS: Long = 1000 * 121;

class WithdrawFragment : Fragment() {
    private var _binding: FragmentWithdrawBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var language: String? = null
    private var orderId: Int? = 0
    private var merchantApprovalStatusId: Int? = null
    private var finalAmount: Int? = null
    private var token: String? = null
    private lateinit var countDownTimer: CountDownTimer
    private var countDownTimerIsExpired: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Set Log Analytics
        LogEvents.setLogFinishScreen(Operation.WITHDRAW, Types.PIX)

        //set text custom terms
        setTextAcceptTerms(
            this,
            AcceptTerms(),
            R.id.containerAcceptTerms,
            getString(R.string.text_accept_terms_finishscreen)
        )

        return root
    }


    fun setLoadingStatusWithdraw() {
        when {
            merchantApprovalStatusId ==
                    MerchantApprovalStatusOrder.PENDING.code -> {
                binding.TxtViewMessageMerchantApprovalPending.visibility = View.VISIBLE
                binding.fragmentCheckingWithdrawLoading.visibility = View.GONE
                binding.TxtViewCheckOrderWithdrawInEmail.visibility = View.GONE
            }
            countDownTimerIsExpired -> {
                binding.TxtViewMessageMerchantApprovalPending.visibility = View.GONE
                binding.fragmentCheckingWithdrawLoading.visibility = View.GONE
                binding.TxtViewCheckOrderWithdrawInEmail.visibility = View.VISIBLE
            }
            else -> {
                binding.TxtViewMessageMerchantApprovalPending.visibility = View.GONE
                binding.fragmentCheckingWithdrawLoading.visibility = View.VISIBLE
                binding.TxtViewCheckOrderWithdrawInEmail.visibility = View.GONE
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(TIMER_FINAL_CHECK_IN_MILLIS,
            TIMER_INTERVAL_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                dispatchCheckStatusWithdraw(seconds)
            }

            override fun onFinish() {
                //Hidden loading check status withdraw
                countDownTimerIsExpired = true
                setLoadingStatusWithdraw()
            }
        }

        countDownTimer.start()
    }

    private fun setDataOrderWithdraw(
        deposit_id: Int?,
        origin_amount: Int?,
        original_currency: String?,
        taxAmount: Int?,
        final_amount: Int?,
        kycLimits: LimitsKyc?,
    ) {
        val limitsKycString = Gson().toJson(kycLimits)

        setTransactionData(
            this,
            TransactionDataFragment(),
            R.id.containerFragmentTransactionData,
            deposit_id,
            origin_amount,
            original_currency,
            taxAmount,
            null,
            final_amount,
            null,
            null,
            limitsKycString,
            language
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.language.observe(viewLifecycleOwner) { language = it }

        startTimer()

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            orderId = it.data?.order?.id
            token = it.data?.token
            finalAmount = it.data?.final_amount
            val withdrawalTypeId = it.data?.withdrawal_type_id

            //Insert withdraw type in title
            val withdrawTitleLabel = if (withdrawalTypeId == WithdrawTypes.PIX.code) "PIX"
            else getString(R.string.type_wallet)

            binding.TitleWithdrawPix.text = withdrawTitleLabel

            binding.fragmentWithdrawStatus.visibility = View.VISIBLE

            if (withdrawalTypeId == WithdrawTypes.PIX.code){
               binding.instructionsPix.container.visibility = View.VISIBLE
               binding.instructionsWallet.container.visibility = View.GONE

            } else {
                binding.instructionsPix.container.visibility = View.GONE
                binding.instructionsWallet.container.visibility = View.VISIBLE
            }
        }


        mainViewModel.checkStatusOrderDataResponse.observe(viewLifecycleOwner) {
            merchantApprovalStatusId = it.data?.order?.merchant_approval_status_id

            //Insert date for withdrawal status
            if (it.isSuccess == true) {
                mainViewModel.setStatusWithdrawOrder(
                    StatusWithdrawOrder(
                        withdrawal_type_id = it.data?.withdrawal_type_id,
                        withdrawal_status_id = it.data?.withdrawal?.status_id,
                        merchant_approval_status_id = it.data?.order?.merchant_approval_status_id,
                        order_status_id = it.data?.order?.status_id
                    )
                )

            }
            if (it.isSuccess == true && it.data?.final_amount != null) {
                countDownTimer.cancel()

                binding.fragmentCheckingWithdrawLoading.visibility = View.GONE
                binding.containerFragmentTransactionData.visibility = View.VISIBLE
                setDataOrderWithdraw(
                    it.data?.order?.id,
                    it.data?.original_amount,
                    it.data?.original_currency,
                    0,
                    it.data?.final_amount,
                    it.data?.kyc_limits
                )
            } else {
                setLoadingStatusWithdraw()
                binding.containerFragmentTransactionData.visibility = View.GONE
            }
        }
    }

    private fun dispatchCheckStatusWithdraw(seconds: Long) {
        val secondsLeft = seconds % 10
        val restDivision: Long = 0
        if (secondsLeft == restDivision) {
            orderId?.let {
                mainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(orderId!!, token.toString())
                )
            }
        }
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }
}