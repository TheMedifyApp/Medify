package com.geekymusketeers.medify.mainFragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.appointment.PatientAppointment
import com.geekymusketeers.medify.adapter.PatientAppointmentAdapter
import com.geekymusketeers.medify.databinding.FragmentPatientAppointmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class PatientAppointmentFragment : Fragment() {

    private var _binding: FragmentPatientAppointmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbref : DatabaseReference
    private lateinit var Recyclerview : RecyclerView
    private lateinit var appointmentAdapter: PatientAppointmentAdapter
    private lateinit var appointmentList : ArrayList<PatientAppointment>
    private lateinit var sharedPreference : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPatientAppointmentBinding.inflate(inflater, container, false)
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // CODE

        appointmentList = ArrayList()
        appointmentAdapter = PatientAppointmentAdapter(requireActivity(), appointmentList)

        val isDoctor = sharedPreference.getString("isDoctor","Not found").toString()
        if (isDoctor == "Doctor")
            binding.toPatientList.visibility = View.VISIBLE
        binding.toPatientList.setOnClickListener {
            startActivity(Intent(requireActivity(), DoctorPatient::class.java))
        }

        Recyclerview = binding.appointmentRecyclerview
        Recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        Recyclerview.setHasFixedSize(true)

        binding.selectDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            fragmentManager?.let { datePicker.show(it, "DatePicker") }
            val date: StringBuilder = StringBuilder("")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val tempDate = dateFormatter.format(Date(it)).toString().trim()
                date.setLength(0)
                date.append(tempDate)
                appointmentList.clear()
                getData(date.toString())

            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(requireActivity(), "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(requireActivity(), "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }

//        getData()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(date: String) {

//        val current = LocalDateTime.now()
        val userID = sharedPreference.getString("uid","Not found").toString()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        val date = current.format(formatter)

        dbref = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("PatientsAppointments").child(date)

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){
                        val appointment = appointmentSnapshot.getValue(PatientAppointment::class.java)
                        appointmentList.add(appointment!!)
                    }
                    Recyclerview.adapter = appointmentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(),
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.selectDateTextToHide.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        val isDoctor = sharedPreference.getString("isDoctor","Not found").toString()
        if (isDoctor == "Doctor")
            binding.toPatientList.visibility = View.VISIBLE
    }
}

