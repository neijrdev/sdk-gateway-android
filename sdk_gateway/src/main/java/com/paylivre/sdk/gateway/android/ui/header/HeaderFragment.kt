package com.paylivre.sdk.gateway.android.ui.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.HeaderTitleBinding
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.checkValidDrawableId
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.roundToInt


class HeaderFragment : Fragment() {

    private var _binding: HeaderTitleBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HeaderTitleBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.ButtonCloseSDK.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_GoBack_Header")
            mainViewModel.setIsCloseSDK(true)
        }

        var logoUrl: String? = null

        mainViewModel.logoUrl.observe(viewLifecycleOwner) {
            logoUrl = it
        }

        mainViewModel.logoResId.observe(viewLifecycleOwner) {
            when {
                checkValidDrawableId(requireContext(), it) -> {
                    binding.logoMerchant.setImageResource(it)
                }
                logoUrl != null -> {
                    try {
                        val sizeWidth = dpToPx(resources, 160f).roundToInt()
                        val sizeHeight = dpToPx(resources, 160f).roundToInt()

                        val picasso = Picasso.Builder(requireContext())
                            .listener { picasso, uri, exception ->
                                println("Exception Picasso: " + exception.stackTraceToString())
                                binding.logoMerchant.setImageResource(R.drawable.ic_logo_paylivre_blue)
                            }
                            .build()

                        picasso
                            .load(logoUrl)
                            .resize(sizeWidth, sizeHeight)
                            .centerInside()
                            .into(binding.logoMerchant)

                    } catch (e: Exception) {
                        println("Catch Picasso:" + e.stackTraceToString())
                        binding.logoMerchant.setImageResource(R.drawable.ic_logo_paylivre_blue)
                    }

                }
                else -> {
                    binding.logoMerchant.setImageResource(R.drawable.ic_logo_paylivre_blue)
                }
            }

        }

        val textViewOperation: TextView = binding.textViewOperation


        mainViewModel.operation.observe(viewLifecycleOwner) {
            val operation =
                if (it == Operation.DEPOSIT.code) getString(R.string.deposit) else getString(R.string.withdraw)
            textViewOperation.text = operation
        }

        val versionName = BuildConfig.VERSION_NAME

        if (versionName.isNotEmpty()) {
            binding.textVersionSDK.text = "v${versionName}"
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}