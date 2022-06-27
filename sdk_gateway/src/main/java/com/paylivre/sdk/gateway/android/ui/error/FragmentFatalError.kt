package com.paylivre.sdk.gateway.android.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentFatalErrorBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.*
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.roundToInt

class FragmentFatalError : Fragment() {

    private var _binding: FragmentFatalErrorBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()
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
        var currency: String? = null
        var msgError1: String? = null
        var msgError2: String? = null
        var msgError3: String? = null


        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Screen_GenericError")

        binding.btnClose.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_Close_ScreenError")

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
            setErrorMessage(
                context = context,
                keyError = it,
                textView = textViewMsgError,
                currency = currency
            )
        }

        mainViewModel.msgDetailsError.observe(viewLifecycleOwner) {
            setErrorMessage(
                context = context,
                keyError = it,
                textView = binding.textViewMsgSubtitle3Error,
                currency = currency
            )
        }

        mainViewModel.errorTags.observe(viewLifecycleOwner) {
            val errorCodes = it
            if (!errorCodes.isNullOrEmpty()) {
                msgError3 = errorCodes
                binding.textViewMsgSubtitle2Error.text = errorCodes
                binding.textViewMsgSubtitle2Error.visibility = View.VISIBLE
            }

            //Set Log Analytics
            logEventsService.setLogEventAnalyticsWithParams(
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