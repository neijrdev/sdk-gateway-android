package com.example.paylivre.sdk.gateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.paylivre.sdk.gateway.android.BuildConfig
import com.example.paylivre.sdk.gateway.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set Theme do SDK Gateway Paylivre
        setTheme(R.style.Theme_SDKGatewayAndroid)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "SDK Gateway (Webview) - ${BuildConfig.VERSION_NAME}"
        }

        val myWebView: WebView = binding.webview

        myWebView.settings.loadWithOverviewMode = true
        myWebView.settings.useWideViewPort = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.javaScriptCanOpenWindowsAutomatically = true

        val url = intent.getStringExtra("url").toString()

        if (url != null) {
            myWebView.loadUrl(url)
        } else {
            myWebView.loadUrl("https://www.google.com")
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        myWebView.webViewClient = WebViewClient()
    }
}