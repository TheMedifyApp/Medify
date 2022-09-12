package com.geekymusketeers.medify.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.RemoveCountryCode
import com.geekymusketeers.medify.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var fd: FirebaseDatabase

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDoctor = intent.extras!!.getString("isDoctor")
        val age = intent.extras!!.getString("age")
        initialization()

        if (isDoctor == "Doctor"){
            binding.menuDoctorType.visibility = View.VISIBLE
        }

        // Hide and Show Password
        var passwordVisible = false
        binding.SignUpPassword.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.SignUpPassword.getRight() - binding.SignUpPassword.getCompoundDrawables()
                        .get(Right).getBounds().width()
                ) {
                    val selection: Int = binding.SignUpPassword.getSelectionEnd()
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        binding.SignUpPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility_off,
                            0
                        )
                        //for hide password
                        binding.SignUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        binding.SignUpPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility,
                            0
                        )
                        //for show password
                        binding.SignUpPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance())
                        passwordVisible = true
                    }
                    binding.SignUpPassword.setLongClickable(false) //Handles Multiple option popups
                    binding.SignUpPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.toSignIn.setOnClickListener {
            val intent = Intent(this, SignIn_Activity::class.java)
            startActivity(intent)
        }

        // Disease List
        val items = listOf("Cardiologist", "Dentist", "ENT specialist", "Obstetrician/Gynaecologist", "Orthopaedic surgeon","Psychiatrists", "Radiologist", "Pulmonologist", "Neurologist", "Allergists", "Gastroenterologists", "Urologists", "Otolaryngologists", "Rheumatologists", "Anesthesiologists")
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.SignUpTypeOfDoctor.setAdapter(adapter)

        binding.createAccount.setOnClickListener {
            val name = binding.SignUpName.text.toString().trim()
            val email = binding.SignUpEmail.text.toString().trim()
            val tempPhone = binding.SignUpPhone.text.toString().trim()
//            val specialist = binding.SignUpTypeOfDoctor.text.toString().trim()
            val specialist = binding.SignUpTypeOfDoctor.text.toString().trim()
            val phone = RemoveCountryCode.remove(tempPhone)
            val password = binding.SignUpPassword.text.toString().trim()



            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length > 7) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val u = firebaseAuth.currentUser
                            val uid = firebaseAuth.currentUser?.uid.toString()
                            //Create user object
                            val statsData = "0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0"
                            val user = User(name, email, phone, uid, isDoctor, age, specialist, statsData, "false")

                            //add user data in the Realtime Database
                            db.child(u?.uid!!).setValue(user).addOnCompleteListener { it1 ->
                                if (it1.isSuccessful) {
                                    u.sendEmailVerification()
                                    Toast.makeText(this, "Email Verification sent to your mail", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, SignIn_Activity::class.java))

                                    if (isDoctor == "Doctor") {
                                        fd.getReference(isDoctor).child(u.uid).setValue(user).addOnSuccessListener {
                                        }

                                    }

                                } else
                                    Log.e("Not successful", "Unsuccessful")
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

    private fun initialization() {
        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        fd = FirebaseDatabase.getInstance()
        db = fd.getReference("Users")
    }
}