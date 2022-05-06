package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.databinding.FragmentCheckingWithdrawLoadingBinding


class WithdrawCheckingLoadingFragment : Fragment() {
    private var _binding: FragmentCheckingWithdrawLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentCheckingWithdrawLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

}