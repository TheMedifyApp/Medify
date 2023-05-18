package com.geekymusketeers.medify.ui.auth.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.geekymusketeers.medify.databinding.ActivityForgotPasswordBinding
import com.geekymusketeers.medify.ui.auth.signInScreen.SignInScreen
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.ForgotPassButton.setOnClickListener {
            val email = binding.ForgotPassEmail.text.toString().trim()
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this,"Email has been send",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInScreen::class.java))
                }else
                    Toast.makeText(this, "Email failed to send", Toast.LENGTH_SHORT).show()
            }
        }
    }
}