package com.paylivre.sdk.gateway.android.ui.error

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentErrorKycUserBlockedBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.checkValidDrawableId
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.paylivre.sdk.gateway.android.utils.getBlockReasonByLocale
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import kotlin.math.roundToInt

class ErrorKycUserBlocked : Fragment() {

    private var _binding: FragmentErrorKycUserBlockedBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()

    private val binding get() = _binding!!

    private var errorTransaction: ErrorTransaction? = null
    private var linkBlockReason: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentErrorKycUserBlockedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Screen_ErrorKycUserBlocked")

        binding.btnClose.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_Close_Screen_ErrorKycUserBlocked")

            mainViewModel.setIsCloseSDK(true)
        }

        var logoUrl: String? = null
        var language: String? = null

        mainViewModel.logoUrl.observe(viewLifecycleOwner) {
            logoUrl = it
        }

        mainViewModel.language.observe(viewLifecycleOwner) {
            language = it
        }

        fun setErrors(error: ErrorTransaction?) {
            try {
                errorTransaction = error
                val blockReasonLink = errorTransaction?.errors?.link

                val blockReasonByLocale = getBlockReasonByLocale(error?.errors, language ?: "en")
                val isLimitKyc1Exceeded =
                    errorTransaction?.errors?.internal_reason_pt == "Limite KYC 1 atingido."

                if (isLimitKyc1Exceeded) {
                    binding.TextViewMsgSubtitleError.text =
                        "$blockReasonByLocale\n\n${getString(R.string.error_screen_limit_emailsend_kyc1)}"
                } else {
                    binding.TextViewMsgSubtitleError.text = blockReasonByLocale
                }

                if (blockReasonLink != null) {
                    binding.textLink.text = blockReasonLink
                    binding.containerLink.visibility = View.VISIBLE
                    linkBlockReason = blockReasonLink
                } else {
                    binding.containerLink.visibility = View.GONE
                }
            } catch (e: Exception) {
                println("Exception setError ErrorKycUserBlockedFragment: $e")
            }
        }

        mainViewModel.transactionError.observe(viewLifecycleOwner) {
            setErrors(it)
        }

        mainViewModel.logoResId.observe(viewLifecycleOwner) {
            if (checkValidDrawableId(requireContext(), it)) {
                binding.logoMerchant.setImageResource(it)
                binding.logoMerchant.visibility = View.VISIBLE
            } else {
                try {
                    val sizeWidth = dpToPx(resources, 160f).roundToInt()
                    val sizeHeight = dpToPx(resources, 160f).roundToInt()
                    Picasso.get()
                        .load(logoUrl)
                        .resize(sizeWidth, sizeHeight)
                        .centerInside()
                        .into(binding.logoMerchant)
                } catch (e: Exception) {
                    println(e)
                }
            }
        }


        fun openUrl(url: String?) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            } catch (e: Exception) {
                var toast: Toast = Toast.makeText(
                    context,
                    getString(R.string.erro_default_open_url),
                    Toast.LENGTH_SHORT
                )
                toast.show();
            }

        }


        binding.textLink.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_OpenLinkBlockReason")
            openUrl(linkBlockReason)
        }



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}