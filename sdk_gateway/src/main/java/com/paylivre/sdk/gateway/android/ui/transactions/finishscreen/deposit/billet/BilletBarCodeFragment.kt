package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.billet

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentBilletBarCodeBinding
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.copyToClipboard
import org.koin.android.ext.android.inject
import java.lang.Exception


fun generateBarCodeBitmap(code: String?, imageView: ImageView) {
    try {
        //Min 0 and Max 80 characters
        if (!code.isNullOrEmpty() && code?.length!! < 80) {
            val writer = Code128Writer()
            val bitMatrix = writer.encode(
                code,
                BarcodeFormat.CODE_128, 580, 120,
            )
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            imageView.setImageBitmap(bitmap)
        }
    } catch (e: Exception) {
        println(e)
    }
}

class BilletBarCodeFragment : Fragment() {
    private var _binding: FragmentBilletBarCodeBinding? = null
    private val binding get() = _binding!!
    private val logEventsService : LogEventsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var receivableUrl: String? = null
        var billetDigitableLine: String? = ""

        _binding = FragmentBilletBarCodeBinding.inflate(inflater, container, false)

        //receive parameters
        val bundle = this.arguments
        if (bundle != null) {
            receivableUrl = bundle.getString("receivable_url")
            billetDigitableLine = bundle.getString("billet_digitable_line")
        }

        if (!billetDigitableLine.isNullOrEmpty()) {
            binding.txtCode.text = billetDigitableLine

            //Generate barcode image
            generateBarCodeBitmap(billetDigitableLine, binding.barCodeBillet)
        }

        binding.btnCopyCodeBillet.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_CopyCodeBillet")

            copyToClipboard(
                requireContext(),
                billetDigitableLine,
                getString(R.string.msg_copied_code_billet),
                getString(R.string.msg_error_copy_code_billet)
            )
        }

        binding.btnOpenBillet.setOnClickListener {
            //Set Log Analytics
            logEventsService.setLogEventAnalytics("Btn_OpenBillet")
            openUrl(receivableUrl)
        }

        return binding.root
    }

    private fun openUrl(url: String?) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (e: Exception) {
            logEventsService.setLogEventAnalytics("Error_OpenBillet")

            var toast: Toast = Toast.makeText(
                context,
                getString(R.string.error_open_billet),
                Toast.LENGTH_SHORT
            )
            toast.show();
        }

    }
}