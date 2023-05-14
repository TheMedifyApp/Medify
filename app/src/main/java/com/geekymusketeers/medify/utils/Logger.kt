package com.geekymusketeers.medify.utils

import android.util.Log


object Logger {

    fun errorLog(value: String) {
        Log.e("", value)
    }

    fun debugLog(value: String) {
        Log.d("", value)
    }
}