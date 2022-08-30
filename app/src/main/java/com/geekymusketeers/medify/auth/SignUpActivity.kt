package com.geekymusketeers.medify.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.widget.Toast
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var fd: FirebaseDatabase

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        fd = FirebaseDatabase.getInstance()
        db = fd.getReference("Users")

        var passwordVisible = false

        binding.signuppass.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.signuppass.getRight() - binding.signuppass.getCompoundDrawables()
                        .get(Right).getBounds().width()
                ) {
                    val selection: Int = binding.signuppass.getSelectionEnd()
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        binding.signuppass.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility_off,
                            0
                        )
                        //for hide password
                        binding.signuppass.setTransformationMethod(PasswordTransformationMethod.getInstance())
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        binding.signuppass.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility,
                            0
                        )
                        //for show password
                        binding.signuppass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                        passwordVisible = true
                    }
                    binding.signuppass.setLongClickable(false) //Handles Multiple option popups
                    binding.signuppass.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.confirmpass.setOnTouchListener { view, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.confirmpass.getRight() - binding.confirmpass.getCompoundDrawables()
                        .get(Right).getBounds().width()
                ) {
                    val selection: Int = binding.confirmpass.getSelectionEnd()
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        binding.confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility_off,
                            0
                        )
                        //for hide password
                        binding.confirmpass.setTransformationMethod(PasswordTransformationMethod.getInstance())
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        binding.confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility,
                            0
                        )
                        //for show password
                        binding.confirmpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                        passwordVisible = true
                    }
                    binding.confirmpass.setLongClickable(false) //Handles Multiple option popups
                    binding.confirmpass.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // For Sign In text
        binding.gotosignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        // For Sign Up button
        binding.signupbtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.signupemail.text.toString()
            val pass = binding.signuppass.text.toString()
            val confirmpass = binding.confirmpass.text.toString()

            //Create user object
            val user = User(name, email)

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()
            ) {
                if (pass == confirmpass && pass.length > 8) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //get the generated uid
                            val u = firebaseAuth.currentUser

                            //add user data in the Realtime Database
                            db.child(u?.uid!!).setValue(user).addOnCompleteListener { it1 ->
                                if (it1.isSuccessful) {
                                    u.sendEmailVerification()
                                    Toast.makeText(
                                        this,
                                        "Email Verification sent to your mail",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this, SignInActivity::class.java))
                                }
                            }

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Password is not matching!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter the details!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}