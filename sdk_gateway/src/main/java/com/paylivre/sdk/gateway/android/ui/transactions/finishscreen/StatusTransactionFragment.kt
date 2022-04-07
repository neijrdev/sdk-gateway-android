package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paylivre.sdk.gateway.android.databinding.FragmentStatusTransactionBinding

class StatusTransactionFragment : Fragment() {
    private var _binding: FragmentStatusTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var statusId: Int?

        _binding = FragmentStatusTransactionBinding.inflate(inflater, container, false)

        //Recebe os parametros
        val bundle = this.arguments
        if (bundle != null) {
            statusId = bundle.getInt("statusId", -1)
            val dataStatusTransaction = getDataStatusTransactionById(statusId)

            binding.imageStatusIcon.setImageResource(dataStatusTransaction.icon_drawable_id)
            binding.textViewTitle.text = "${getString(dataStatusTransaction.title_string_id)}!"
            binding.textViewBody.text = getString(dataStatusTransaction.body_message_string_id)
        }


        return binding.root
    }
}