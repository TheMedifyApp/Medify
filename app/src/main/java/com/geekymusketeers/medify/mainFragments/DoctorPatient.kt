package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.DoctorAppointment
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.databinding.ActivityDoctorPatientBinding
import com.geekymusketeers.medify.databinding.ActivityHomeBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DoctorPatient : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientBinding
    private lateinit var dbref : Query
    private lateinit var Recyclerview : RecyclerView
    private lateinit var appointmentAdapter: DoctorsAppointmentAdapter
    private lateinit var appointmentList : ArrayList<DoctorAppointment>
    private lateinit var sharedPreference : SharedPreferences

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // CODE

        appointmentList = ArrayList()
        appointmentAdapter = DoctorsAppointmentAdapter(appointmentList)

        Recyclerview = binding.appointmentRecyclerview
        Recyclerview.layoutManager = LinearLayoutManager(baseContext)
        Recyclerview.setHasFixedSize(true)

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
                val date = dateFormatter.format(Date(it)).toString().trim()
                binding.selectDate.text = date
                appointmentList.clear()
                getData(date)

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(date: String) {
        val userID = sharedPreference.getString("uid","Not found").toString()
        val doctorIntentUid = intent.getStringExtra("uid")

        if (doctorIntentUid.isNullOrEmpty()) {
            dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(userID).child("DoctorsAppointments").child(date).orderByChild("Points")
        } else {
            dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorIntentUid).child("DoctorsAppointments").child(date).orderByChild("Points")
        }

        dbref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){
                        val appointment = appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        appointmentList.add(appointment!!)
                        appointmentList.reverse()
                    }
                    Recyclerview.adapter = appointmentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.selectDateTextToHide.visibility = View.GONE
    }
}