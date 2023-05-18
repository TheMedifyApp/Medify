package com.geekymusketeers.medify.ui.introduction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geekymusketeers.medify.ui.HomeActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.ui.auth.signInScreen.SignIn_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splashscreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance(); //initialize Firebase Auth
        val currentUser: FirebaseUser? = auth.currentUser //Get the current user

        if (currentUser == null) sendUserToLoginActivity() //If the user has not logged in, send them to On-Boarding Activity
        else {
            //If user was logged in last time
            CoroutineScope(Dispatchers.Main).launch {
                //If the user has logged in, send them to Home Activity
                delay(2000L)
                val loginIntent: Intent =
                    if (currentUser.isEmailVerified) Intent(
                        this@Splashscreen,
                        HomeActivity::class.java
                    ) //If the user email is verified
                    else Intent(
                        this@Splashscreen,
                        SignIn_Activity::class.java
                    ) //If the user email is not verified
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()
            }
        }
    }

    private fun sendUserToLoginActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000L)
            val loginIntent = Intent(
                this@Splashscreen,
                SignIn_Activity::class.java
            )
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(loginIntent)
            finish()
        }
    }

}