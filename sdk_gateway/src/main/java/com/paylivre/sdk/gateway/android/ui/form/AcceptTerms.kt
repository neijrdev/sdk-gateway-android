package com.paylivre.sdk.gateway.android.ui.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentAcceptTermsBinding
import com.paylivre.sdk.gateway.android.utils.makeLinks
import android.content.Intent
import android.net.Uri


class AcceptTerms : Fragment() {
    private var _binding: FragmentAcceptTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAcceptTermsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textAcceptTerms = binding.textViewTerms

        val bundle = this.arguments
        if (bundle != null) {
            val descriptionCustom =
                bundle.getString("description", getString(R.string.text_accept_terms))

            textAcceptTerms.text = descriptionCustom

        }


        val textTermsOfUser = getString(R.string.terms_of_use)
        val textPrivacyTerms = getString(R.string.privacy_terms)

        textAcceptTerms.makeLinks(
            Pair(textTermsOfUser, View.OnClickListener {
                //Action to click text
                val urlTermsOfUse = "https://www.paylivre.com/termos-e-condicoes-de-uso/"
                openUrl(urlTermsOfUse)
            }),
            Pair(textPrivacyTerms, View.OnClickListener {
                //Action to click text
                val urlPrivacyPolicy = "https://www.paylivre.com/politica-de-privacidade/"
                openUrl(urlPrivacyPolicy)
            })
        )

        return root
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}