package com.geekymusketeers.medify.auth

data class User(
    val Name: String? = null,
    val Email: String? = null,
    val Phone: String? = null,
    val UID: String? = null,
    val isDoctor: String? = null,
    val Age: String? = null,
    val Specialist: String? = null,
    val Stats: String? = null,
    val Prescription: String? = null,
    var totalRating: Float = 5F,
    var ratings: List<Rating> = emptyList()
)

data class Rating(
    val patientId: String,
    val doctorId: String,
    val rating: Float,
    val review: String,
    val timestamp: String,
    val patientName: String,
)