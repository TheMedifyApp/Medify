package com.geekymusketeers.medify.ui.mainFragments.home.appointment_booking

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Summary
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class AppointmentBookingViewModel(application: Application): BaseViewModel(application) {
    private lateinit var mapOfDiseasesList: HashMap<String, ArrayList<String>>
    private lateinit var diseaseValue: HashMap<String, Float>
    private var context: Context = application.applicationContext

    private val _navigateToBookingSummary = MutableLiveData<Summary>()
    val navigateToBookingSummary: LiveData<Summary> get() = _navigateToBookingSummary

    fun bookAppointment(
        doctorType: String?,
        userName: String,
        userPhone: String,
        userId: String,
        userPrescription: String,
        selectDate: String,
        time: String,
        disease: String,
        situation: String,
        doctorUid: String,
        doctorName: String,
        doctorEmail: String,
        doctorPhone: String
    ) {
        val conditionValue = Utils.setConditionValue(context)

        val totalPoint: Int

        val rightNow = Calendar.getInstance()
        val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        val firstComeFirstServe = 1 + (0.1 * ((currentHourIn24Format / 10) + 1))

        Logger.debugLog("firstComeFirstServe: $firstComeFirstServe")
        Logger.debugLog("diseaseValue: ${diseaseValue[disease]}")
        Logger.debugLog("conditionValue: ${conditionValue[situation]}")
        Logger.debugLog("currentHourIn24Format: $currentHourIn24Format")

        var temp = diseaseValue[disease]!!
        Logger.debugLog("temp value after adding disease value: $temp")
        temp += conditionValue[situation]!!
        Logger.debugLog("temp value after adding condition value: $temp")
        totalPoint = (temp * firstComeFirstServe).toInt()
        Logger.debugLog("totalPoint: $totalPoint")

        val appointmentD: HashMap<String, String> = HashMap()
        appointmentD["PatientName"] = userName
        appointmentD["PatientPhone"] = userPhone
        appointmentD["Time"] = time
        appointmentD["Date"] = selectDate
        appointmentD["Disease"] = disease
        appointmentD["PatientCondition"] = situation
        appointmentD["Prescription"] = userPrescription
        appointmentD["TotalPoints"] = totalPoint.toString().trim()
        appointmentD["DoctorUID"] = doctorUid
        appointmentD["PatientID"] = userId

        val appointmentP: HashMap<String, String> = HashMap()
        appointmentP["DoctorUID"] = doctorUid
        appointmentP["DoctorName"] = doctorName
        appointmentP["DoctorPhone"] = doctorPhone
        appointmentP["Date"] = selectDate
        appointmentP["Time"] = time
        appointmentP["Disease"] = disease
        appointmentP["PatientCondition"] = situation
        appointmentP["Prescription"] = userPrescription
        appointmentP["PatientID"] = userId

        val appointmentDB_Doctor =
            FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid!!)
                .child("DoctorsAppointments").child(selectDate)
        appointmentDB_Doctor.child(userId).setValue(appointmentD)

        val appointmentDB_User_Doctor =
            FirebaseDatabase.getInstance().getReference("Users").child(doctorUid)
                .child("DoctorsAppointments").child(selectDate)
        appointmentDB_User_Doctor.child(userId).setValue(appointmentD)

        val appointmentDB_Patient =
            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                .child("PatientsAppointments").child(selectDate)
        appointmentDB_Patient.child(doctorUid).setValue(appointmentP)

        val summary = Summary(
            doctorName = doctorName,
            doctorSpeciality = doctorType.toString(),
            doctorEmail = doctorEmail,
            doctorPhone = doctorPhone,
            appointmentDate = selectDate,
            appointmentTime = time,
            disease = disease,
            painLevel = situation,
            totalPoint = totalPoint
        )
        _navigateToBookingSummary.value = summary
    }

    fun initializeSpecializationWithDiseasesLists() {
        mapOfDiseasesList = Utils.initializeSpecializationWithDiseasesLists()
    }

    fun setDiseaseValues() {
        diseaseValue = Utils.setDiseaseValues(context)
    }

}