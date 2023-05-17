package com.geekymusketeers.medify.ui.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.ui.prescription.AddPrescriptionActivity
import com.geekymusketeers.medify.ui.profile.ProfileActivity
import com.geekymusketeers.medify.ui.auth.SignIn_Activity
import com.geekymusketeers.medify.databinding.FragmentSettingsBinding
import com.geekymusketeers.medify.model.SettingsItem
import com.geekymusketeers.medify.model.SettingsState
import com.geekymusketeers.medify.ui.adapter.SettingsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private  var settingsItemList: MutableList<SettingsItem> = mutableListOf()
    private lateinit var settingsItemAdapter: SettingsAdapter
    private lateinit var db: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference
        getDataFromSharedPreference()

        handleOperations()

//        binding.logoutButton.setOnClickListener {
//            logoutFun()
//        }
//
//        binding.profile.setOnClickListener {
//            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
//        }
//        binding.createUPI.setOnClickListener {
//            startActivity(Intent(requireActivity(), UPImanager::class.java))
//        }
//        binding.updatePrescription.setOnClickListener {
//            startActivity(Intent(requireActivity(), AddPrescriptionActivity::class.java))
//            db.child("Users").child(userID).child("Prescription").addValueEventListener(object :
//                ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val presURL = snapshot.child("fileurl").value.toString().trim()
//                    val editor = sharedPreferences.edit()
//                    editor.putString("prescription", presURL)
//                    editor.apply()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
        return binding.root
    }

    private fun handleOperations() {
        setupSettings()
    }

    private fun setupSettings() {
        settingsItemList.apply {
            add(SettingsItem(0, R.drawable.edit_profile,"Edit Profile"))
            add(SettingsItem(1, R.drawable.upload_prescription, "Upload Prescription"))
            add(SettingsItem(2, R.drawable.upi_id, "UPI QR"))
            add(SettingsItem(3, R.drawable.info, "About Us"))
            add(SettingsItem(4, R.drawable.feedback, "Feedback"))
            add(SettingsItem(5, R.drawable.need_help, "Need help?"))
            add(SettingsItem(6, R.drawable.logout, "Logout"))

        }

        settingsItemAdapter = SettingsAdapter(requireContext(), settingsItemList as ArrayList<SettingsItem>) {

            when(it) {
                SettingsState.getSettingsState(SettingsState.TO_EDIT_PROFILE) -> {
                    startActivity(Intent(requireActivity(), ProfileActivity::class.java))
                }
                SettingsState.getSettingsState(SettingsState.TO_UPLOAD_PRESCRIPTION) -> {
                    startActivity(Intent(requireActivity(), AddPrescriptionActivity::class.java))
                    db.child("Users").child(userID).child("Prescription").addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val presURL = snapshot.child("fileurl").value.toString().trim()
                            val editor = sharedPreferences.edit()
                            editor.putString("prescription", presURL)
                            editor.apply()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Do nothing
                        }
                    })
                }
                SettingsState.getSettingsState(SettingsState.TO_UPI_QR) -> {
                    startActivity(Intent(requireActivity(), UPImanager::class.java))
                }
                SettingsState.getSettingsState(SettingsState.TO_ABOUT_US) -> {
                    aboutUs()
                }
                SettingsState.getSettingsState(SettingsState.TO_FEEDBACK) -> {
                    openMail()
                }
                SettingsState.getSettingsState(SettingsState.TO_NEED_HELP) -> {
                    needHelp()
                }
                SettingsState.getSettingsState(SettingsState.TO_LOGOUT) -> {
                    logoutFun()
                }
            }
        }

        binding.settingsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = settingsItemAdapter
        }
    }

    private fun needHelp() {

    }

    private fun aboutUs() {

    }

    private fun openMail() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(requireContext().resources.getString(R.string.mailTo))
        startActivity(intent)
    }

    @SuppressLint("CommitPrefEdits")
    private fun logoutFun() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, SignIn_Activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()

    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun getDataFromSharedPreference() {
        userID = sharedPreferences.getString("uid","Not found").toString()
    }
}