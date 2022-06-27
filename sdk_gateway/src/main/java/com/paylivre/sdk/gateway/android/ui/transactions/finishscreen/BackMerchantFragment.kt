package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paylivre.sdk.gateway.android.databinding.FragmentBackMerchantBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BackMerchantFragment : Fragment() {
    private var _binding: FragmentBackMerchantBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBackMerchantBinding.inflate(inflater, container, false)

        binding.btnBackAppMerchant.setOnClickListener {
            mainViewModel.setIsCloseSDK(true)

            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_BackToMerchant")
        }

        return binding.root
    }
}