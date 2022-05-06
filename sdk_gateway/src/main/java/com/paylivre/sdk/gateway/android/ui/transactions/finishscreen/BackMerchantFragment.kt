package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.databinding.FragmentBackMerchantBinding
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel

class BackMerchantFragment : Fragment() {
    private var _binding: FragmentBackMerchantBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
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
            LogEvents.setLogEventAnalytics("Btn_BackToMerchant")
        }

        return binding.root
    }
}