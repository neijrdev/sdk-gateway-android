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
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentErrorKycLimitBinding
import com.paylivre.sdk.gateway.android.utils.checkValidDrawableId
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.squareup.picasso.Picasso
import java.lang.Exception
import kotlin.math.roundToInt

class ErrorKycLimitFragment : Fragment() {

    private var _binding: FragmentErrorKycLimitBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentErrorKycLimitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnClose.setOnClickListener {
            mainViewModel.setIsCloseSDK(true)
        }

        var logoUrl: String? = null

        mainViewModel.logoUrl.observe(viewLifecycleOwner, {
            logoUrl = it
        })

        mainViewModel.logoResId.observe(viewLifecycleOwner, {
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
        })


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


        binding.linkAppPlayStore.setOnClickListener {
            openUrl("https://play.google.com/store/apps/details?id=com.paylivre.br")
        }

        binding.linkWebPaylivre.setOnClickListener {
            openUrl("https://web.paylivre.com")
        }

        binding.linkPageDocumentsLimits.setOnClickListener {
            openUrl("https://www.paylivre.com/documentos-e-limites")
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}