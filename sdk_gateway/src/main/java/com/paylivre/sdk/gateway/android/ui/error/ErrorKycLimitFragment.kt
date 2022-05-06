package com.paylivre.sdk.gateway.android.ui.error

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.Errors
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentErrorKycLimitBinding
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.utils.checkValidDrawableId
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyBRL
import com.paylivre.sdk.gateway.android.utils.makeLinks
import com.squareup.picasso.Picasso
import java.lang.Exception
import kotlin.math.roundToInt

const val LINK_PAGE_SUPPORT_LIMITS =
    "https://paylivrehelp.zendesk.com/hc/pt-br/articles/1500006060141-Quais-s%C3%A3o-os-limites-"

class ErrorKycLimitFragment : Fragment() {

    private var _binding: FragmentErrorKycLimitBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!
    private var language: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentErrorKycLimitBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var logoUrl: String? = null
        val textViewLimits = binding.textViewLimits
        val textViewMsgMakeNewTransaction = binding.textViewMsgMakeNewTransaction

        //Set Log Analytics
        LogEvents.setLogEventAnalytics("Screen_ErrorKycLimit")

        mainViewModel.language.observe(viewLifecycleOwner) {
            language = it
        }

        binding.btnClose.setOnClickListener {
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Btn_Close_ScreenErrorKycLimit")

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

        fun setErrors(errorKycLimit: Errors? = null) {
            try {
                val kycLevel = errorKycLimit?.kyc_level.toString()
                val kycLimitAvailable = errorKycLimit?.available_limit.toString()
                val kycLimitAvailableFormatted =
                    formatToCurrencyBRL(kycLimitAvailable, 100, language.toString())

                val errorKycLimitFormatted = getLimitsKycString(errorKycLimit)
                if (errorKycLimitFormatted.isNotEmpty()) {
                    textViewLimits.text = errorKycLimitFormatted
                }

                if (errorKycLimit?.available_limit!! > 0) {
                    binding.textViewMsgMakeNewTransaction.visibility = View.VISIBLE
                    //is kyc_level = 1
                    if (kycLevel == "1") {
                        val messageMakeNewTransaction =
                            getString(R.string.error_limit_kyc_make_a_new_transaction_kyc_level_1,
                                kycLimitAvailableFormatted)
                        textViewMsgMakeNewTransaction.text = messageMakeNewTransaction

                        //Insert link to register on Paylivre
                        textViewMsgMakeNewTransaction.makeLinks(
                            Pair("App Paylivre", View.OnClickListener {
                                openUrl("https://play.google.com/store/apps/details?id=com.paylivre.br")
                            }),
                            Pair("web.paylivre.com", View.OnClickListener {
                                openUrl("https://web.paylivre.com")
                            })
                        )

                    }
                    //is kyc_level = 2 || is kyc_level = 3
                    else if (kycLevel == "2" || kycLevel == "3") {
                        val messageMakeNewTransaction =
                            getString(R.string.error_limit_kyc_make_a_new_transaction_kyc_level_above_1,
                                kycLimitAvailableFormatted)
                        textViewMsgMakeNewTransaction.text = messageMakeNewTransaction
                    }
                }

            } catch (e: Exception) {
                println("Exception setError ErrorKycLimitFragment: $e")
            }

        }

        mainViewModel.transactionError.observe(viewLifecycleOwner) {
            setErrors(it?.errors)
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






        binding.linkPageSupportHelp.setOnClickListener {
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Btn_OpenLinkPageDocumentsLimits")

            openUrl(LINK_PAGE_SUPPORT_LIMITS)
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}