package com.geekymusketeers.medify.ui.mainFragments.appointment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.geekymusketeers.medify.model.Summary
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var mapOfDiseasesList: HashMap<String, ArrayList<String>>
    private lateinit var diseaseValue: HashMap<String, Float>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initView()

        diseaseValue = Utils.setDiseaseValues(baseContext)

        val doctorType = intent.extras!!.getString("Dtype")

        // Date Picker
        binding.selectDate.setOnClickListener {
            handleDatePicker()
        }

        // Booking Appointment
        binding.btnFinalbook.onSlideCompleteListener =
            object : SlideToActView.OnSlideCompleteListener {
                override fun onSlideComplete(view: SlideToActView) {
                    bookAppointment(doctorType)
                }
            }


        val items: List<String> = mapOfDiseasesList[doctorType]!!
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Severe Pain", "Mild Pain", "No Pain")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)

        val timeItems = listOf(
            "9:00 AM - 11:00 AM",
            "11:00 AM - 13:00 PM",
            "17:00 PM - 19:00 PM",
            "19:00 PM - 22:OO PM"
        )
        val timeAdapter = ArrayAdapter(this, R.layout.list_items, timeItems)
        binding.timeDropdown.setAdapter(timeAdapter)
    }

    @SuppressLint("SimpleDateFormat")
    private fun handleDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {

            // disable past dates
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointForward.now())
            setCalendarConstraints(constraintsBuilder.build())

            // set the minimum selectable date to today's date
            val calendar = Calendar.getInstance()
            setSelection(calendar.timeInMillis)

        }.build()
        datePicker.show(supportFragmentManager, "DatePicker")


        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.selectDate.setText(date)
        }

        // Setting up the event for when cancelled is clicked
        datePicker.addOnNegativeButtonClickListener {
            Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG)
                .show()
        }

        // Setting up the event for when back button is pressed
        datePicker.addOnCancelListener {
            Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun bookAppointment(doctorType: String?) {

        val conditionValue = Utils.setConditionValue(baseContext)

        val totalPoint: Int

        val doctorUid = intent.extras!!.getString("Duid")
        val doctorName = intent.extras!!.getString("Dname")
        val doctorEmail = intent.extras!!.getString("Demail")
        val doctorPhone = intent.extras!!.getString("Dphone")

        val userName = sharedPreference.getString("name", "").toString()
        val userPhone = sharedPreference.getString("phone", "").toString()
        val userid = sharedPreference.getString("uid", "").toString()
        val userPrescription = sharedPreference.getString("prescription", "").toString()

        val date = binding.selectDate.text.toString()
        val time = binding.timeDropdown.text.toString()
        val disease = binding.diseaseDropdown.text.toString()
        val situation = binding.situationDropdown.text.toString()
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

        val appointmentD: HashMap<String, String> = HashMap() //define empty hashmap
        appointmentD["PatientName"] = userName
        appointmentD["PatientPhone"] = userPhone
        appointmentD["Time"] = time
        appointmentD["Date"] = date
        appointmentD["Disease"] = disease
        appointmentD["PatientCondition"] = situation
        appointmentD["Prescription"] = userPrescription
        appointmentD["TotalPoints"] = totalPoint.toString().trim()
        appointmentD["DoctorUID"] = doctorUid.toString()
        appointmentD["PatientID"] = userid

        val appointmentP: HashMap<String, String> = HashMap() //define empty hashmap
        appointmentP["DoctorUID"] = doctorUid.toString()
        appointmentP["DoctorName"] = doctorName.toString()
        appointmentP["DoctorPhone"] = doctorPhone.toString()
        appointmentP["Date"] = date
        appointmentP["Time"] = time
        appointmentP["Disease"] = disease
        appointmentP["PatientCondition"] = situation
        appointmentP["Prescription"] = userPrescription
        appointmentP["PatientID"] = userid

        val appointmentDB_Doctor =
            FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid!!)
                .child("DoctorsAppointments").child(date)
        appointmentDB_Doctor.child(userid).setValue(appointmentD)

        val appointmentDB_User_Doctor =
            FirebaseDatabase.getInstance().getReference("Users").child(doctorUid)
                .child("DoctorsAppointments").child(date)
        appointmentDB_User_Doctor.child(userid).setValue(appointmentD)

        val appointmentDB_Patient =
            FirebaseDatabase.getInstance().getReference("Users").child(userid)
                .child("PatientsAppointments").child(date)
        appointmentDB_Patient.child(doctorUid).setValue(appointmentP)

        val summary = Summary(
            doctorName = doctorName.toString(),
            doctorSpeciality = doctorType.toString(),
            doctorEmail = doctorEmail.toString(),
            doctorPhone = doctorPhone.toString(),
            appointmentDate = date,
            appointmentTime = time,
            disease = disease,
            painLevel = situation,
            totalPoint = totalPoint
        )

        val intent = Intent(this@AppointmentBooking, BookingDoneActivity::class.java)
        intent.putExtra("summary", summary)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        mapOfDiseasesList = Utils.initializeSpecializationWithDiseasesLists()
    }

}

