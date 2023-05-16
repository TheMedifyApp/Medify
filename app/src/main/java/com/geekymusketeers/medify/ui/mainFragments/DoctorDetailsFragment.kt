package com.geekymusketeers.medify.ui.mainFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentDoctorDetailsBinding
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Logger
import com.google.android.material.bottomnavigation.BottomNavigationView


class DoctorDetailsFragment : Fragment() {

    private var _binding: FragmentDoctorDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DoctorDetailsFragmentArgs by navArgs()
    private var doctor : User = User()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDoctorDetailsBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.GONE

        initDoctor()
        initViews()
        return binding.root
    }

    private fun initDoctor() {
        args.doctorDetails.let {
            doctor.Name = it.Name
            doctor.Specialist = it.Specialist
            doctor.Email = it.Email
            doctor.Phone = it.Phone
            doctor.UID = it.UID
        }
    }

    private fun initViews() {
        binding.run {
            doctorName.text = doctor.Name
            doctorSpecialization.text = doctor.Specialist
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            bookAppointment.setOnClickListener {
                val action = DoctorDetailsFragmentDirections.actionDoctorDetailsFragmentToAppointmentBookingFragment(doctor)
                findNavController().navigate(action)
            }
        }
        Logger.debugLog("Doctor is ${doctor}")
    }


}