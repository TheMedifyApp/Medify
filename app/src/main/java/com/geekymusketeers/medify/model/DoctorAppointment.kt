package com.geekymusketeers.medify.model

data class DoctorAppointment(
    var PatientName: String? = null,
    var Disease: String? = null,
    var PatientPhone: String? = null,
    var Time: String? = null,
    var Date: String? = null,
    var PatientCondition: String? = null,
    var Prescription: String? = null,
    var TotalPoints: String? = null,
    var DoctorUID: String? = null,
    var PatientID: String? = null,
)
