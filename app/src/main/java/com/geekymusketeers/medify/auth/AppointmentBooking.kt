package com.geekymusketeers.medify.mainFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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

        val items = listOf("Fever", "Cold", "Diarrhea", "Allergies", "Stomach Aches")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        val situationItems = listOf("Critical", "Urgent", "Under-control")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)
    }
}