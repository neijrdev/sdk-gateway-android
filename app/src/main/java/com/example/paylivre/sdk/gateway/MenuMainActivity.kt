package com.example.paylivre.sdk.gateway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.paylivre.sdk.gateway.databinding.ActivityMenuMainBinding
import com.paylivre.sdk.gateway.android.BuildConfig

class MenuMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BuildConfig.VERSION_NAME


        //Set Theme do SDK Gateway Paylivre
        setTheme(R.style.Theme_SDKGatewayAndroid)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "SDK Gateway Paylivre - ${BuildConfig.VERSION_NAME}"
        }

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