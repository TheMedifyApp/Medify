package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var Name: String? = null,
    var Email: String? = null,
    var Phone: String? = null,
    var UID: String? = null,
    var isDoctor: String? = null,
    var Age: String? = null,
    var Specialist: String? = null,
    var Stats: String? = null,
    var Prescription: String? = null,
    var totalRating: Float = 5F,
    var ratings: List<Rating> = emptyList()
) : Parcelable

@Parcelize
data class Rating(
    val patientId: String,
    val doctorId: String,
    val rating: Float,
    val review: String,
    val timestamp: String,
    val patientName: String,
) : Parcelable