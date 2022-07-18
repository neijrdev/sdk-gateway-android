package com.paylivre.sdk.gateway.android.services.postdelayed

import android.os.Handler
import android.os.Looper

class PostDelayedImpl : PostDelayedService {
    override fun postDelayed(runnable: Runnable, timerInterval: Long ){
        Handler(Looper.getMainLooper()).postDelayed(runnable, timerInterval)
    }
}