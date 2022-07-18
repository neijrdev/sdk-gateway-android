package com.paylivre.sdk.gateway.android.services.postdelayed

interface PostDelayedService {
    fun postDelayed(runnable: Runnable, timerInterval: Long )
}