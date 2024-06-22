package com.dentalcare.quiz.analytics

import com.orm.BuildConfig

object Analytics {
    fun recordException(t: Throwable) {
        // TODO record exception via firebase or mixpanel
        if (BuildConfig.DEBUG) {
            t.printStackTrace()
        }
    }
}