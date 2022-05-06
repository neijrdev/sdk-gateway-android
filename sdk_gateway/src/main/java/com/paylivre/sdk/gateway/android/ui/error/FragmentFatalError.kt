package com.paylivre.sdk.gateway.android.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentFatalErrorBinding
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.utils.*
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class FragmentFatalError : Fragment() {

    private var _binding: FragmentFatalErrorBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentFatalErrorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textViewMsgError = binding.TextViewMsgSubtitleError

        var logoUrl: String? = null
        var isErrorWithdrawLimit: Boolean? = null
        var currency: String? = null
        var msgError1 : String? = null
        var msgError2 : String? = null
        var msgError3 : String? = null


        //Set Log Analytics
        LogEvents.setLogEventAnalytics("Screen_GenericError")

        fun getMessageDetails(keyMessage: String?): String {
            return if (isErrorWithdrawLimit == true) {
                getStringByKey(context, keyMessage + currency)
            } else {
                getStringByKey(context, keyMessage.toString())
            }
        }

        binding.btnClose.setOnClickListener {
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Btn_Close_ScreenError")

            mainViewModel.setIsCloseSDK(true)
        }

        mainViewModel.currency.observe(viewLifecycleOwner) {
            currency = it
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


        mainViewModel.keyMsgFatalError.observe(viewLifecycleOwner) {
            val titleMsgError = getStringByKey(context, it.toString())
            if (!titleMsgError.isNullOrEmpty()) {
                msgError1 = titleMsgError
                textViewMsgError.text = titleMsgError
                textViewMsgError.visibility = View.VISIBLE
            }
        }

        mainViewModel.msgDetailsError.observe(viewLifecycleOwner) {
            isErrorWithdrawLimit = it == "exceeded_withdrawal_limit_value"
            val msgDetailsError = getMessageDetails(it.toString())
            if (!msgDetailsError.isNullOrEmpty()) {
                msgError2 = msgDetailsError
                binding.textViewMsgSubtitle3Error.text = msgDetailsError
                binding.textViewMsgSubtitle3Error.visibility = View.VISIBLE
            }
        }

        mainViewModel.errorTags.observe(viewLifecycleOwner) {
            println("errorTags-> $it")
            val errorCodes = it
            if (!errorCodes.isNullOrEmpty()) {
                msgError3 = errorCodes
                binding.textViewMsgSubtitle2Error.text = errorCodes
                binding.textViewMsgSubtitle2Error.visibility = View.VISIBLE
            }

            //Set Log Analytics
            LogEvents.setLogEventAnalyticsWithParams(
                "Screen_Error",
                Pair("message_error_1", msgError1 ?: ""),
                Pair("message_error_2", msgError2 ?: ""),
                Pair("message_error_3", msgError3 ?: ""),
            )
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}