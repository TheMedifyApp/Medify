package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Summary(
    val doctorName: String,
    val doctorSpeciality: String,
    val doctorEmail: String,
    val doctorPhone: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val disease: String,
    val painLevel: String,
    val totalPoint: Int
) : Parcelable
