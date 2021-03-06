package com.paylivre.sdk.gateway.android.ui.howToGenerateApiToken

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentHowToGenerateApiTokenBinding
import com.paylivre.sdk.gateway.android.utils.DataMakeBold
import com.paylivre.sdk.gateway.android.utils.makeBold

class HowToGenerateApiToken : DialogFragment() {
    private var _binding: FragmentHowToGenerateApiTokenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHowToGenerateApiTokenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.txtSubTitleModal.makeBold(
            DataMakeBold(
                getString(R.string.label_safety),
                getString(R.string.instructions_how_to_generate_api_token),
            ),
            DataMakeBold(
                getString(R.string.label_profile),
                getString(R.string.instructions_how_to_generate_api_token),
            ),
            DataMakeBold(
                getString(R.string.label_generate_api_token),
                getString(R.string.instructions_how_to_generate_api_token),
            )
        )



        return root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )

        binding.btnCloseModal.setOnClickListener { dismiss() }
    }


}