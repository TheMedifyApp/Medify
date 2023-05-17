package com.geekymusketeers.medify.ui.mainFragments

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
import com.geekymusketeers.medify.model.Rating
import com.geekymusketeers.medify.model.DoctorAppointment
import com.geekymusketeers.medify.ui.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.databinding.ActivityDoctorPatientBinding
import com.geekymusketeers.medify.databinding.RatingModalBinding
import com.geekymusketeers.medify.utils.DialogUtil.createBottomSheet
import com.geekymusketeers.medify.utils.DialogUtil.setBottomSheet
import com.geekymusketeers.medify.utils.Logger
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorPatient : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientBinding
    private lateinit var dbref: Query
    private lateinit var Recyclerview: RecyclerView
    private lateinit var appointmentAdapter: DoctorsAppointmentAdapter
    private lateinit var appointmentList: ArrayList<DoctorAppointment>
    private lateinit var sharedPreference: SharedPreferences

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userID = sharedPreference.getString("uid", "Not found").toString()

        appointmentList = ArrayList()
        appointmentAdapter = DoctorsAppointmentAdapter(userID, appointmentList) {
            showRatingBottomSheet(it)
        }

        Recyclerview = binding.appointmentRecyclerview
        Recyclerview.layoutManager = LinearLayoutManager(baseContext)
        Recyclerview.setHasFixedSize(true)


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
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG)
                    .show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAppointmentDateTime(date: String?, time: String): String {
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val appointmentDateTime = date + " " + time.substring(0, 5)
        return format.parse(appointmentDateTime)?.toString() ?: ""
    }

    private fun showRatingBottomSheet(doctorAppointment: DoctorAppointment) {
        val dialog = RatingModalBinding.inflate(layoutInflater)
        val bottomSheet = this.createBottomSheet()
        dialog.apply {
            this.apply {
                submitRating.setOnClickListener {
                    val rating = Rating(
                        patientId = doctorAppointment.PatientID!!,
                        doctorId = doctorAppointment.DoctorUID!!,
                        rating = ratingBar.rating,
                        review = ratingComment.text.toString().trim(),
                        patientName = doctorAppointment.PatientName!!,
                        timestamp = getAppointmentDateTime(
                            doctorAppointment.Date,
                            doctorAppointment.Time!!
                        )
                    )
                    try {
                        FirebaseDatabase.getInstance()
                            .getReference("Users").child(doctorAppointment.PatientID!!).get()
                            .addOnSuccessListener { snapShot ->

                                var total = 0f
                                var size = 0

                                snapShot.child("ratings").children.forEach() { ratingSnapshot ->

                                    total += ratingSnapshot.child("rating").value.toString().toFloat()
                                    size++

                                }

                                val totalRatingSize: Int =
                                    if (size == 0) 2 else size + 1

                                if (total == 0f)
                                    total = 5f

                                val totalRatingGiven = total + rating.rating
                                val newRating = totalRatingGiven / totalRatingSize

                                Logger.debugLog("Total: $total")
                                Logger.debugLog("Size: $size")
                                Logger.debugLog("TotalRatingSize: $totalRatingSize")
                                Logger.debugLog("TotalRatingGiven: $totalRatingGiven")
                                Logger.debugLog("NewRating: $newRating")

                                snapShot.child("totalRating").ref.setValue(newRating)
                                snapShot.child("ratings").ref.push().setValue(rating)
                                bottomSheet.dismiss()
                            }

                        Logger.debugLog("Rating: $rating")
                    } catch (e: Exception) {
                        Logger.debugLog("Rating: $e")
                    }
                }
            }
        }
        dialog.root.setBottomSheet(bottomSheet)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(date: String, userID: String) {

        dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(userID)
            .child("DoctorsAppointments").child(date).orderByChild("TotalPoints")

        dbref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (appointmentSnapshot in snapshot.children) {
                        val appointment =
                            appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        appointmentList.add(appointment!!)
                        appointmentList.sortWith(compareBy { it.TotalPoints })
                        appointmentList.reverse()
                        Log.d(
                            "TotalPoints",
                            appointment.TotalPoints.toString() + " " + appointment.PatientName
                        )
                        Log.d("User", appointmentList.toString())
                    }
                    Recyclerview.adapter = appointmentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    baseContext,
                    error.message, Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.selectDateTextToHide.visibility = View.GONE
    }
}