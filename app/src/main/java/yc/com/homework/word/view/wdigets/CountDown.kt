package yc.com.homework.word.view.wdigets

import android.os.CountDownTimer

open class CountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onFinish() {

    }

    override fun onTick(arg0: Long) {

    }

    open fun toClock(millisUntilFinished: Long): String {
        var hour = millisUntilFinished / (60 * 60 * 1000)
        var minute = (millisUntilFinished - hour * 60 * 60 * 1000) / (60 * 1000)
        var second = (millisUntilFinished - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000
        if (second >= 60) {
            second %= 60
            minute += second / 60
        }
        if (minute >= 60) {
            minute %= 60
            hour += minute / 60
        }

        val sh = if (hour < 10) {
            "0$hour"
        } else {
            hour.toString()
        }
        val sm = if (minute < 10) {
            "0$minute"
        } else {
            minute.toString()
        }
        val ss = if (second < 10) {
            "0$second"
        } else {
            second.toString()
        }
        return "$sm:$ss"
    }

}