package com.geekymusketeers.medify.ui.mainFragments.appointment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geekymusketeers.medify.ui.HomeActivity
import com.geekymusketeers.medify.databinding.ActivityBookingDoneBinding
import com.geekymusketeers.medify.model.Summary

class BookingDoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val summary = intent.getSerializableExtra("summary") as Summary
        val summary = if (Build.VERSION.SDK_INT >= 33) { // TIRAMISU
            intent.getParcelableExtra("summary", Summary::class.java)
        } else {
            intent.getParcelableExtra("summary")
        }
        initViews(summary!!)

    }

    @SuppressLint("SetTextI18n")
    private fun initViews(summary: Summary) {
        binding.apply {
            summaryDoctorName.text = "Doctor's Name: ${summary.doctorName}"
            summaryDoctorSpeciality.text = "Speciality: ${summary.doctorSpeciality}"
            summaryDoctorEmail.text = "Doctor's Email: ${summary.doctorEmail}"
            summaryDoctorPhoneNumber.text = "Doctor's Phone ${summary.doctorPhone}"
            summaryDate.text = "Appointment Date: ${summary.appointmentDate}"
            summaryTime.text = "Appointment Time: ${summary.appointmentTime}"
            summaryDisease.text = "Disease: ${summary.disease}"
            summaryPainLevel.text = "Pain: ${summary.painLevel}"
            btnHome.setOnClickListener {
                val intent = Intent(baseContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}