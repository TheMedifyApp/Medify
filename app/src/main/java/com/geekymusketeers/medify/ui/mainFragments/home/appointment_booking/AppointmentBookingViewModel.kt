package com.geekymusketeers.medify.ui.mainFragments.home.appointment_booking

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Summary
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.SharedPrefsExtension.getUserFromSharedPrefs
import com.geekymusketeers.medify.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AppointmentBookingViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var mapOfDiseasesList: HashMap<String, ArrayList<String>>
    private lateinit var diseaseValue: HashMap<String, Float>


    var fireStatusMutableLiveData = MutableLiveData<Boolean>()
    private val _navigateToBookingSummary = MutableLiveData<Summary>()

    private var userLiveData = MutableLiveData<User>()
    val navigateToBookingSummary: LiveData<Summary> get() = _navigateToBookingSummary

    fun bookAppointment(
        doctorType: String?,
        selectDate: String,
        time: String,
        disease: String,
        situation: String,
        doctorUid: String,
        doctorName: String,
        doctorEmail: String,
        doctorPhone: String,
        conditionValue: HashMap<String, Float>
    ) = viewModelScope.launch {

        try {

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
            appointmentD["PatientName"] = userLiveData.value?.Name.toString()
            appointmentD["PatientPhone"] = userLiveData.value?.Phone.toString()
            appointmentD["Time"] = time
            appointmentD["Date"] = selectDate
            appointmentD["Disease"] = disease
            appointmentD["PatientCondition"] = situation
            appointmentD["Prescription"] = userLiveData.value?.Prescription.toString()
            appointmentD["TotalPoints"] = totalPoint.toString().trim()
            appointmentD["DoctorUID"] = doctorUid
            appointmentD["PatientID"] = userLiveData.value?.UID.toString()

            val appointmentP: HashMap<String, String> = HashMap()
            appointmentP["DoctorUID"] = doctorUid
            appointmentP["DoctorName"] = doctorName
            appointmentP["DoctorPhone"] = doctorPhone
            appointmentP["Date"] = selectDate
            appointmentP["Time"] = time
            appointmentP["Disease"] = disease
            appointmentP["PatientCondition"] = situation
            appointmentP["Prescription"] = userLiveData.value?.Prescription.toString()
            appointmentP["PatientID"] = userLiveData.value?.UID.toString()

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
            Logger.debugLog("summary: $summary")
            Logger.debugLog("appointmentD: $appointmentD")
            Logger.debugLog("appointmentP: $appointmentP")


            val firebaseUploadJob = viewModelScope.launch {

                val appointmentDBUserDoctor = viewModelScope.async(Dispatchers.IO) {

                    updateUserDoctorAppointment(
                        doctorUid,
                        userLiveData.value?.UID.toString(),
                        appointmentD
                    )
                }

                val appointmentDBPatient = viewModelScope.async(Dispatchers.IO) {

                    updateUserPatientAppointment(
                        userLiveData.value?.UID.toString(),
                        doctorUid,
                        appointmentP
                    )
                }
                fireStatusMutableLiveData.postValue(
                    appointmentDBUserDoctor.await() && appointmentDBPatient.await()
                )
            }

            firebaseUploadJob.join()
        } catch (e: Exception) {
            Logger.debugLog("Exception: ${e.message}")
        }

    }

    private suspend fun updateUserPatientAppointment(
        userId: String,
        doctorUid: String,
        appointmentP: HashMap<String, String>
    ): Boolean {

        var res = true
        return withContext(Dispatchers.IO) {

            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                .child("PatientsAppointments").child(appointmentP["Date"]!!).child(doctorUid)
                .setValue(appointmentP)
                .addOnSuccessListener {
                    Logger.debugLog("Successfully updated patient appointment")
                    res
                }.addOnFailureListener {
                    Logger.debugLog("Failed to update patient appointment")
                    res = false
                }
            res
        }
    }

    private suspend fun updateUserDoctorAppointment(
        doctorUid: String,
        userId: String,
        appointmentD: HashMap<String, String>
    ): Boolean {

        var result = true
        return withContext(Dispatchers.IO) {

            FirebaseDatabase.getInstance().getReference(Constants.Users).child(doctorUid)
                .child("DoctorsAppointments").child(appointmentD["Date"]!!).child(userId)
                .setValue(appointmentD)
                .addOnSuccessListener {
                    Logger.debugLog("Successfully updated doctor appointment")
                    result
                }.addOnFailureListener {
                    Logger.debugLog("Failed to update doctor appointment")
                    result = false
                }
            result
        }
    }

    fun initializeSpecializationWithDiseasesLists() {
        mapOfDiseasesList = Utils.initializeSpecializationWithDiseasesLists()
    }

    fun setDiseaseValues(diseaseValue: HashMap<String, Float>) {
        this.diseaseValue = diseaseValue
    }

    fun getDataFromSharedPref(sharedPreference: SharedPreferences) {
        userLiveData.value = sharedPreference.getUserFromSharedPrefs()
    }

}