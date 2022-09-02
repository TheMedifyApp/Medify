package com.geekymusketeers.medify.mainFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.geekymusketeers.medify.databinding.ActivityHomeBinding

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val uid :String = intent.getStringExtra("uid").toString()
        val name :String = intent.getStringExtra("name").toString()
        val email :String = intent.getStringExtra("email").toString()
        val phone :String = intent.getStringExtra("phone").toString()

        binding.doctorName.text = name
        binding.doctorEmail.text = email
        binding.doctorPhone.text = phone

    }
}