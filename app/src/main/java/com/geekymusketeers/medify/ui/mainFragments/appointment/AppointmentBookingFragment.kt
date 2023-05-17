package com.geekymusketeers.medify.ui.mainFragments.appointment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentAppointmentBookingBinding
import com.geekymusketeers.medify.model.Summary
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class AppointmentBookingFragment : Fragment() {

    private var _binding: FragmentAppointmentBookingBinding? = null
    private val binding get() = _binding!!
    private val args: AppointmentBookingFragmentArgs by navArgs()
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var mapOfDiseasesList: HashMap<String, ArrayList<String>>
    private lateinit var diseaseValue: HashMap<String, Float>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentBookingBinding.inflate(inflater, container, false)

        initView()

        diseaseValue = Utils.setDiseaseValues(requireContext())

//        val doctorType = args.appointmentDetails.Specialist

        // Date Picker
        binding.selectDate.setOnClickListener {
            handleDatePicker()
        }

        // Booking Appointment
        binding.btnFinalbook.onSlideCompleteListener =
            object : SlideToActView.OnSlideCompleteListener {
                override fun onSlideComplete(view: SlideToActView) {
                    bookAppointment(args.doctorDetails.Specialist)
                }
            }


        val items: List<String> = mapOfDiseasesList[args.doctorDetails.Specialist]!!
        val adapter = ArrayAdapter(requireContext(), R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Severe Pain", "Mild Pain", "No Pain")
        val situationAdapter = ArrayAdapter(requireContext(), R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)

        val timeItems = listOf(
            "9:00 AM - 11:00 AM",
            "11:00 AM - 13:00 PM",
            "17:00 PM - 19:00 PM",
            "19:00 PM - 22:OO PM"
        )
        val timeAdapter = ArrayAdapter(requireContext(), R.layout.list_items, timeItems)
        binding.timeDropdown.setAdapter(timeAdapter)

        return binding.root
    }

    private fun bookAppointment(doctorType: String?) {
        val conditionValue = Utils.setConditionValue(requireContext())

        val totalPoint: Int

        val doctorUid = args.doctorDetails.UID
        val doctorName = args.doctorDetails.Name
        val doctorEmail = args.doctorDetails.Email
        val doctorPhone = args.doctorDetails.Phone

        val userName = sharedPreference.getString("name", "").toString()
        val userPhone = sharedPreference.getString("phone", "").toString()
        val userid = sharedPreference.getString("uid", "").toString()
        val userPrescription = sharedPreference.getString("prescription", "").toString()

        val date = binding.selectDate.text.toString()
        val time = binding.timeDropdown.text.toString()
        val disease = binding.diseaseDropdown.text.toString()
        val situation = binding.situationDropdown.text.toString()
        val rightNow = Calendar.getInstance()
        val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        val firstComeFirstServe = 1 + (0.1 * ((currentHourIn24Format / 10) + 1))

        Logger.debugLog("firstComeFirstServe: $firstComeFirstServe")
        Logger.debugLog("diseaseValue: ${diseaseValue[disease]}")
        Logger.debugLog("conditionValue: ${conditionValue[situation]}")
        Logger.debugLog("currentHourIn24Format: $currentHourIn24Format")

        var temp = diseaseValue[disease]!!
        Logger.debugLog("temp value after adding disease value: $temp")
        temp += conditionValue[situation]!!
        Logger.debugLog("temp value after adding condition value: $temp")
        totalPoint = (temp * firstComeFirstServe).toInt()
        Logger.debugLog("totalPoint: $totalPoint")

        val appointmentD: HashMap<String, String> = HashMap() //define empty hashmap
        appointmentD["PatientName"] = userName
        appointmentD["PatientPhone"] = userPhone
        appointmentD["Time"] = time
        appointmentD["Date"] = date
        appointmentD["Disease"] = disease
        appointmentD["PatientCondition"] = situation
        appointmentD["Prescription"] = userPrescription
        appointmentD["TotalPoints"] = totalPoint.toString().trim()
        appointmentD["DoctorUID"] = doctorUid.toString()
        appointmentD["PatientID"] = userid

        val appointmentP: HashMap<String, String> = HashMap() //define empty hashmap
        appointmentP["DoctorUID"] = doctorUid.toString()
        appointmentP["DoctorName"] = doctorName.toString()
        appointmentP["DoctorPhone"] = doctorPhone.toString()
        appointmentP["Date"] = date
        appointmentP["Time"] = time
        appointmentP["Disease"] = disease
        appointmentP["PatientCondition"] = situation
        appointmentP["Prescription"] = userPrescription
        appointmentP["PatientID"] = userid

        val appointmentDB_Doctor =
            FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid!!)
                .child("DoctorsAppointments").child(date)
        appointmentDB_Doctor.child(userid).setValue(appointmentD)

        val appointmentDB_User_Doctor =
            FirebaseDatabase.getInstance().getReference("Users").child(doctorUid)
                .child("DoctorsAppointments").child(date)
        appointmentDB_User_Doctor.child(userid).setValue(appointmentD)

        val appointmentDB_Patient =
            FirebaseDatabase.getInstance().getReference("Users").child(userid)
                .child("PatientsAppointments").child(date)
        appointmentDB_Patient.child(doctorUid).setValue(appointmentP)
//
        val summary = Summary(
            doctorName = doctorName.toString(),
            doctorSpeciality = doctorType.toString(),
            doctorEmail = doctorEmail.toString(),
            doctorPhone = doctorPhone.toString(),
            appointmentDate = date,
            appointmentTime = time,
            disease = disease,
            painLevel = situation,
            totalPoint = totalPoint
        )

//        val intent = Intent(this@AppointmentBooking, BookingDoneActivity::class.java)
//        intent.putExtra("summary", summary)
//        startActivity(intent)
//        finish()

        val actions =
            AppointmentBookingFragmentDirections.actionAppointmentBookingFragmentToBookingSummaryFragment(
                summary
            )
        findNavController().navigate(actions)
    }

    private fun handleDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {

            // disable past dates
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointForward.now())
            setCalendarConstraints(constraintsBuilder.build())

            // set the minimum selectable date to today's date
            val calendar = Calendar.getInstance()
            setSelection(calendar.timeInMillis)

        }.build()
        datePicker.show(requireActivity().supportFragmentManager, "DatePicker")


        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.selectDate.setText(date)
        }

    }

    private fun initView() {
        sharedPreference = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        mapOfDiseasesList = Utils.initializeSpecializationWithDiseasesLists()
    }

}