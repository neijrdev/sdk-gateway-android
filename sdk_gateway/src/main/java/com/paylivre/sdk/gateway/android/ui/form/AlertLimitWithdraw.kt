package com.paylivre.sdk.gateway.android.ui.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentAlertLimitWithdrawBinding
import com.paylivre.sdk.gateway.android.domain.model.Currency
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyBRL
import com.paylivre.sdk.gateway.android.utils.formatToCurrencyUSD


class AlertLimitWithdraw : Fragment() {
    private var _binding: FragmentAlertLimitWithdrawBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var language: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlertLimitWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mainViewModel.language.observe(viewLifecycleOwner, {
            language = it
        })


        mainViewModel.currency.observe(viewLifecycleOwner, {
            if (it == Currency.BRL.toString()) {
                val limit = formatToCurrencyBRL("200000", 100, language.toString())
                binding.textViewAlert.text = getString(R.string.alert_limit_withdraw, limit)
            } else if (it == Currency.USD.toString()) {
                val limit = formatToCurrencyUSD("40000", 100, language.toString())
                binding.textViewAlert.text = getString(R.string.alert_limit_withdraw, limit)
            }
        })

        binding.containerAlert.setOnClickListener {
            binding.containerAlert.visibility = View.GONE
        }

        return root
    }
}