package com.paylivre.sdk.gateway.android

import android.app.Application
import android.content.Context
import com.paylivre.sdk.gateway.android.data.api.services
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import com.amplitude.api.Amplitude;

private const val KEY_PREFERENCES = "paylivre_sdk_gateway_pref"
private const val KEY_TOKEN = "paylivre_sdk_gateway_host_api"


class App : Application() {

    companion object {
        private lateinit var instance: App

        private val preferences by lazy {
            instance.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)
        }

        fun setHostAPI(hostAPI: String) {
            preferences.edit()
                .putString(KEY_TOKEN, hostAPI)
                .apply()
        }

        fun getHostAPI() = preferences.getString(KEY_TOKEN, null)

        val apiService by lazy { services() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Amplitude.getInstance().initialize(this, "846887b65f31981f627eb15881f40baa")
            .enableForegroundTracking(this);

        SentryAndroid.init(this) { options ->
            options.addIntegration(
                FragmentLifecycleIntegration(
                    this,
                    enableFragmentLifecycleBreadcrumbs = true, // enabled by default
                    enableAutoFragmentLifecycleTracing = true  // disabled by default
                )
            )
        }
    }

}