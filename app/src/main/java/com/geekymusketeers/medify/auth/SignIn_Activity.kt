package com.geekymusketeers.medify.auth

import android.annotation.SuppressLint
import android.content.Context
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
import com.geekymusketeers.medify.encryptionHelper.Encryption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignIn_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        val sharedPreference =  getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        // Hide and Show Password
        var passwordVisible = false
        binding.SignInPassword.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.SignInPassword.getRight() - binding.SignInPassword.getCompoundDrawables().get(Right).getBounds().width()) {
                    val selection: Int = binding.SignInPassword.getSelectionEnd()
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        binding.SignInPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
                        //for hide password
                        binding.SignInPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        binding.SignInPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0)
                        //for show password
                        binding.SignInPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance())
                        passwordVisible = true
                    }
                    binding.SignInPassword.setLongClickable(false) //Handles Multiple option popups
                    binding.SignInPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        binding.toSignUp.setOnClickListener {
            val intent = Intent(this, SignUp_First::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.SignInEmail.text.toString()
            val password = binding.SignInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length > 7) {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val u = firebaseAuth.currentUser
                            if (u?.isEmailVerified!!) {

                                val db = FirebaseDatabase.getInstance().reference
                                val encryption = Encryption.getDefault("Key", "Salt", ByteArray(16))

                                db.child("Users").child(u.uid).addValueEventListener(object: ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {

                                        editor.putString("uid", snapshot.child("uid").value.toString().trim())
                                        editor.putString("name", snapshot.child("name").value.toString().trim())
                                        editor.putString("age", snapshot.child("age").value.toString().trim())
                                        editor.putString("email", snapshot.child("email").value.toString().trim())
                                        editor.putString("phone", snapshot.child("phone").value.toString().trim())
                                        editor.putString("isDoctor", snapshot.child("doctor").value.toString().trim())
                                        editor.putString("specialist",snapshot.child("specialist").value.toString().trim())
                                        editor.putString("stats", snapshot.child("stats").value.toString().trim())
                                        editor.putString("prescription", snapshot.child("prescription").value.toString().trim())
                                        editor.putString("upi", snapshot.child(encryption.encrypt("nulla")).value.toString().trim())
                                        editor.apply()

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })

                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()

                            } else {
                                u.sendEmailVerification()
                                Toast.makeText(
                                    this,
                                    "Email Verification sent to your mail",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Password length must be greater than 8", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Please enter the details!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun initialization() {
        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
    }
}