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
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentErrorKycLimitBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.checkValidDrawableId
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyBRL
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import kotlin.math.roundToInt

const val LINK_PAGE_SUPPORT_LIMITS =
    "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-"
const val LINK_PAGE_REGISTER =
    "https://web.paylivre.com/signup"

class ErrorKycLimitFragment : Fragment() {

    private var _binding: FragmentErrorKycLimitBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val binding get() = _binding!!
    private var language: String? = ""
    var urlToOpen: String = LINK_PAGE_SUPPORT_LIMITS
    var urlPageSupportLimits: String = LINK_PAGE_SUPPORT_LIMITS
    private val logEventsService : LogEventsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentErrorKycLimitBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var logoUrl: String? = null
        val textViewLimits = binding.textViewLimits
        var kycLevel: String? = null
        var document: String? = null

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Screen_ErrorKycLimit")

        mainViewModel.order_data.observe(viewLifecycleOwner) {
            document = it?.document_number
        }

        mainViewModel.language.observe(viewLifecycleOwner) {
            language = it
        }

        binding.btnClose.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_Close_ScreenErrorKycLimit")

            mainViewModel.setIsCloseSDK(true)
        }

        fun getAvailableLimit(availableLimit: Int?): Int? {
            return try {
                if (availableLimit != null) {
                    if (availableLimit > 0) availableLimit else 0
                } else null
            } catch (e: Exception) {
                null
            }

        }

        fun getLimitsKycString(errorKycLimit: Errors? = null): String {
            try {
                val limitInt = errorKycLimit?.limit
                val usedLimitInt = errorKycLimit?.used_limit
                val availableLimitInt = getAvailableLimit(errorKycLimit?.available_limit)
                var limitFormatted = ""
                var usedLimitFormatted = ""
                var availableLimitFormatted = ""

                if (limitInt != null) {
                    limitFormatted = getString(R.string.label_limit_kyc_total,
                        formatToCurrencyBRL(limitInt.toString(), 100, language.toString()))
                }
                if (usedLimitInt != null) {
                    usedLimitFormatted = getString(R.string.label_limit_kyc_used,
                        formatToCurrencyBRL(usedLimitInt.toString(), 100, language.toString()))
                }
                if (availableLimitInt != null) {
                    availableLimitFormatted = getString(R.string.label_limit_kyc_available,
                        formatToCurrencyBRL(availableLimitInt.toString(), 100, language.toString()))
                }
                return "$limitFormatted\n$usedLimitFormatted\n$availableLimitFormatted"
            } catch (e: Exception) {
                return ""
            }
        }


        fun setErrors(errorKycLimit: Errors? = null) {
            try {
                val errorKycLimitFormatted = getLimitsKycString(errorKycLimit)
                if (errorKycLimitFormatted.isNotEmpty()) {
                    textViewLimits.text = errorKycLimitFormatted
                }
            } catch (e: Exception) {
                println("Exception setError ErrorKycLimitFragment: $e")
            }

        }

        mainViewModel.transactionError.observe(viewLifecycleOwner) {
            setErrors(it?.errors)
            kycLevel = it?.errors?.kyc_level

            urlToOpen = if (kycLevel == "0" || kycLevel == "1") {
                if (document != null) "$LINK_PAGE_REGISTER?document=$document"
                else LINK_PAGE_REGISTER
            }
            else {
                LINK_PAGE_SUPPORT_LIMITS
            }
        }

        mainViewModel.logoUrl.observe(viewLifecycleOwner) {
            logoUrl = it
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

        fun openUrlPageSupportLimits() {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlPageSupportLimits))
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


        fun openUrl() {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
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



        binding.btnIncreaseLimit.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_OpenLinkIncreaseLimit")
            openUrl()
        }


        binding.linkPageSupportHelp.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_OpenLinkPageDocumentsLimits")
            openUrlPageSupportLimits()
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}