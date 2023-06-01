package com.geekymusketeers.medify.ui.auth.signUpScreen.firstScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.ActivitySignUpFirstBinding
import com.geekymusketeers.medify.model.Gender
import com.geekymusketeers.medify.ui.auth.signUpScreen.SecondScreen.SignUpSecondScreen
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.DateTimeExtension
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.Utils.getListOfGenders
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date


class SignUpFirstScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpFirstBinding
    private val signUpFirstViewModel by viewModels<SignUpFirstViewModel> { ViewModelFactory() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initObservers()
        initViews()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        binding.apply {
            nameEditText.setUserInputListener {
                signUpFirstViewModel.setUserName(it)
            }
            emailEditText.setUserInputListener {
                signUpFirstViewModel.setUserEmail(it)
            }
            phoneEditText.setUserInputListener {
                signUpFirstViewModel.setUserPhone(it)
            }
            ageEditText.setLayoutListener(false) {
                showCalendarDialog()
            }
            genderEditText.getSelectedItemFromDialog {
                Logger.debugLog(
                    "Gender value is: $it and converted = ${
                        Gender.getGenderToGender(it).toItemString()
                    }"
                )
                signUpFirstViewModel.setUserGender(Gender.getGenderToGender(it).toItemString())
            }
            passwordEditText.apply {
                setUserInputListener {
                    signUpFirstViewModel.setUserPassword(it)
                }
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources, R.drawable.pass_show, null)
                )

            }
            bottomDualEditText.apply {
                firstTextView.text = getString(R.string.already_have_an_account)
                secondTextView.apply {
                    text = context.getString(R.string.login_text)
                    setOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
            nextButton.setOnClickListener {
                signUpFirstViewModel.setUpUser()
            }
            nextButton.setEndDrawableIcon(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.arrow_forward,
                    null
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun showCalendarDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {

            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointBackward.now())
            setCalendarConstraints(constraintsBuilder.build())

            val calendar = Calendar.getInstance()
            setSelection(calendar.timeInMillis)

        }.build()
        datePicker.show(supportFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val age =
                DateTimeExtension.calculateAge(DateTimeExtension.convertDateToLocalDate(Date(it)))
            signUpFirstViewModel.setUserAge(age)
            binding.ageEditText.setEditTextBox(age.toString())
        }
    }

    private fun initObservers() {
        setUpGenderDialog()
        signUpFirstViewModel.run {
            enableNextButtonLiveData.observe(this@SignUpFirstScreen) {
                binding.nextButton.isEnabled = it
                binding.nextButton.setButtonEnabled(it)
            }
            isValidName.observe(this@SignUpFirstScreen) {
                if (it.not()) {
                    Toast.makeText(
                        baseContext,
                        Constants.INVALID_NAME_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            isValidEmail.observe(this@SignUpFirstScreen) {
                if (it.not()) {
                    Toast.makeText(
                        baseContext,
                        Constants.INVALID_EMAIL_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            isValidPhoneNumber.observe(this@SignUpFirstScreen) {
                if (it.not()) {
                    Toast.makeText(
                        baseContext,
                        Constants.INVALID_PHONE_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            isValidPassword.observe(this@SignUpFirstScreen) {
                if (it.not()) {
                    Toast.makeText(
                        baseContext,
                        Constants.INVALID_PASSWORD_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            userLiveData.observe(this@SignUpFirstScreen) {
                val intent = Intent(this@SignUpFirstScreen, SignUpSecondScreen::class.java)
                intent.putExtra(Constants.user, it)
                intent.putExtra(Constants.password, userPassword.value.toString())
                startActivity(intent)
            }
        }

    }

    private fun setUpGenderDialog() {
        val genderItems = getListOfGenders()
        binding.genderEditText.setUpDialogData(
            genderItems,
            getString(R.string.please_select_your_gender),
            null, null, null
        )
    }
}