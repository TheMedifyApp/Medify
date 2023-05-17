package com.geekymusketeers.medify.ui.mainFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.adapter.DoctorListAdapter
import com.geekymusketeers.medify.databinding.FragmentHomeBinding
import com.geekymusketeers.medify.databinding.RatingDisputeLayoutBinding
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.DialogUtil.createBottomSheet
import com.geekymusketeers.medify.utils.DialogUtil.setBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils
import com.geekymusketeers.medify.utils.Utils.toStringWithoutSpaces
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorListAdapter: DoctorListAdapter
    private lateinit var doctorList: MutableList<User>

    //Current User's data
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private lateinit var userPosition: String
    private lateinit var userType: String
    private lateinit var userID: String
    private var userPrescription: String = "false"
    private var totalRatings = 5f

    //Searched doctor's data
    private lateinit var searchedName: String
    private lateinit var searchedEmail: String
    private lateinit var searchedPhone: String
    private lateinit var searchedData: String
    private lateinit var searchedUID: String
    private lateinit var searchedType: String

    private lateinit var sharedPreference: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        db = FirebaseDatabase.getInstance().reference
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        handleDoctorSearch()
        handleChipFilter()
        getDataFromSharedPreference()
        getUserRatings()
        getDoctorsList()

        doctorListAdapter = DoctorListAdapter {
            if (userPrescription != "false") {

                if (totalRatings < 3.0f) {
                    showAlertDialog()
                    return@DoctorListAdapter
                }

                val action = HomeFragmentDirections.actionHomeToDoctorDetailsFragment(it)
                findNavController().navigate(action)
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.please_upload_your_prescription_in_settings_tab),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recyclerView = binding.doctorList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = doctorListAdapter

        return binding.root
    }

    private fun handleChipFilter() {

        val listOfDoctorSpecialists = mutableListOf<String>()
        resources.getStringArray(R.array.all_specialists).forEach {
            listOfDoctorSpecialists.add(it)
        }

        //Create the chips dynamically from the filter response
        for ((count, filter) in listOfDoctorSpecialists.withIndex()) {
            val newChip = Chip(requireContext(), null, R.attr.FilterChips)
            newChip.text = filter
            newChip.id = count
            newChip.elevation = 2F
            binding.chipGroup.addView(newChip)
        }

        binding.chipGroup.check(0)

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedId ->

            if (checkedId.isNotEmpty()) {
                val chip: Chip? = group.findViewById(checkedId[0])
                val selectedSpecialist = chip?.text.toString()

                if (selectedSpecialist.isNotEmpty()) {
                    if (selectedSpecialist == "All") {
                        doctorListAdapter.addItems(doctorList)
                    } else {
                        val searchedList = doctorList.filter {
                            it.Specialist!!.trim().lowercase().toStringWithoutSpaces()
                                .contains(
                                    selectedSpecialist.lowercase().trim().toStringWithoutSpaces(),
                                    true
                                )
                        }
                        doctorListAdapter.addItems(searchedList)
                    }
                } else {
                    doctorListAdapter.addItems(doctorList)
                }
            }
        }
    }

    private fun handleDoctorSearch() {
        binding.doctorData.addTextChangedListener {
            searchedData = binding.doctorData.text.toString().trim()
            if (searchedData.isNotEmpty()) {
                val searchedList = doctorList.filter {
                    containsName(it.Name!!) || containsEmail(it.Email!!) || containsPhone(it.Phone!!)
                }
                doctorListAdapter.addItems(searchedList)
            } else {
                doctorListAdapter.addItems(doctorList)
            }
        }
    }

    private fun containsPhone(phone: String): Boolean {
        return phone.trim().lowercase().toStringWithoutSpaces().contains(
            searchedData.lowercase().trim().toStringWithoutSpaces()
        )
    }

    private fun containsEmail(email: String): Boolean {
        return email.trim().lowercase().toStringWithoutSpaces()
            .contains(
                searchedData.lowercase().trim().toStringWithoutSpaces(),
                true
            )
    }

    private fun containsName(name: String): Boolean {
        return name.trim().lowercase().toStringWithoutSpaces()
            .contains(searchedData.lowercase().trim().toStringWithoutSpaces(), true)
    }


    private fun getUserRatings() {
        FirebaseDatabase.getInstance().reference.child("Users").child(userID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("totalRating").exists()) {
                        totalRatings =
                            snapshot.child("totalRating").value.toString().toFloat()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getDoctorsList() {
        doctorList = mutableListOf()
        db.child("Doctor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() {
                    val age: String = it.child("age").value.toString().trim()
                    val doctor: String = it.child("doctor").value.toString().trim()
                    val email: String = it.child("email").value.toString().trim()
                    val phone: String = it.child("phone").value.toString().trim()
                    val name: String = it.child("name").value.toString().trim()
                    val specialist: String = it.child("specialist").value.toString().trim()
                    val uid: String = it.child("uid").value.toString().trim()

                    val doctorItem = User(
                        Name = name,
                        Email = email,
                        Phone = phone,
                        UID = uid,
                        isDoctor = doctor,
                        Age = age,
                        Specialist = specialist
                    )

                    Logger.debugLog("Doctors are from Firebase are: $doctorItem")
                    doctorList.add(doctorItem)
                }
                doctorListAdapter.addItems(doctorList)

            }

            override fun onCancelled(error: DatabaseError) {
                Logger.debugLog("Error in getting doctors list: ${error.message}")
            }
        })
    }


    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ineligible to book an appointment")
        builder.setMessage("You need to have a rating of 3 or above to book an appointment")

        builder.setPositiveButton("File a Dispute") { dialog, _ ->

            showBottomSheetForDispute()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showBottomSheetForDispute() {
        val dialog = RatingDisputeLayoutBinding.inflate(layoutInflater)
        val bottomSheet = requireContext().createBottomSheet()
        dialog.apply {
            this.apply {
                btnSubmitDispute.setOnClickListener {
                    Logger.debugLog("Dispute Clicked")
                    val subject = disputeSubject.text.toString().trim()
                    val reason = disputeReason.text.toString().trim()

                    if (subject.isEmpty() || reason.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Please fill all the fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    Utils.sendEmailToGmail(
                        requireActivity(),
                        subject,
                        reason,
                        Constants.supportEmail
                    )
                    bottomSheet.dismiss()
                }
            }
        }
        dialog.root.setBottomSheet(bottomSheet)
    }


//    override fun onStart() {
//        super.onStart()
//        Handler().postDelayed({
//            getDataFromSharedPreference()
//        }, 1000)
//    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun getDataFromSharedPreference() {
        userID = sharedPreference.getString("uid", "Not found").toString()
        userName =
            sharedPreference.getString("name", "Not found").toString()
        userEmail =
            sharedPreference.getString("email", "Not found").toString()
        userPhone =
            sharedPreference.getString("phone", "Not found").toString()
        userPosition =
            sharedPreference.getString("isDoctor", "Not fount").toString()
        userPrescription =
            sharedPreference.getString("prescription", "false").toString()

        if (userPosition == "Doctor")
            binding.nameDisplay.text = "Dr. $userName"
        else
            binding.nameDisplay.text = userName
    }
}
