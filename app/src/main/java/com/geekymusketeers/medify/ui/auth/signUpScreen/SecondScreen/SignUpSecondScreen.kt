package com.geekymusketeers.medify.ui.auth.signUpScreen.SecondScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.databinding.ActivitySignUpBinding
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.Specialist
import com.geekymusketeers.medify.ui.auth.signInScreen.SignIn_Activity

class SignUpSecondScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val signUpSecondViewModel by viewModels<SignUpSecondViewModel> { ViewModelFactory() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpIntentValue()
        initObservers()
        initViews()

    }

    @Suppress("DEPRECATION")
    private fun setUpIntentValue() {
        val user = intent.getParcelableExtra<User>("user")
        val password = intent.extras!!.getString("password")
        signUpSecondViewModel.apply {
            setUpUser(user!!)
            setUserPassword(password!!)
        }
    }

    private fun initViews() {
        binding.run {
            addressEditText.setUserInputListener {
                signUpSecondViewModel.setUserAddress(it)
            }
            isDoctorSpinnerEditText.getSelectedItemFromDialog {
                signUpSecondViewModel.setUserIsDoctor(Doctor.isDoctor(it))
            }
            specializationSpinnerEditText.getSelectedItemFromDialog {
                signUpSecondViewModel.setUserSpecialization(it)
            }
            createAccountButton.setOnClickListener {
                signUpSecondViewModel.createAccount()
            }
        }
    }

    private fun initObservers() {
        setUpIsDoctorDialog()
        setUpSpecializationDialog()
        signUpSecondViewModel.run {
            userIsDoctor.observe(this@SignUpSecondScreen) {
                binding.specializationSpinnerEditText.visibility =
                    if (it == Doctor.IS_DOCTOR) View.VISIBLE else View.GONE
            }
            userAccountCreationLiveData.observe(this@SignUpSecondScreen) {
                if (it) {
                    createUserDatabase()
                }
            }
            enableCreateAccountButtonLiveData.observe(this@SignUpSecondScreen) {
                binding.createAccountButton.isEnabled = it
                binding.createAccountButton.setButtonEnabled(it)
            }
            errorLiveData.observe(this@SignUpSecondScreen) {
                Toast.makeText(this@SignUpSecondScreen, it, Toast.LENGTH_SHORT).show()
            }
            userDataBaseUpdate.observe(this@SignUpSecondScreen) {
                if (it) {
                    Toast.makeText(
                        this@SignUpSecondScreen,
                        getString(R.string.account_created_successfully),
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@SignUpSecondScreen, SignIn_Activity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

    }

    private fun setUpIsDoctorDialog() {
        val isDoctorItems = listOf(
            Doctor.IS_DOCTOR.toItemString(),
            Doctor.IS_NOT_DOCTOR.toItemString()
        )
        binding.isDoctorSpinnerEditText.setUpDialogData(
            isDoctorItems,
            "Please select your profession",
            null, null, null
        )
    }

    private fun setUpSpecializationDialog() {
        val specializationItems = listOf(
            Specialist.ALLERGISTS.toItemString(),
            Specialist.ENT_SPECIALIST.toItemString(),
            Specialist.CARDIOLOGIST.toItemString(),
            Specialist.DENTIST.toItemString(),
            Specialist.ENT_SPECIALIST.toItemString(),
            Specialist.OBSTETRICIAN_GYNAECOLOGIST.toItemString(),
            Specialist.ORTHOPAEDIC_SURGEON.toItemString(),
            Specialist.PSYCHIATRIST.toItemString(),
            Specialist.RADIOLOGIST.toItemString(),
            Specialist.PULMONOLOGIST.toItemString(),
            Specialist.NEUROLOGIST.toItemString(),
            Specialist.ALLERGISTS.toItemString(),
            Specialist.GASTROENTEROLOGISTS.toItemString(),
        )
        binding.specializationSpinnerEditText.setUpDialogData(
            specializationItems,
            "Please select your Specialization",
            null, null, null
        )
    }
}