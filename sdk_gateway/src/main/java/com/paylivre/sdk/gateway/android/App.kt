package com.paylivre.sdk.gateway.android

import android.app.Application
import android.content.Context
import android.util.Log
import com.amplitude.api.Amplitude
import com.paylivre.sdk.gateway.android.di.appModule
import io.sentry.Scope
import io.sentry.Sentry
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level


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
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(appModule)
        }

        Amplitude.getInstance().initialize(this, BuildConfig.AMPLITUDE_API_KEY)
            .enableForegroundTracking(this);

        SentryAndroid.init(this) { options ->
            options.environment =  BuildConfig.BUILD_TYPE
            options.dsn = BuildConfig.SENTRY_API_KEY_URL
            options.addIntegration(
                FragmentLifecycleIntegration(
                    this,
                    enableFragmentLifecycleBreadcrumbs = true, // enabled by default
                    enableAutoFragmentLifecycleTracing = true  // disabled by default
                )
            )
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}