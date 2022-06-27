package com.example.paylivre.sdk.gateway

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.paylivre.sdk.gateway.databinding.ActivityInsertUrlBinding
import com.example.paylivre.sdk.gateway.utils.setTextThemeStatusBar
import com.google.android.material.textfield.TextInputEditText
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import java.lang.Exception
import com.paylivre.sdk.gateway.android.BuildConfig


class InsertUrlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertUrlBinding

    lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertUrlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTextThemeStatusBar(this, "Light")
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "SDK Gateway Paylivre - v${BuildConfig.VERSION_NAME}"
        }

        fun View.hideKeyboard() {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(applicationWindowToken, 0)
        }

        fun onBlurInput() {
            binding.editUrl.clearFocus()
        }

        fun pasteFromClipboard() {
            try {
                val myClipboard: ClipboardManager? = this?.getSystemService(CLIPBOARD_SERVICE)
                        as ClipboardManager?

                val item = myClipboard!!.primaryClip?.getItemAt(0)

                val pasteItem: TextInputEditText = binding.editUrl

                pasteItem.isEnabled = when {
                    !myClipboard!!.hasPrimaryClip() -> {
                        false
                    }
                    !(myClipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN))!! -> {
                        // This disables the paste menu item, since the clipboard has data but it is not plain text
                        false
                    }
                    else -> {
                        // This enables the paste menu item, since the clipboard contains plain text.
                        binding.editUrl.setText(item?.text.toString())
                        true
                    }
                }

                toast =
                    Toast.makeText(
                        this,
                        "Copied URL",
                        Toast.LENGTH_SHORT
                    )
                toast.show();
            } catch (e: Exception) {
                println(e)
            }
        }

        fun getLanguageChecked(): String? {
            return when {
                binding.checkLangPt.isChecked -> Languages.PT.toString()
                    .lowercase()
                binding.checkLangEn.isChecked -> Languages.EN.toString()
                    .lowercase()
                binding.checkLangEs.isChecked -> Languages.ES.toString()
                    .lowercase()
                else -> null
            }
        }

        fun startCheckoutByUrl(url: String?) {
            if (url.isNullOrEmpty()) {
                toast =
                    Toast.makeText(
                        this,
                        "Enter a valid URL!",
                        Toast.LENGTH_SHORT
                    )
                toast.show()

            } else {
                val intent = Intent(this, PreviewDataActivity::class.java).apply {
                    val typeStartCheckout = TypesStartCheckout.BY_URL.code
                    putExtra("type_start_checkout", typeStartCheckout)
                    putExtra("use_logo_url", 1)
                    putExtra("language", getLanguageChecked())
                    putExtra("url", url)
                }

                startActivity(intent)
            }

        }

        binding.container.setOnClickListener {
            onBlurInput()
            it.hideKeyboard()
        }

        binding.buttonPasteUrl.setOnClickListener {
            pasteFromClipboard()
            onBlurInput()
            it.hideKeyboard()
        }

        binding.buttonClearUrl.setOnClickListener {
            binding.editUrl.setText("")
            onBlurInput()
            it.hideKeyboard()
        }

        binding.buttonStartCheckoutByURl.setOnClickListener {
            startCheckoutByUrl(binding.editUrl.text.toString())
            onBlurInput()
            it.hideKeyboard()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }


}