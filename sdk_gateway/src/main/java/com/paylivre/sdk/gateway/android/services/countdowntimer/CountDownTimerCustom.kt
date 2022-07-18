package com.paylivre.sdk.gateway.android.services.countdowntimer

import android.os.CountDownTimer


interface CountDownTimerService {
    var countDownTimer: CountDownTimer
    fun startTimer(
        timerFinalCheckInMillis: Long,
        timerIntervalInMillis: Long,
        onTickAction: (Long) -> Unit,
        onFinishAction: () -> Unit,
    )

    fun cancel()
}

class CountDownTimerServiceImpl : CountDownTimerService {
    override lateinit var countDownTimer: CountDownTimer

    override fun startTimer(
        timerFinalCheckInMillis: Long,
        timerIntervalInMillis: Long,
        onTickAction: (Long) -> Unit,
        onFinishAction: () -> Unit,
    ) {
        countDownTimer = object : CountDownTimer(timerFinalCheckInMillis,
            timerIntervalInMillis) {
            override fun onTick(millisUntilFinished: Long) {
                onTickAction(millisUntilFinished)
            }

            override fun onFinish() {
                onFinishAction()
            }
        }

        countDownTimer.start()
    }

    override fun cancel() {
        countDownTimer.cancel()
    }

}