package com.geekymusketeers.medify.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeExtension {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateAppointments(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }
}