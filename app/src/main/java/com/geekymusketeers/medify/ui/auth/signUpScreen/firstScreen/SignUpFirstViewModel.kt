package com.geekymusketeers.medify.ui.auth.signUpScreen.firstScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Validator.Companion.isValidEmail
import com.geekymusketeers.medify.utils.Validator.Companion.isValidName
import com.geekymusketeers.medify.utils.Validator.Companion.isValidPassword
import com.geekymusketeers.medify.utils.Validator.Companion.isValidPhone


class SignUpFirstViewModel(application: Application) : BaseViewModel(application) {

    var userName = MutableLiveData<String>()
    var userEmail = MutableLiveData<String>()
    var userPhone = MutableLiveData<String>()
    var userPassword = MutableLiveData<String>()
    var userAge = MutableLiveData<Int>()
    var userGender = MutableLiveData<String>()
    var userLiveData = MutableLiveData<User>()
    val enableNextButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    //Validation
    val isValidName: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidEmail: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidPhoneNumber: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidPassword: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    init {
        userGender.value = "male"
        userAge.value = 0
    }

    fun setUserName(name: String) {
        userName.value = name
        updateButtonState()
    }

    fun setUserEmail(email: String) {
        userEmail.value = email
        updateButtonState()
    }

    fun setUserPhone(phone: String) {
        userPhone.value = phone
        updateButtonState()
    }

    fun setUserPassword(password: String) {
        userPassword.value = password
        updateButtonState()
    }

    fun setUserAge(age: Int) {
        userAge.value = age
        updateButtonState()
    }

    fun setUserGender(gender: String) {
        userGender.value = gender
        updateButtonState()
    }

    fun setUpUser() {
        val name: String = userName.value.toString().trim()
        val email: String = userEmail.value.toString().trim().lowercase()
        val phone: String = userPhone.value.toString().trim()
        val password: String = userPassword.value.toString().trim()
        val age: Int = userAge.value!!
        val gender: String = userGender.value.toString().trim()


        if (name.isValidName().not()) {
            isValidName.postValue(false)
            return
        }
        if (email.isValidEmail().not()) {
            isValidEmail.postValue(false)
            return
        }
        if (phone.isValidPhone().not()) {
            isValidPhoneNumber.postValue(false)
            return
        }
        if (password.isValidPassword().not()) {
            isValidPassword.postValue(false)
            return
        }
        val user = User(
            Name = name,
            Email = email,
            Phone = phone,
            Age = age,
            Gender = gender
        )
        userLiveData.postValue(user)
    }


    private fun updateButtonState() {
        val requiredField = userName.value.isNullOrEmpty() ||
                userEmail.value.isNullOrEmpty() || userPhone.value.isNullOrEmpty() ||
                userPassword.value.isNullOrEmpty() || userAge.value == 0 ||
                userGender.value.isNullOrEmpty()
        enableNextButtonLiveData.value = requiredField.not()
//        setProgressValue()
    }
}