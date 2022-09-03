package com.geekymusketeers.medify

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.*

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding
    private lateinit var appointmentdb: DatabaseReference
    private lateinit var sharedPreference : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val doctorUid = intent.extras!!.getString("Duid")
//        val nameUid = intent.extras!!.getString("Dname")
//        val emailUid = intent.extras!!.getString("Demail")
//        val phoneUid = intent.extras!!.getString("Dphone")

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

        // Time Picker
        binding.selectTime.setOnClickListener {

            // instance of MDC time picker
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                // set the title for the alert dialog
                .setTitleText("SELECT YOUR TIMING")
                // set the default hour for the
                // dialog when the dialog opens
                .setHour(12)
                // set the default minute for the
                // dialog when the dialog opens
                .setMinute(10)
                // set the time format
                // according to the region
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "MainActivity")

            // on clicking the positive button of the time picker
            // dialog update the TextView accordingly
            materialTimePicker.addOnPositiveButtonClickListener {

                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                // check for single digit hour hour and minute
                // and update TextView accordingly
                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                        }
                    }
                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                        }
                    }
                }

                // then update the preview TextView
                binding.selectTime.setText(formattedTime)
            }
        }


        // Booking Appointment
        binding.btnFinalbook.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener{


            override fun onSlideComplete(view: SlideToActView) {
                val userName = sharedPreference.getString("name","").toString()
                val userPhone = sharedPreference.getString("phone","").toString()
                val uid = sharedPreference.getString("uid","").toString()

                val date = binding.selectDate.text.toString()
                val time = binding.selectTime.text.toString()
                val disease = binding.diseaseDropdown.text.toString()
                val situation = binding.situationDropdown.text.toString()

                //Toast.makeText(baseContext, "Pahuch gaye", Toast.LENGTH_LONG).show()
                //val appointment = DocAppointment(userName,userPhone,date,time,disease,situation)
                val appointment:HashMap<String,String> = HashMap<String,String>() //define empty hashmap
                appointment.put("PatientName",userName)
                appointment.put("PatientPhone",userPhone)
                appointment.put("Time",time)
                appointment.put("Date",date)
                appointment.put("Disease",disease)
                appointment.put("PatientCondition",situation)


                Toast.makeText(baseContext, doctorUid.toString(), Toast.LENGTH_LONG).show()
                appointmentdb = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid.toString()).child("DoctorsAppointments").child(date)

                appointmentdb.child(uid).setValue(appointment).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(baseContext, "Booking Done", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        // Disease List
        val items = listOf("Fever", "Cold", "Diarrhea", "Allergies", "Stomach Aches","Unknown")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Critical", "Urgent", "Under-control")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)
    }

}

