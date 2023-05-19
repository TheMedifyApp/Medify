package com.geekymusketeers.medify.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geekymusketeers.medify.databinding.ActivityProfileBinding
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.SharedPrefsExtension.getUserFromSharedPrefs
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreference : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = baseContext.getSharedPreferences(Constants.UserData, Context.MODE_PRIVATE)

        getUserData()

        binding.ProfileToEdit.setOnClickListener {
            startActivity(Intent(baseContext, EditProfileActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val user = sharedPreference.getUserFromSharedPrefs()
        binding.name.text = "Name :  ${user.Name}"
        binding.age.text = "Age : ${user.Age}"
        binding.email.text = "Email : ${user.Email}"
        binding.phone.text = "Phone Number : ${user.Phone}"

    }
}