package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.pix

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context.CLIPBOARD_SERVICE
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.databinding.FragmentDepositPixBinding
import com.paylivre.sdk.gateway.android.domain.model.DepositStatus
import com.paylivre.sdk.gateway.android.ui.form.AcceptTerms
import com.paylivre.sdk.gateway.android.ui.transactions.data.TransactionDataFragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit
import com.paylivre.sdk.gateway.android.utils.START_TIME_IN_MILLIS
import androidx.core.text.HtmlCompat
import com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


val START_TIME_IN_MINUTES = TimeUnit.MILLISECONDS.toMinutes(START_TIME_IN_MILLIS) % 60

class DepositPixFragment : Fragment() {
    private var _binding: FragmentDepositPixBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var minutesToExpirePix: Long = -1
    private var depositId: Int? = 0
    private var statusDepositId: Int? = 1
    private var language: String? = null

    private lateinit var countDownTimer: CountDownTimer

    private fun setLastStatusDepositPix(response: CheckStatusDepositResponse?) {
        val statusCodeDepositPix = response?.data?.deposit_status_id
        if (statusCodeDepositPix != null) {
            val currentTime: String =
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val paymentLabelString = getString(R.string.payment_label_status)
            val status =
                getString(getStringIdStatusTransactionById(statusCodeDepositPix)).lowercase()
            val lastUpdateTimeString = getString(R.string.last_update_time, currentTime)

            val stringStatusFull =
                "$paymentLabelString <font><b>$status</b></font>. $lastUpdateTimeString"
            val stringHtmlStyle =
                HtmlCompat.fromHtml(stringStatusFull, HtmlCompat.FROM_HTML_MODE_LEGACY);
            binding.textLastStatusDepositPix.text = stringHtmlStyle
            binding.textLastStatusDepositPix.visibility = View.VISIBLE
        }

    }


    private fun checkStatusDepositPix(response: CheckStatusDepositResponse?) {
        val statusCodeDepositPix = response?.data?.deposit_status_id
        if (statusCodeDepositPix != null) {
            if (statusCodeDepositPix == DepositStatus.COMPLETED.code ||
                statusCodeDepositPix == DepositStatus.CANCELLED.code
            ) {
                finishCheckStartStatus()
            }
        }
    }

    private fun dispatchCheckStatusDepositPix(minutes: Long) {
        if (minutesToExpirePix != minutes) {
            minutesToExpirePix = minutes
            depositId?.let { mainViewModel.checkStatusDeposit(it) }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                if (minutes < (START_TIME_IN_MINUTES - 1)) {
                    dispatchCheckStatusDepositPix(minutes)
                }
                binding.textViewTimeExpirePix.text = getString(
                    R.string.formatted_time, minutes, seconds,
                )
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: called")
                finishCheckStartStatus()
            }
        }

        countDownTimer.start()
    }

    private fun finishCheckStartStatus() {
        countDownTimer.cancel()
        binding.containerCodePixAndCheckStatus.visibility = View.GONE

        setTransactionStatus(
            this,
            StatusTransactionFragment(),
            R.id.fragmentDepositStatus,
            binding.fragmentDepositStatus,
            statusDepositId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositPixBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setTextAcceptTerms(
            this,
            AcceptTerms(),
            R.id.containerAcceptTerms,
            getString(R.string.text_accept_terms_finishscreen)
        )


        return root
    }

    private lateinit var toast: Toast

    private fun copyToClipboard(text: String) {
        try {

            val myClipboard: ClipboardManager? = context?.getSystemService(CLIPBOARD_SERVICE)
                    as ClipboardManager?

            val myClip: ClipData = ClipData.newPlainText("text", text)
            myClipboard!!.setPrimaryClip(myClip)

            toast =
                Toast.makeText(
                    context,
                    getString(R.string.message_pix_code_copied),
                    Toast.LENGTH_SHORT
                )

            toast.show();
        } catch (e: Exception) {
            println(e)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var codePix = ""

        mainViewModel.language.observe(viewLifecycleOwner, { language = it })

        mainViewModel.checkStatusDepositLoading.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.textLastStatusDepositPix.visibility = View.GONE
                binding.containerCheckingStatus.visibility = View.VISIBLE
            } else {
                binding.textLastStatusDepositPix.visibility = View.VISIBLE
                binding.containerCheckingStatus.visibility = View.GONE
            }
        })

        mainViewModel.checkStatusDepositResponse.observe(viewLifecycleOwner, {
            statusDepositId = it?.data?.deposit_status_id
            checkStatusDepositPix(it)
            setLastStatusDepositPix(it)
        })

        binding.textCodePix.setOnClickListener {
            copyToClipboard(codePix)
        }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner, {
            codePix = it.data?.receivable_url.toString()
            binding.textCodePix.text = codePix

            //StartTime to checkStatusPix and Expire Time Pix
            startTimer()

            binding.fragmentBackMerchant.visibility = View.VISIBLE

            val limitsKycString = Gson().toJson(it.data?.kyc_limits)
            depositId = it.data?.deposit_id

            binding.textCodePix.requestFocus()

            setTransactionData(
                this,
                TransactionDataFragment(),
                R.id.containerFragmentTransactionData,
                it.data?.deposit_id,
                it.data?.original_amount,
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

        binding.btnCopyCodePix.setOnClickListener {
            copyToClipboard(codePix)
        }
    }

    override fun onDestroy() {
        countDownTimer.cancel()

        super.onDestroy()
    }

}