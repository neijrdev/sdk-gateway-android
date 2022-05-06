package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.databinding.FragmentSelectOriginImportProofBinding
import com.paylivre.sdk.gateway.android.domain.model.OriginTypeInsertProof
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel

class SelectOriginImportProofFragment : DialogFragment() {
    private var _binding: FragmentSelectOriginImportProofBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set Log Analytics
        LogEvents.setLogEventAnalytics("ModalSelectOriginImportProof")

        _binding = FragmentSelectOriginImportProofBinding
            .inflate(
                inflater,
                container,
                false
            )
        val root: View = binding.root


        binding.btnOpenGallery.setOnClickListener {
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Btn_ChooseInGallery")

            mainViewModel.setOriginTypeInsertProof(OriginTypeInsertProof.GALLERY)
            dismiss()
        }

        binding.btnOpenCamera.setOnClickListener {
            //Set Log Analytics
            LogEvents.setLogEventAnalytics("Btn_OpenCamera")

            mainViewModel.setOriginTypeInsertProof(OriginTypeInsertProof.CAMERA)
            dismiss()
        }

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