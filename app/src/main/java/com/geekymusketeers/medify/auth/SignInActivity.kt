package com.geekymusketeers.medify.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Toast
import com.geekymusketeers.medify.HomeActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show & Hide password function
        var passwordVisible = false
        binding.loginpassword.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.loginpassword.getRight() - binding.loginpassword.getCompoundDrawables()
                        .get(Right).getBounds().width()
                ) {
                    val selection: Int = binding.loginpassword.getSelectionEnd()
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        binding.loginpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility_off,
                            0
                        )
                        //for hide password
                        binding.loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        binding.loginpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility,
                            0
                        )
                        //for show password
                        binding.loginpassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance())
                        passwordVisible = true
                    }
                    binding.loginpassword.setLongClickable(false) //Handles Multiple option popups
                    binding.loginpassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // For Sign Up text
        binding.signuptext.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signin.setOnClickListener {
            val email = binding.loginemail.text.toString()
            val password = binding.loginpassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        val u = firebaseAuth.currentUser
                        if (u?.isEmailVerified!!){
                            startActivity(Intent(this, HomeActivity::class.java))
                        }else{
                            u.sendEmailVerification()
                            Toast.makeText(this, "Email Verification sent to your mail", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please enter the details!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}