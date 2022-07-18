package com.paylivre.sdk.gateway.android.services.countdowntimer
import android.os.CountDownTimer


class MockCountDownTimerGivenHelper :
    CountDownTimerService {
    override lateinit var countDownTimer: CountDownTimer
    var timerFinalCheckInMillisMocked: Long = 10
    var timerIntervalInMillisMocked: Long = 10
    private lateinit var _onTickAction: (Long) -> Unit
    private lateinit var _onFinishAction: () -> Unit
    var calledCancel = false

    override fun startTimer(
        timerFinalCheckInMillis: Long,
        timerIntervalInMillis: Long,
        onTickAction: (Long) -> Unit,
        onFinishAction: () -> Unit,
    ) {
        countDownTimer = object : CountDownTimer(timerFinalCheckInMillisMocked,
            timerIntervalInMillisMocked) {
            override fun onTick(millisUntilFinished: Long) {
                onTickAction(millisUntilFinished)
            }

            override fun onFinish() {
                onFinishAction()
            }
        }

        _onFinishAction = onFinishAction
        _onTickAction = onTickAction

        countDownTimer.start()
    }

    override fun cancel() {
        calledCancel = true
        countDownTimer.cancel()
    }

    fun dispatchOnFinish() {
        _onFinishAction()
    }

    fun dispatchOnTick(millisUntilFinished: Long) {
        _onTickAction(millisUntilFinished)
    }

}