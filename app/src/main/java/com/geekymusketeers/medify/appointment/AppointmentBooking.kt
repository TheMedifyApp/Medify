package com.geekymusketeers.medify.appointment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.*

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding
    private lateinit var sharedPreference : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val diseaseValue = HashMap<String, Int>()
        val conditionValue = HashMap<String, Int>()
        var totalPoint = 0

        conditionValue["Severe Pain"] = 15
        conditionValue["Mild Pain"] = 8
        conditionValue["No Pain"] = 0

        diseaseValue["Not sure"] = 10
        diseaseValue["Fever"] = 7
        diseaseValue["Cold & Flu"] = 4
        diseaseValue["Diarrhea"] = 6
        diseaseValue["Allergies"] = 5
        diseaseValue["Stomach Aches"] = 7
        diseaseValue["Conjunctivitis"] = 5
        diseaseValue["Dehydration"] = 4
        diseaseValue["Tooth ache"] = 7
        diseaseValue["Ear ache"] = 5
        diseaseValue["Food poisoning"] = 6
        diseaseValue["Headache"] = 5


        val doctorUid = intent.extras!!.getString("Duid")
        val doctorName = intent.extras!!.getString("Dname")
        val doctorEmail = intent.extras!!.getString("Demail")
        val doctorPhone = intent.extras!!.getString("Dphone")

        // Date Picker
        binding.selectDate.setOnClickListener {
            // Initiation date picker with
            // MaterialDatePicker.Builder.datePicker()
            // and building it using build()
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                binding.selectDate.setText(date)

            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }




        // Booking Appointment
        binding.btnFinalbook.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener{

            override fun onSlideComplete(view: SlideToActView) {
                val userName = sharedPreference.getString("name","").toString()
                val userPhone = sharedPreference.getString("phone","").toString()
                val userid = sharedPreference.getString("uid","").toString()
                val userPrescription = sharedPreference.getString("prescription", "").toString()

                val date = binding.selectDate.text.toString()
                val time = binding.timeDropdown.text.toString()
                val disease = binding.diseaseDropdown.text.toString()
                val situation = binding.situationDropdown.text.toString()
                val rightNow = Calendar.getInstance()
                val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY)
                val firstComeFirstServe = 1 + (0.1 * ((currentHourIn24Format / 10) + 1))

                totalPoint += diseaseValue[disease]!!
                totalPoint += conditionValue[situation]!!
                totalPoint = (totalPoint * firstComeFirstServe).toInt()

                val appointmentD:HashMap<String,String> = HashMap() //define empty hashmap
                appointmentD["PatientName"] = userName
                appointmentD["PatientPhone"] = userPhone
                appointmentD["Time"] = time
                appointmentD["Date"] = date
                appointmentD["Disease"] = disease
                appointmentD["PatientCondition"] = situation
                appointmentD["Prescription"] = userPrescription
                appointmentD["TotalPoints"] = totalPoint.toString().trim()

                val appointmentP : HashMap<String, String> = HashMap() //define empty hashmap
                appointmentP["DoctorUID"] = doctorUid.toString()
                appointmentP["DoctorName"] = doctorName.toString()
                appointmentP["DoctorPhone"] = doctorPhone.toString()
                appointmentP["Date"] = date
                appointmentP["Time"] = time
                appointmentP["Disease"] = disease
                appointmentP["PatientCondition"] = situation
                appointmentP["Prescription"] = userPrescription

                val appointmentDB_Doctor = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid!!).child("DoctorsAppointments").child(date)
                appointmentDB_Doctor.child(userid).setValue(appointmentD)

                val appointmentDB_User_Doctor = FirebaseDatabase.getInstance().getReference("Users").child(doctorUid).child("DoctorsAppointments").child(date)
                appointmentDB_User_Doctor.child(userid).setValue(appointmentD)


                val appointmentDB_Patient = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("PatientsAppointments").child(date)
                appointmentDB_Patient.child(doctorUid).setValue(appointmentP)

                startActivity(Intent(baseContext, BookingDoneActivity::class.java))
                finish()
            }
        }


        // Disease List
        val items = listOf("Not sure", "Fever", "Cold & Flu", "Diarrhea","Headache", "Allergies", "Stomach Aches","Conjunctivitis", "Dehydration", "Tooth ache", "Ear ache", "Food poisoning")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Severe Pain", "Mild Pain", "No Pain")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)

        val timeItems = listOf("9:00 AM - 11:00 AM","11:00 AM - 13:00 PM", "17:00 PM - 19:00 PM","19:00 PM - 22:OO PM")
        val timeAdapter = ArrayAdapter(this, R.layout.list_items, timeItems)
        binding.timeDropdown.setAdapter(timeAdapter)
    }

}

