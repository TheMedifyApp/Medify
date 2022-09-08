package com.geekymusketeers.medify.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.geekymusketeers.medify.auth.User
import com.geekymusketeers.medify.databinding.ActivityProfileBinding
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var dbreference : DatabaseReference
    private lateinit var user: User
    private lateinit var sharedPreference : SharedPreferences
    private lateinit var userName : String
    private lateinit var userEmail : String
    private lateinit var userPhone : String
    private lateinit var userID: String
    private lateinit var age : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        getUserData()

        binding.ProfileToEdit.setOnClickListener {
            startActivity(Intent(baseContext, EditProfileActivity::class.java))
        }

    }

    private fun getUserData() {

        getDataFromSharedPreference()
        dbreference = FirebaseDatabase.getInstance().reference
        binding.name.text = "Name :  $userName"
        binding.age.text = "Age : $age"
        binding.email.text = "Email : $userEmail"
        binding.phone.text = "Phone Number : $userPhone"

    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataFromSharedPreference() {
        userID = sharedPreference.getString("uid","Not found").toString()
        userName = sharedPreference.getString("name","Not found").toString()
        userEmail = sharedPreference.getString("email","Not found").toString()
        userPhone = sharedPreference.getString("phone","Not found").toString()
        age = sharedPreference.getString("age", "Not found").toString().trim()

    }
}