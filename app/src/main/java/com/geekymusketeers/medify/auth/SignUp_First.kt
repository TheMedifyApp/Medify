package com.geekymusketeers.medify.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.databinding.ActivitySignUpFirstBinding
import io.ghyeok.stickyswitch.widget.StickySwitch


class SignUp_First : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.nextButton.setOnClickListener {

            val intent = Intent(this, SignUp_Activity::class.java)
            intent.putExtra("isDoctor", (binding.stickySwitch.getText() == "Doctor").toString())
            intent.putExtra("age", binding.ageInput.text.toString().trim())
            startActivity(intent)

        }

    }
}