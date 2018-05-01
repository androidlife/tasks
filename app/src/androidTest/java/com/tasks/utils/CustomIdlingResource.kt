package com.tasks.utils

import android.support.test.espresso.IdlingResource
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class CustomIdlingResource : IdlingResource {

    private val idleNow = AtomicBoolean(false)
    private var callback: IdlingResource.ResourceCallback? = null
    override fun getName(): String {
        return "com.tasks.CustomIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        return idleNow.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    private fun setIdleNow(isIdle: Boolean) {
        idleNow.set(isIdle)
        if (isIdle)
            callback?.onTransitionToIdle()
    }

    fun startCountdown(countDown: Long, timeUnit: TimeUnit) {
        Observable.timer(countDown, timeUnit).subscribe({ l -> setIdleNow(true) })
    }

}
