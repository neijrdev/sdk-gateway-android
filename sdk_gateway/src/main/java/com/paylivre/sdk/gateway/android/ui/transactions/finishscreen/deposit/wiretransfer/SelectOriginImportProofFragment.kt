package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.paylivre.sdk.gateway.android.databinding.FragmentSelectOriginImportProofBinding
import com.paylivre.sdk.gateway.android.domain.model.OriginTypeInsertProof
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectOriginImportProofFragment : DialogFragment() {
    private var _binding: FragmentSelectOriginImportProofBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val binding get() = _binding!!
    private val logEventsService : LogEventsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("ModalSelectOriginImportProof")

        _binding = FragmentSelectOriginImportProofBinding
            .inflate(
                inflater,
                container,
                false
            )
        val root: View = binding.root


        binding.btnOpenGallery.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_ChooseInGallery")

            mainViewModel.setOriginTypeInsertProof(OriginTypeInsertProof.GALLERY)
            dismiss()
        }

        binding.btnOpenCamera.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_OpenCamera")

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