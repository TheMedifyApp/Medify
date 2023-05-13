package com.geekymusketeers.medify.model

data class PatientAppointment(
    var DoctorName: String? = null,
    var DoctorPhone: String? = null,
    var DoctorUID: String? = null,
    var Disease: String? = null,
    var Time: String? = null,
    var Date: String? = null,
    var PatientCondition: String? = null,
    var prescriptionLink: String? = null,
    var PatientID: String? = null,
)
