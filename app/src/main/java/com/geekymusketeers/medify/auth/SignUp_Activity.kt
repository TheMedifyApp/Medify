package com.geekymusketeers.medify.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var fd: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDoctor = intent.extras!!.getString("isDoctor")
        val age = intent.extras!!.getString("age")
        initialization()

        binding.toSignIn.setOnClickListener {
            val intent = Intent(this, SignIn_Activity::class.java)
            startActivity(intent)
        }

        binding.createAccount.setOnClickListener {
            val name = binding.SignUpName.text.toString().trim()
            val email = binding.SignUpEmail.text.toString().trim()
            val phone = binding.SignUpPhone.text.toString().trim()
            val password = binding.SignUpPassword.text.toString().trim()

            //Create user object
            val user = User(name, email, phone, isDoctor, age)

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length > 7) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val u = firebaseAuth.currentUser

                            //add user data in the Realtime Database
                            db.child(u?.uid!!).setValue(user).addOnCompleteListener { it1 ->
                                if (it1.isSuccessful) {
                                    u.sendEmailVerification()
                                    Toast.makeText(this, "Email Verification sent to your mail", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, SignIn_Activity::class.java))

                                    if (isDoctor == "Doctor") {
                                        fd.getReference(isDoctor).child(u.uid).setValue(user).addOnSuccessListener {
                                            Toast.makeText(this, "Doctor added", Toast.LENGTH_SHORT).show()
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