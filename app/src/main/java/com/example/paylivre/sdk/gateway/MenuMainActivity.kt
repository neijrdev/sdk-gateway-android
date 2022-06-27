package com.example.paylivre.sdk.gateway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.paylivre.sdk.gateway.databinding.ActivityMenuMainBinding
import com.example.paylivre.sdk.gateway.utils.setTextThemeStatusBar
import com.paylivre.sdk.gateway.android.BuildConfig

class MenuMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "SDK Gateway Paylivre - v${BuildConfig.VERSION_NAME}"
        }

        setTextThemeStatusBar(this, "Light")
        fun goToFormGenerateData(){
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        fun goToFormInsertUrl(){
            val intent = Intent(this, InsertUrlActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGoFormGenerateData.setOnClickListener {
            goToFormGenerateData()
        }

        binding.buttonGoFormInsertUrl.setOnClickListener {
            goToFormInsertUrl()
        }

    }




}