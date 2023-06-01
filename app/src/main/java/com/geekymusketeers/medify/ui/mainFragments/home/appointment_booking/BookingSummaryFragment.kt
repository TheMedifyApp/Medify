package com.geekymusketeers.medify.ui.mainFragments.home.appointment_booking

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.databinding.FragmentBookingSummaryBinding


class BookingSummaryFragment : Fragment() {

    private lateinit var _binding: FragmentBookingSummaryBinding
    private val binding get() = _binding
    private val args: BookingSummaryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingSummaryBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.apply {
            summaryDoctorName.text = "Doctor's Name: ${args.summary.doctorName}"
            summaryDoctorSpeciality.text = "Speciality: ${args.summary.doctorSpeciality}"
//            summaryDoctorEmail.text = "Doctor's Email: ${args.summary.doctorEmail}"
//            summaryDoctorPhoneNumber.text = "Doctor's Phone ${args.summary.doctorPhone}"
            summaryDate.text = "Appointment Date: ${args.summary.appointmentDate}"
            summaryTime.text = "Appointment Time: ${args.summary.appointmentTime}"
            btnHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}