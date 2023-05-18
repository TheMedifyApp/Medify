package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var Name: String? = null,
    var Email: String? = null,
    var Phone: String? = null,
    var UID: String? = null,
    var isDoctor: Doctor = Doctor.IS_NOT_DOCTOR,
    var Age: Int? = null,
    var Gender: String? = null,
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
        fun fromBoolean(isDoctor: Boolean): Doctor {
            return if (isDoctor) IS_DOCTOR else IS_NOT_DOCTOR
        }

        fun fromItemString(isDoctor: String): Boolean {
            return isDoctor == "Yes, I'm a Doctor"
        }

        fun isDoctor(isDoctor: String): Doctor {
            return if (isDoctor == IS_DOCTOR.toItemString())
                IS_DOCTOR
            else
                IS_NOT_DOCTOR
        }
    }
    fun toBoolean(): Boolean {
        return this == IS_DOCTOR
    }

    fun toItemString(): String {
        return if (this == IS_DOCTOR) {
            "Yes, I'm a Doctor"
        } else "No, I'm not a Doctor"
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
        fun getGenderState(gender: Gender) : String {
            return when (gender) {
                MALE -> "male"
                FEMALE -> "female"
                else -> "other"
            }
        }

        fun fromItemString(gender: String): String {
            return when (gender) {
                "male" -> "male"
                "female" -> "female"
                else -> "other"
            }
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