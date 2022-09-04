package com.geekymusketeers.medify.mainFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.DoctorAppointment
import com.geekymusketeers.medify.adapter.DoctorsAppointmentAdapter
import com.geekymusketeers.medify.databinding.FragmentAppointmentBinding
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbref : DatabaseReference
    private lateinit var Recyclerview : RecyclerView
    private lateinit var appointmentList : ArrayList<DoctorAppointment>
    private lateinit var sharedPreference : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // CODE

         Recyclerview = binding.appointmentRecyclerview
        Recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        Recyclerview.setHasFixedSize(true)

        appointmentList = arrayListOf<DoctorAppointment>()
        getData()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {

        val current = LocalDateTime.now()
        val userID = sharedPreference.getString("uid","Not found").toString()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = current.format(formatter)

        dbref = FirebaseDatabase.getInstance().getReference("Doctor").child(userID).child("DoctorsAppointments").child(date)

        dbref.addValueEventListener(object  : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){

                        val appointment = appointmentSnapshot.getValue(DoctorAppointment::class.java)
                        appointmentList.add(appointment!!)
                    }
                    Recyclerview.adapter = DoctorsAppointmentAdapter(appointmentList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}

