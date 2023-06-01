package com.geekymusketeers.medify.ui.mainFragments.home.appointment_booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.FragmentDoctorDetailsBinding
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils


class DoctorDetailsFragment : Fragment() {

    private var _binding: FragmentDoctorDetailsBinding? = null
    private val detailsViewModel by viewModels<DoctorDetailsViewModel> { ViewModelFactory() }
    private val binding get() = _binding!!
    private val args: DoctorDetailsFragmentArgs by navArgs()
    private var doctor: User = User()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDoctorDetailsBinding.inflate(inflater, container, false)

        initDoctor()
        initViews()
        return binding.root
    }

    private fun initDoctor() {
        val doctorDetails = args.doctorDetails
        detailsViewModel.initDoctor(doctorDetails)
    }

    private fun initViews() {
        binding.run {
            doctorName.text = detailsViewModel.getDoctor().Name
            doctorSpecialization.text = detailsViewModel.getDoctor().Specialist
            Logger.debugLog("Doctor is ${detailsViewModel.getDoctor().Address}")
            detailsViewModel.getDoctor().let {
                if (it.Address != "null")
                    doctorAddress.text = it.Address.toString()
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            bookAppointment.setOnClickListener {
                val action =
                    DoctorDetailsFragmentDirections.actionDoctorDetailsFragmentToAppointmentBookingFragment(
                        detailsViewModel.getDoctor()
                    )
                findNavController().navigate(action)
            }
            emailId.setOnClickListener {
                Utils.sendEmailToGmail(
                    activity = requireActivity(),
                    subject = "",
                    body = "",
                    email = detailsViewModel.getDoctor().Email
                )
            }
            phoneCall.setOnClickListener {
                Utils.makePhoneCall(
                    activity = requireActivity(),
                    phone = detailsViewModel.getDoctor().Phone
                )
            }
        }
        Logger.debugLog("Doctor is ${detailsViewModel.getDoctor()}")
    }


}