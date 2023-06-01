package com.geekymusketeers.medify.utils

import android.util.Log
import com.geekymusketeers.medify.BuildConfig


object Logger {

    fun errorLog(value: String) {
        Log.e("", value)
    }

    fun debugLog(value: String) {
        Log.d("", value)
    }

    fun debugLog(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg!!)
        }
    }

    fun logException(tag: String, exception: Exception, logLevel: LogLevel, logToCrashlytics : Boolean = false) {
        when (logLevel) {
            LogLevel.DEBUG -> Log.d(tag, null, exception)
            LogLevel.ERROR -> Log.e(tag, null, exception)
            LogLevel.INFO -> Log.i(tag, null, exception)
            LogLevel.VERBOSE -> Log.v(tag, null, exception)
            LogLevel.WARN -> Log.w(tag, null, exception)
        }
        if (logToCrashlytics) {
            //TODO: send log to crashlytics like Firebase
        }
    }

    enum class LogLevel {
        DEBUG, ERROR, INFO, VERBOSE, WARN
    }
}