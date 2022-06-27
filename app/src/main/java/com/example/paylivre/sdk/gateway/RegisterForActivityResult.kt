package com.example.paylivre.sdk.gateway

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.paylivre.sdk.gateway.databinding.ActivityRegisterForResultBinding
import com.example.paylivre.sdk.gateway.utils.RegisterForActivityResultData
import com.example.paylivre.sdk.gateway.utils.setTextThemeStatusBar
import com.example.paylivre.sdk.gateway.utils.setValueTextViewWithLabelBold
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.domain.model.Type

class RegisterForActivityResult : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterForResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterForResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTextThemeStatusBar(this, "Light")
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "SDK Gateway Paylivre - v${BuildConfig.VERSION_NAME}"
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        val dataRegisterForActivityResultString =
            intent.getStringExtra("data_register_for_activity_result").toString()

        val dataRegisterForActivityResult = Gson().fromJson(
            dataRegisterForActivityResultString,
            RegisterForActivityResultData::class.java
        )

        binding.activityResultValue.text = dataRegisterForActivityResult.registerForActivityResult

        val selectedType = dataRegisterForActivityResult.selected_type?.let { Type.fromInt(it) }
        setValueTextViewWithLabelBold(
            binding.selectedTypeValue,
            "selected_type",
            "${selectedType?.code} ${selectedType?.name ?: ""}"
        )

        binding.isGeneratedTransactionValue.text =
            "${dataRegisterForActivityResult.is_generated_transaction}"

        setValueTextViewWithLabelBold(
            binding.orderIdValue,
            " - order_id",
            "${dataRegisterForActivityResult.order_id}"
        )

        setValueTextViewWithLabelBold(
            binding.orderTypeIdValue,
            " - order_type_id",
            "${dataRegisterForActivityResult.order_type_id}" +
                    " ${dataRegisterForActivityResult.order_type_name}"
        )

        setValueTextViewWithLabelBold(
            binding.orderStatusIdValue,
            " - order_status_id",
            "${dataRegisterForActivityResult.order_status_id}" +
                    " ${dataRegisterForActivityResult.order_status_name}"
        )

        setValueTextViewWithLabelBold(
            binding.transactionIdValue,
            " - transaction_id",
            "${dataRegisterForActivityResult.transaction_id}"
        )

        setValueTextViewWithLabelBold(
            binding.transactionStatusIdValue,
            " - transaction_status_id",
            "${dataRegisterForActivityResult.transaction_status_id}" +
                    " ${dataRegisterForActivityResult.transaction_status_name}"
        )

        setValueTextViewWithLabelBold(
            binding.depositIdValue,
            " - deposit_id",
            "${dataRegisterForActivityResult.deposit_id}"
        )

        setValueTextViewWithLabelBold(
            binding.depositTypeIdValue,
            " - deposit_type_id",
            "${dataRegisterForActivityResult.deposit_type_id}" +
                    " ${dataRegisterForActivityResult.deposit_type_name}"
        )

        setValueTextViewWithLabelBold(
            binding.depositStatusIdValue,
            " - deposit_status_id",
            "${dataRegisterForActivityResult.deposit_status_id}" +
                    " ${dataRegisterForActivityResult.deposit_status_name}"
        )

        setValueTextViewWithLabelBold(
            binding.withdrawalIdValue,
            " - withdrawal_id",
            "${dataRegisterForActivityResult.withdrawal_id}"
        )

        setValueTextViewWithLabelBold(
            binding.withdrawalTypeIdValue,
            " - withdrawal_type_id",
            "${dataRegisterForActivityResult.withdrawal_type_id}" +
                    " ${dataRegisterForActivityResult.withdrawal_type_name}"
        )

        setValueTextViewWithLabelBold(
            binding.withdrawalStatusIdValue,
            " - withdrawal_status_id",
            "${dataRegisterForActivityResult.withdrawal_status_id}" +
                    " ${dataRegisterForActivityResult.withdrawal_status_name}"
        )

        binding.isErrorTransactionValue.text =
            "${dataRegisterForActivityResult.is_error_transaction}"

        setValueTextViewWithLabelBold(
            binding.errorTransactionCodeValue,
            " - error_transaction_code",
            dataRegisterForActivityResult.error_transaction_code
        )

        setValueTextViewWithLabelBold(
            binding.errorTransactionMessageValue,
            " - error_transaction_message",
            dataRegisterForActivityResult.error_transaction_message
        )

        binding.isUserCompletedTransactionValue.text =
            "${dataRegisterForActivityResult.is_user_completed_transaction}"

        setValueTextViewWithLabelBold(
            binding.actionNotCompletedCodeValue,
            " - action_not_completed_code",
            dataRegisterForActivityResult.action_not_completed_code
        )

        setValueTextViewWithLabelBold(
            binding.actionNotCompletedMessageValue,
            " - action_not_completed_message",
            dataRegisterForActivityResult.action_not_completed_message
        )

        setValueTextViewWithLabelBold(
            binding.errorNotCompletedMessageValue,
            " - error_completed_transaction_message",
            dataRegisterForActivityResult.error_completed_transaction_message
        )
    }
}