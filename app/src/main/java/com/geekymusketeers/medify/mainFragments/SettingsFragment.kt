package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekymusketeers.medify.prescription.AddPrescriptionActivity
import com.geekymusketeers.medify.profile.ProfileActivity
import com.geekymusketeers.medify.auth.SignIn_Activity
import com.geekymusketeers.medify.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference
        getDataFromSharedPreference()

        binding.logoutButton.setOnClickListener {
            logoutFun()
        }

        binding.profile.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
        }
        binding.createUPI.setOnClickListener {
            startActivity(Intent(requireActivity(), UPImanager::class.java))
        }
        binding.updatePrescription.setOnClickListener {
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
                    TODO("Not yet implemented")
                }
            })
        }
        return binding.root
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