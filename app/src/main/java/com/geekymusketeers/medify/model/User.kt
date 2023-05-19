package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var Name: String? = null,
    var Email: String? = null,
    var Phone: String? = null,
    var UID: String? = null,
    var isDoctor: String = Doctor.IS_NOT_DOCTOR.toItemString(),
    var Age: Int = 0,
    var Gender: String? = null,
    var Address: String? = null,
    var Specialist: String? = null,
    var Stats: String? = "0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0",
    var Prescription: String? = null,
    var totalRating: Float = 5F,
    var ratings: List<Rating> = emptyList()
) : Parcelable

enum class Doctor {
    IS_DOCTOR,
    IS_NOT_DOCTOR;

    companion object {
        fun isDoctor(isDoctor: String): Doctor {
            return if (isDoctor == IS_DOCTOR.toDisplayString())
                IS_DOCTOR
            else
                IS_NOT_DOCTOR
        }

        fun isDoctorString(isDoctor: String): String {
            return if (isDoctor == IS_DOCTOR.toDisplayString())
                "Doctor"
            else
                "Patient"
        }
    }

    fun isUserDoctor(): Boolean {
        return this.toItemString() == IS_DOCTOR.toItemString()
    }

    fun toDisplayString(): String {
        return if (this == IS_DOCTOR) {
            "Yes, I'm a Doctor"
        } else "No, I'm not a Doctor"
    }

    fun toItemString(): String {
        return if (this == IS_DOCTOR) {
            "Doctor"
        } else "Patient"
    }
}

enum class Specialist {
    CARDIOLOGIST,
    DENTIST,
    ENT_SPECIALIST,
    OBSTETRICIAN_GYNAECOLOGIST,
    ORTHOPAEDIC_SURGEON,
    PSYCHIATRIST,
    RADIOLOGIST,
    PULMONOLOGIST,
    NEUROLOGIST,
    ALLERGISTS,
    GASTROENTEROLOGISTS;

    companion object {
        fun fromString(value: String): Specialist? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }

    }

    fun toItemString(): String {
        return when (this) {
            CARDIOLOGIST -> "Cardiologist"
            DENTIST -> "Dentist"
            ENT_SPECIALIST -> "ENT Specialist"
            OBSTETRICIAN_GYNAECOLOGIST -> "Obstetrician/Gynaecologist"
            ORTHOPAEDIC_SURGEON -> "Orthopaedic Surgeon"
            PSYCHIATRIST -> "Psychiatrist"
            RADIOLOGIST -> "Radiologist"
            PULMONOLOGIST -> "Pulmonologist"
            NEUROLOGIST -> "Neurologist"
            ALLERGISTS -> "Allergists"
            GASTROENTEROLOGISTS -> "Gastroenterologists"
        }
    }
}


enum class Gender(gender: String) {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    companion object {
        fun getGenderToGender(gender: String): Gender {
            return when (gender) {
                MALE.toDisplayString() -> MALE
                FEMALE.toDisplayString() -> FEMALE
                else -> OTHER
            }
        }

        fun getGenderToString(gender: String): String {
            return when (gender) {
                MALE.toDisplayString() -> "male"
                FEMALE.toDisplayString() -> "female"
                else -> "other"
            }
        }
    }

    fun toDisplayString(): String {
        return when (this) {
            MALE -> "Male"
            FEMALE -> "Female"
            else -> "Other"
        }
    }
    fun toItemString(): String {
        return when (this) {
            MALE -> "male"
            FEMALE -> "female"
            else -> "other"
        }
    }
}

@Parcelize
data class Rating(
    val patientId: String,
    val doctorId: String,
    val rating: Float,
    val review: String,
    val timestamp: String,
    val patientName: String,
) : Parcelable