package com.geekymusketeers.medify.introduction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.geekymusketeers.medify.HomeActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.auth.SignIn_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Splashscreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance(); //initialize Firebase Auth
        val currentUser: FirebaseUser? = auth.getCurrentUser() //Get the current user

        if (currentUser == null) SendUserToLoginActivity() //If the user has not logged in, send them to On-Boarding Activity
        else {
            //If user was logged in last time
            Handler().postDelayed({
                val loginIntent: Intent
                if (currentUser.isEmailVerified) loginIntent = Intent(this, HomeActivity::class.java) //If the user email is verified
                else loginIntent = Intent(this, SignIn_Activity::class.java) //If the user email is not verified
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()
            }, 2000)
        }
    }

    //If the user has not logged in, send them to On-Boarding Activity
    private fun SendUserToLoginActivity() {

//        // when this activity is about to be launch we need to check if its opened before or not
//        if (restorePrefData()) {

            Handler().postDelayed({
                val loginIntent = Intent(this, SignIn_Activity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()
            }, 2000)
//        } else {
//            Handler().postDelayed({
//                val loginIntent = Intent(this, Onboarding::class.java)
//                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(loginIntent)
//                finish()
//            }, 2000)
//        }
    }

//    private fun restorePrefData(): Boolean {
//        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
//        return pref.getBoolean("isIntroOpened", false)
//    }
}