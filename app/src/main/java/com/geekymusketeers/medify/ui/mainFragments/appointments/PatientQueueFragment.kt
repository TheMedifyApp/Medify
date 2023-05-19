package com.geekymusketeers.medify.ui.mainFragments.appointments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.FragmentPatientQueueBinding
import com.geekymusketeers.medify.databinding.RatingModalBinding
import com.geekymusketeers.medify.model.DoctorAppointment
import com.geekymusketeers.medify.model.Rating
import com.geekymusketeers.medify.ui.adapter.PatientQueueAdapter
import com.geekymusketeers.medify.utils.DialogUtil.createBottomSheet
import com.geekymusketeers.medify.utils.DialogUtil.setBottomSheet
import java.text.SimpleDateFormat


class PatientQueueFragment : Fragment() {

    private var _binding: FragmentPatientQueueBinding? = null
    private val binding get() = _binding!!
    private val patientQueueViewModel by viewModels<PatientQueueViewModel> { ViewModelFactory() }
    private val args: PatientQueueFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var patientQueueAdapter: PatientQueueAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientQueueBinding.inflate(inflater, container, false)

        initObservers()
        initViews()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.run {
            recyclerView = binding.appointmentRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            patientQueueAdapter = PatientQueueAdapter(args.doctorUserID) {
                showRatingBottomSheet(it)
            }
            recyclerView.adapter = patientQueueAdapter
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
        val bottomSheet = requireContext().createBottomSheet()
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
                    patientQueueViewModel.updateRating(rating)
                    bottomSheet.dismiss()
                }
            }
        }
        dialog.root.setBottomSheet(bottomSheet)
    }

    private fun initObservers() {
        patientQueueViewModel.run {
            setPassedData(
                args.doctorUserID,
                args.selectedDate,
                args.hideSelectedDate
            )
            doctorUserID.observe(viewLifecycleOwner) {
                getPatientsListFromFirebase()
                binding.selectedDateText.text = "Selected Date: ${selectedDate.value}"
            }
            patientsListLiveData.observe(viewLifecycleOwner) {
                patientQueueAdapter.setData(it as ArrayList<DoctorAppointment>)
            }
        }
    }

}