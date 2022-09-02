package com.geekymusketeers.medify

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Date Picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.selectDate.setOnClickListener {
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view , mYear,mMonth,mDay->
                binding.selectDate.setText(""+mDay +"/"+mMonth +"/"+mYear)
            }, year,month,day)
            dpd.show()
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


        // Disease List
        val items = listOf("Fever", "Cold", "Diarrhea", "Allergies", "Stomach Aches")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Critical", "Urgent", "Under-control")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)
    }

}

