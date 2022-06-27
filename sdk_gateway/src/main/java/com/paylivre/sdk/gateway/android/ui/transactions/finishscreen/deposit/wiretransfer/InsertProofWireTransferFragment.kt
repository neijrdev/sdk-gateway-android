package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentInsertProofWireTransferBinding
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.paylivre.sdk.gateway.android.utils.getColorById
import kotlin.math.roundToInt
import com.github.dhaval2404.imagepicker.ImagePicker
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.domain.model.OriginTypeInsertProof
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class InsertProofWireTransferFragment : Fragment() {
    private var _binding: FragmentInsertProofWireTransferBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val binding get() = _binding!!
    private var uriProofFile: Uri? = null
    private var tokenTransferTransaction: String? = null
    private var orderIdTransferTransaction: Int? = null
    private val logEventsService : LogEventsService by inject()

    fun handleImagePickerSuccess(data: Intent?) {
        //Image Uri will not be null for RESULT_OK
        val uri: Uri = data?.data!!

        // Use Uri object instead of File to avoid storage permissions
        binding.imgPreviewProof.visibility = View.VISIBLE
        binding.imgPreviewProof.setImageURI(uri)
        binding.textNameSelectedFile.visibility = View.GONE
        binding.txtChooseFile.text = getString(R.string.change_selected_file)
        binding.btnSubmit.isEnabled = true
        mainViewModel.setProofImageUri(uri)
        uriProofFile = uri

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("EnteredProofImage")
    }

    fun handleImagePickerError(data: Intent?) {
        //Set Log Analytics
        logEventsService.setLogEventAnalytics("ErrorEnteredProofImage")
        println("data: ${ImagePicker.getError(data)}")
    }

    fun handleImagePickerCancelled() {
        //Set Log Analytics
        logEventsService.setLogEventAnalytics("CancelledEnteredProofImage")
        println("Task Cancelled")
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> handleImagePickerSuccess(result.data)
                ImagePicker.RESULT_ERROR -> handleImagePickerError(result.data)
                else -> handleImagePickerCancelled()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentInsertProofWireTransferBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fun dpToPxLocalInt(value: Float): Int {
            return dpToPx(resources, value).roundToInt()
        }

        val widthDeviceDP = resources.displayMetrics.widthPixels
        val marginsHorizontalDP = dpToPxLocalInt(80f)
        val widthDeviceDPMinusMargin = widthDeviceDP - marginsHorizontalDP
        val widthButton = widthDeviceDPMinusMargin * 0.99


        fun setActivateBorderButton(isActive: Boolean) {
            val bgActive = R.drawable.button_outline_with_ripple_primary
            val bgInactive = R.drawable.button_outline_with_ripple_secondary
            val bgCurrent = if (isActive) bgActive else bgInactive
            val currentColorContent = if (isActive) {
                getColorById(requireContext(), R.color.primary_color_paylivre_sdk)
            } else {
                getColorById(requireContext(), R.color.white_color_paylivre_sdk)
            }

            binding.txtChooseFile.setTextColor(currentColorContent)
            binding.iconChooseFile.setColorFilter(currentColorContent)

            binding.btnChooseFile.background = ResourcesCompat.getDrawable(
                resources,
                bgCurrent,
                null
            );

            //Parametros Botão
            val params = binding.btnChooseFile.layoutParams
            params.width = widthButton.toInt()
            binding.btnChooseFile.layoutParams = params

        }

        mainViewModel.proof_image_uri.observe(viewLifecycleOwner) {
            if (it != null) {
                setActivateBorderButton(false)
            } else {
                setActivateBorderButton(true)
            }
        }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            if (it != null) {
                tokenTransferTransaction = it.data?.verification_token
                orderIdTransferTransaction = it.data?.order_id
            }
        }

        setActivateBorderButton(true)

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.origin_type_insert_proof.observe(viewLifecycleOwner) {
            when (it) {
                OriginTypeInsertProof.GALLERY -> {
                    ImagePicker.with(this)
                        .galleryMimeTypes(  //Exclude gif images
                            mimeTypes = arrayOf(
                                "image/png",
                                "image/jpg",
                                "image/jpeg"
                            )
                        )
                        .galleryOnly()
                        .crop()
                        .compress(2048)
                        .maxResultSize(1080, 1920)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }

                OriginTypeInsertProof.CAMERA -> {
                    ImagePicker.with(this)
                        .cameraOnly()
                        .crop()
                        .compress(2048)
                        .maxResultSize(1080, 1920)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }

                }
            }
        }


        binding.btnChooseFile.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_ChooseFile")
            openModalSelectTypeInsertProof(true)
        }

        binding.btnSubmit.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_SubmitTransferProof")

            val file = File(uriProofFile?.path)

            mainViewModel.insertTransferProof(
                InsertTransferProofDataRequest(
                    file = file!!,
                    order_id = orderIdTransferTransaction!!,
                    token = tokenTransferTransaction!!
                )
            )
        }
    }

//    //Recebe uri da imagem escolhida
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (resultCode) {
//            Activity.RESULT_OK -> {
//                //Image Uri will not be null for RESULT_OK
//                val uri: Uri = data?.data!!
//
//                // Use Uri object instead of File to avoid storage permissions
//                binding.imgPreviewProof.visibility = View.VISIBLE
//                binding.imgPreviewProof.setImageURI(uri)
//                binding.textNameSelectedFile.visibility = View.GONE
//                binding.txtChooseFile.text = getString(R.string.change_selected_file)
//                binding.btnSubmit.isEnabled = true
//                mainViewModel.setProofImageUri(uri)
//                uriProofFile = uri
//
//            }
//            ImagePicker.RESULT_ERROR -> {
//                println("data: ${ImagePicker.getError(data)}")
//            }
//            else -> {
//                println("Task Cancelled")
//            }
//        }
//    }


    private fun openModalSelectTypeInsertProof(isShow: Boolean) {
        val dialog = SelectOriginImportProofFragment()
        if (isShow) {
            dialog.show(childFragmentManager, dialog.tag)
        }
    }

}