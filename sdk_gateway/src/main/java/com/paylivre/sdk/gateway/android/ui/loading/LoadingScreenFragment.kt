package com.paylivre.sdk.gateway.android.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentLoadingScreenBinding
import com.paylivre.sdk.gateway.android.services.log.LogEvents

class LoadingScreenFragment : Fragment() {

    private var _binding: FragmentLoadingScreenBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Recebe os parametros de quem instanciou o loadingModal
        val bundle = this.arguments
        if (bundle != null) {
            val messageLoading = bundle.getString("message_loading", getString(R.string.checking_data))
            binding.textViewMessageLoading.text = messageLoading
        }

        //Set Log Analytics
        LogEvents.setLogEventAnalytics("Screen_CheckServicesStatus")

        _binding = FragmentLoadingScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}