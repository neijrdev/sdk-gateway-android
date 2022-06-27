package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentButtonBankInfoRowBinding
import com.paylivre.sdk.gateway.android.utils.DataMakeBold
import com.paylivre.sdk.gateway.android.utils.makeBold


class ButtonBankInfoRow : Fragment() {
    private var _binding: FragmentButtonBankInfoRowBinding? = null
    private val binding get() = _binding!!
    private var label: String? = null
    private var value: String? = null
    private var toast: Toast? = null

    private fun setDividerLineEndVisibility(isShow: Boolean) {
        if (!isShow) {
            binding.dividerLineEnd.visibility = View.GONE
        } else binding.dividerLineEnd.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentButtonBankInfoRowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bundle = this.arguments
        if (bundle != null) {
            label = bundle.getString("label")
            value = bundle.getString("value")
            setDividerLineEndVisibility(bundle.getBoolean("isShowDividerLine", true))
        }

        if (label != null && value != null) {
            val textValue = "$label: $value"
            binding.textViewBankDataInfo.text = textValue
            binding.textViewBankDataInfo.makeBold(DataMakeBold("$label: ", textValue))
            binding.containerButton.setOnClickListener { copyToClipboard(value!!, label!!) }
        }


        return root
    }

    private fun copyToClipboard(text: String, label: String) {
        try {

            toast?.cancel()

            val myClipboard: ClipboardManager? =
                context?.getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager?

            val myClip: ClipData = ClipData.newPlainText("text", text)
            myClipboard!!.setPrimaryClip(myClip)

            toast = Toast.makeText(
                context,
                getString(R.string.message_copied_value, label),
                Toast.LENGTH_SHORT
            )

            toast?.show()
        } catch (e: Exception) {
            println(e)
        }
    }
}