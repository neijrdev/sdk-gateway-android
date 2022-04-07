package com.example.paylivre.sdk.gateway

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.paylivre.sdk.gateway.data.DataStartCheckoutByParams
import com.example.paylivre.sdk.gateway.databinding.ActivityPreviewDataBinding
import com.example.paylivre.sdk.gateway.utils.DataMakeBold
import com.example.paylivre.sdk.gateway.utils.getSdkGatewayExtraData
import com.example.paylivre.sdk.gateway.utils.makeBold
import com.example.paylivre.sdk.gateway.utils.setValueTextViewWithLabelBold
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.StartCheckout
import com.paylivre.sdk.gateway.android.StartCheckoutByURL
import com.paylivre.sdk.gateway.android.domain.model.extractDataFromUrl
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import java.lang.Exception
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

const val SDK_GATEWAY_PAYLIVRE_ACTIVITY_REQUEST_CODE: Int = 0
const val MERCHANT_LOGO_RES_ID: Int = R.drawable.logo_jackpot

class PreviewDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set Theme do SDK Gateway Paylivre
        setTheme(R.style.Theme_SDKGatewayAndroid)

        val actionBar = supportActionBar

        val typeStartCheckout: Int = intent.getIntExtra("type_start_checkout", -1)

        val dataStartCheckoutByParamsString =
            intent.getStringExtra("data_start_checkout_by_params").toString()

        val dataStartCheckoutByParams = Gson().fromJson(
            dataStartCheckoutByParamsString,
            DataStartCheckoutByParams::class.java
        )


        val url = intent.getStringExtra("url").toString()
        val useLogoUrl = intent.getIntExtra("use_logo_url", -1)

        val language = intent.getStringExtra("language")


        //Show Configuration Other Parameters
        if (language != null) {
            setValueTextViewWithLabelBold(binding.txtLanguage, "language", language)
            binding.txtLabelExtraParamsConfig.visibility = View.VISIBLE
        } else {
            binding.txtLanguage.visibility = View.GONE
        }

        fun checkValidDrawableId(LogoResId: Int): Boolean {
            return try {
                ContextCompat.getDrawable(this, LogoResId);
                true
            } catch (e: Exception) {
                false
            }
        }

        val urlDataExtracted = extractDataFromUrl(url, returnNullIfParamNotExist = true)

        fun setLogoByLogoResId() {
            if (checkValidDrawableId(MERCHANT_LOGO_RES_ID)) {
                binding.txtLabelExtraParamsConfig.visibility = View.VISIBLE
                binding.textLabelLogoExample.visibility = View.VISIBLE
                binding.textLabelLogoExample.text = "Logo Merchant (Preview)"
                binding.previewLogoExampleMerchant.visibility = View.VISIBLE
                binding.previewLogoExampleMerchant.setImageResource(MERCHANT_LOGO_RES_ID)
            } else {
                binding.textLabelLogoExample.visibility = View.GONE
                binding.previewLogoExampleMerchant.visibility = View.GONE
                binding.previewLogoExampleMerchant.setImageResource(0)
            }
        }

        //Logo Merchant
        if (useLogoUrl == 1) {
            try {
                val sizeWidth = dpToPx(resources, 150f).roundToInt()
                val sizeHeight = dpToPx(resources, 40f).roundToInt()

                val picasso = Picasso.Builder(this)
                    .listener { picasso, uri, exception ->
                        println("Exception Picasso: " + exception.stackTraceToString())
                        binding.textLabelLogoExample.visibility = View.GONE
                        binding.previewLogoExampleMerchant.visibility = View.GONE
                        binding.textLabelLogoExample.text = "Logo Merchant (Preview)"
                        setLogoByLogoResId()
                    }
                    .build()

                var logoUrlToLoad: String? =
                    if (typeStartCheckout == TypesStartCheckout.BY_PARAMS.code) {
                        dataStartCheckoutByParams.logo_url
                    } else {
                        urlDataExtracted.logo_url
                    }

                picasso
                    .load(logoUrlToLoad)
                    .resize(sizeWidth, sizeHeight)
                    .centerInside()
                    .into(binding.previewLogoExampleMerchant)

                binding.textLabelLogoExample.visibility = View.VISIBLE
                binding.textLabelLogoExample.text = "logo_url (Preview)"
                binding.previewLogoExampleMerchant.visibility = View.VISIBLE
                binding.txtLabelExtraParamsConfig.visibility = View.VISIBLE

            } catch (e: Exception) {
                println("Catch Picasso:" + e.stackTraceToString())
                binding.textLabelLogoExample.visibility = View.GONE
                binding.previewLogoExampleMerchant.visibility = View.GONE
                setLogoByLogoResId()
            }
        } else {
            setLogoByLogoResId()
        }



        when (typeStartCheckout) {
            TypesStartCheckout.BY_PARAMS.code -> {
                binding.buttonStartCheckout.text = getString(R.string.start_checkout_by_params)
                actionBar?.title = "Preview Checkout Params - ${BuildConfig.VERSION_NAME}"

                setValueTextViewWithLabelBold(
                    binding.txtGatewayToken,
                    "gateway_token",
                    dataStartCheckoutByParams?.gateway_token.toString()
                )

                //Set Data Preview
                setValueTextViewWithLabelBold(
                    binding.txtBaseUrl,
                    "base_url",
                    dataStartCheckoutByParams?.base_url.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtMerchantTransactionId,
                    "merchant_transaction_id",
                    dataStartCheckoutByParams?.merchant_transaction_id.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtMerchantId,
                    "merchant_id",
                    dataStartCheckoutByParams?.merchant_id.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtOperation,
                    "operation",
                    dataStartCheckoutByParams?.operation.toString()
                )

                if( dataStartCheckoutByParams?.email_address!=null){
                    setValueTextViewWithLabelBold(
                        binding.txtEmail,
                        "email",
                        dataStartCheckoutByParams?.email_address.toString()
                    )
                }

                if( dataStartCheckoutByParams?.document!=null){
                    setValueTextViewWithLabelBold(
                        binding.txtDocument,
                        "document",
                        dataStartCheckoutByParams?.document.toString()
                    )
                }

                if(dataStartCheckoutByParams?.pix_key_type != null) {
                    setValueTextViewWithLabelBold(
                        binding.txtPixKeyType,
                        "pix_key_type",
                        dataStartCheckoutByParams?.pix_key_type.toString()
                    )
                }

                if(dataStartCheckoutByParams?.pix_key != null) {
                    setValueTextViewWithLabelBold(
                        binding.txtPixKey,
                        "pix_key",
                        dataStartCheckoutByParams?.pix_key.toString()
                    )

                }

                setValueTextViewWithLabelBold(
                    binding.txtAmount,
                    "amount",
                    dataStartCheckoutByParams?.amount.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtCurrency,
                    "currency",
                    dataStartCheckoutByParams?.currency.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtType,
                    "type",
                    dataStartCheckoutByParams?.type.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtAccountId,
                    "account_id",
                    dataStartCheckoutByParams?.account_id.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtAutoApprove,
                    "auto_approve",
                    dataStartCheckoutByParams?.auto_approve.toString()
                )

                setValueTextViewWithLabelBold(
                    binding.txtCallbackUrl,
                    "callback_url",
                    dataStartCheckoutByParams?.callback_url.toString()
                )

                if (useLogoUrl == 1) {
                    setValueTextViewWithLabelBold(
                        binding.txtLogoUrl,
                        "logo_url",
                        dataStartCheckoutByParams?.logo_url.toString()
                    )
                } else {
                    binding.txtLogoUrl.visibility = View.GONE
                }


                binding.txtRedirectUrl.visibility = View.GONE
                binding.txtSignature.visibility = View.GONE
                binding.txtUrl.visibility = View.GONE
                binding.txtLabelUrl.visibility = View.GONE
                binding.buttonOpenUrlInBrowser.visibility = View.GONE
                binding.buttonCopyUrl.visibility = View.GONE
                binding.buttonOpenUrlInWebView.visibility = View.GONE
            }

            TypesStartCheckout.BY_URL.code -> {
                binding.buttonStartCheckout.text = getString(R.string.start_checkout_by_url)
                actionBar?.title = "Preview Checkout By Url - ${BuildConfig.VERSION_NAME}"

                val urlFormatted = url.replace("?", "?\n")
                    .replace("&", "&\n")

                binding.txtUrl.text = urlFormatted


                binding.txtUrl.visibility = View.VISIBLE
                binding.containerDataParams.visibility = View.GONE

                binding.txtUrl.makeBold(
                    DataMakeBold(
                        urlDataExtracted.base_url,
                        "",
                    ),
                    DataMakeBold(
                        "merchant_transaction_id=",
                        urlDataExtracted.merchant_transaction_id,
                    ),
                    DataMakeBold(
                        "merchant_id=",
                        urlDataExtracted.merchant_id.toString(),
                    ),
                    DataMakeBold(
                        "operation=",
                        urlDataExtracted.operation.toString(),
                    ),
                    DataMakeBold(
                        "email=",
                        urlDataExtracted.email_address.toString(),
                    ),
                    DataMakeBold(
                        "document_number=",
                        urlDataExtracted.document.toString(),
                    ),
                    DataMakeBold(
                        "amount=",
                        urlDataExtracted.amount.toString(),
                    ),
                    DataMakeBold(
                        "currency=",
                        urlDataExtracted.currency,
                    ),
                    DataMakeBold(
                        "type=",
                        urlDataExtracted.type.toString(),
                    ),
                    DataMakeBold(
                        "account_id=",
                        urlDataExtracted.account_id,
                    ),
                    DataMakeBold(
                        "callback_url=",
                        urlDataExtracted.callback_url,
                    ),
                    DataMakeBold(
                        "auto_approve=",
                        urlDataExtracted.auto_approve.toString(),
                    ),
                    DataMakeBold(
                        "redirect_url=",
                        urlDataExtracted.redirect_url.toString(),
                    ),
                    DataMakeBold(
                        "logo_url=",
                        urlDataExtracted.logo_url.toString(),
                    ),
                    DataMakeBold(
                        "signature=",
                        urlDataExtracted.signature.toString(),
                    ),
                    DataMakeBold(
                        "pix_key_type=",
                        urlDataExtracted.pix_key_type.toString(),
                    ),
                    DataMakeBold(
                        "pix_key=",
                        urlDataExtracted.pix_key?:"",
                    ),
                )

            }
        }

        //Get params result of SDK Gateway
        val checkoutRegisterForResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->

                val resultCodeValue =
                    if (result.resultCode == Activity.RESULT_OK) "Activity.RESULT_OK"
                    else "Not Activity.RESULT_OK"

                val data = result.data
                val resultExtraData = getSdkGatewayExtraData(resultCodeValue, data)

                val intent = Intent(this, RegisterForActivityResult::class.java).apply {
                    val dataRegisterForActivityResult = Gson().toJson(resultExtraData)
                    putExtra(
                        "data_register_for_activity_result",
                        dataRegisterForActivityResult
                    )
                }
                startActivity(intent)
                finish()
            }


        fun startCheckoutByUrl() {
            val checkout = StartCheckoutByURL.Builder(
//                request_code = SDK_GATEWAY_PAYLIVRE_ACTIVITY_REQUEST_CODE,
                url = url,
            ).build()

            //Register callback for activity result
            checkout.setRegisterForResult(checkoutRegisterForResult)

            //set language
            language?.let { checkout.setLanguage(it) }

            if (useLogoUrl != 1) {
                //set logo merchant
                checkout.setLogoResId(MERCHANT_LOGO_RES_ID)
            }

            checkout.startPayment(this)
        }


        fun startCheckoutByParams() {
            val checkout = StartCheckout.Builder(
//                request_code = SDK_GATEWAY_PAYLIVRE_ACTIVITY_REQUEST_CODE,
                merchant_id = dataStartCheckoutByParams.merchant_id,
                gateway_token = dataStartCheckoutByParams.gateway_token,
                operation = dataStartCheckoutByParams.operation,
                merchant_transaction_id = dataStartCheckoutByParams.merchant_transaction_id,
                amount = dataStartCheckoutByParams.amount,
                currency = dataStartCheckoutByParams.currency,
                type = dataStartCheckoutByParams.type,
                account_id = dataStartCheckoutByParams.account_id,
                callback_url = dataStartCheckoutByParams.callback_url,
                base_url = dataStartCheckoutByParams.base_url,
                email_address = dataStartCheckoutByParams.email_address, //Optional
                document = dataStartCheckoutByParams.document, //Optional
                auto_approve = dataStartCheckoutByParams.auto_approve,
                logo_url = dataStartCheckoutByParams.logo_url,
                pix_key_type = dataStartCheckoutByParams.pix_key_type,
                pix_key = dataStartCheckoutByParams.pix_key
            ).build()

            //Register callback for activity result
            checkout.setRegisterForResult(checkoutRegisterForResult)

            //set language
            language?.let { checkout.setLanguage(it) }

            if (useLogoUrl != 1) {
                //set logo merchant
                checkout.setLogoResId(MERCHANT_LOGO_RES_ID)
            }

            checkout.startPayment(this)

        }

        binding.buttonBackToForm.setOnClickListener {
            finish()
        }

        binding.buttonStartCheckout.setOnClickListener {
            if (typeStartCheckout == TypesStartCheckout.BY_PARAMS.code) {
                startCheckoutByParams()
            } else {
                startCheckoutByUrl()
            }
        }

        binding.buttonOpenUrlInBrowser.setOnClickListener {
            openUrl(url)
        }

        binding.buttonOpenUrlInWebView.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra("url", url)
            }
            startActivity(intent)
            //Return to previous activity
            finish()
        }

        binding.buttonCopyUrl.setOnClickListener {
            copyToClipboard(url)
        }

    }


    private fun copyToClipboard(text: String) {
        lateinit var toast: Toast

        try {
            val myClipboard: ClipboardManager? = this?.getSystemService(CLIPBOARD_SERVICE)
                    as ClipboardManager?

            val myClip: ClipData = ClipData.newPlainText("text", text)
            myClipboard!!.setPrimaryClip(myClip)

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


    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
        //Return to previous activity
        finish()
    }

//    @Suppress("DEPRECATION")
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        //to remove DEPRECATION -> implement registerForActivityResult
//        super.onActivityResult(requestCode, resultCode, data)
//        // Check that it is the SecondActivity with an OK result
//        if (requestCode == SDK_GATEWAY_PAYLIVRE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                println("onActivityResult -> Activity.RESULT_OK")
//                // Get String data from Intent
////                val returnString = data!!.getStringExtra("keyName")
//            } else {
//                println("onActivityResult-> Activity NOT RESULT_OK ->")
//            }
//        }
//    }

}