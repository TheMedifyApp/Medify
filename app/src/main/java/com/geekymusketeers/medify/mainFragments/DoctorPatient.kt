package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.appointment.DoctorAppointment
import com.geekymusketeers.medify.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.databinding.ActivityDoctorPatientBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
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

        appointmentList = ArrayList()
        appointmentAdapter = DoctorsAppointmentAdapter(appointmentList)

        Recyclerview = binding.appointmentRecyclerview
        Recyclerview.layoutManager = LinearLayoutManager(baseContext)
        Recyclerview.setHasFixedSize(true)

        val userID = sharedPreference.getString("uid","Not found").toString()
        var date: StringBuilder = StringBuilder("")
        date.append(intent.getStringExtra("date").toString())
        if (date.toString().isNotEmpty() || date.length > 0) {
            val hide = intent.getStringExtra("hide")
            if (hide == "hide") binding.selectDate.visibility = View.INVISIBLE
            val doctorIntentUid = intent.getStringExtra("uid").toString()
            getData(date.toString(), doctorIntentUid)
        }

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
                val tempDate = dateFormatter.format(Date(it)).toString().trim()
                date.setLength(0)
                date.append(tempDate)
                appointmentList.clear()
                binding.selectDate.text = date
                getData(date.toString(), userID)

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
    private fun getData(date: String, userID: String) {

        dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(userID).child("DoctorsAppointments").child(date).orderByChild("TotalPoints")

        dbref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){
                        val appointment = appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        appointmentList.add(appointment!!)
                        appointmentList.sortWith(compareBy { it.TotalPoints })
                        appointmentList.reverse()
                        Log.d("TotalPoints", appointment.TotalPoints.toString() +" "+ appointment.PatientName)
                        Log.d("User", appointmentList.toString())
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