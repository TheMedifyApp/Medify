package com.geekymusketeers.medify.mainFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.DoctorAppointment
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.databinding.ActivityDoctorPatientBinding
import com.geekymusketeers.medify.databinding.ActivityHomeBinding
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DoctorPatient : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var Recyclerview : RecyclerView
    private lateinit var appointmentAdapter: DoctorsAppointmentAdapter
    private lateinit var appointmentList : ArrayList<DoctorAppointment>
    private lateinit var sharedPreference : SharedPreferences

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

        getData()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {
        val current = LocalDateTime.now()
        val userID = sharedPreference.getString("uid","Not found").toString()
        val doctorIntentUid = intent.getStringExtra("uid")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = current.format(formatter)

        if (doctorIntentUid.isNullOrEmpty()) {
            dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(userID).child("DoctorsAppointments").child(date)
        } else {
            dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorIntentUid).child("DoctorsAppointments").child(date)
        }

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){
                        val appointment = appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        appointmentList.add(appointment!!)
                    }
                    Recyclerview.adapter = appointmentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}