package com.geekymusketeers.medify.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateTimeExtension {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateAsString(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): Date? {
        val currentDateTime = LocalDateTime.now()
        val format = SimpleDateFormat(Constants.dateFormat)
        val formatter = DateTimeFormatter.ofPattern(Constants.dateFormat)
        val formattedDateTime = currentDateTime.format(formatter)
        return format.parse(formattedDateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(dateOfBirth: LocalDate): Int {
        val period = Period.between(dateOfBirth, LocalDate.now())
        return period.years
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateToLocalDate(date: Date): LocalDate {
        val instant: Instant = date.toInstant()
        val zoneId: ZoneId = ZoneId.systemDefault()
        val zonedDateTime = instant.atZone(zoneId)
        return zonedDateTime.toLocalDate()
    }
}