package com.geekymusketeers.medify.ui.auth.signInScreen

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.SharedPrefsExtension.saveUserToSharedPrefs
import com.geekymusketeers.medify.utils.Validator.Companion.isValidEmail
import com.geekymusketeers.medify.utils.Validator.Companion.isValidPassword
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignInViewModel(application: Application) : BaseViewModel(application) {
    private val email = MutableLiveData<String>()
    private val password = MutableLiveData<String>()
    var enableLoginButton = MutableLiveData<Boolean>()
    var userIDLiveData = MutableLiveData<String>()
    var userLiveData = MutableLiveData<User>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferenceLiveData = MutableLiveData<Boolean>()
    private val signInRepository: SignInRepository = SignInRepository()

    fun setEmail(email: String) {
        this.email.value = email
        updateButtonState()
    }

    fun setPassword(password: String) {
        this.password.value = password
        updateButtonState()
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {

        val emailInput = email.value
        val passwordInput = password.value

        if (emailInput.isNullOrEmpty() || emailInput.isValidEmail().not()) {
            errorLiveData.postValue("Please enter a valid email")
            return@launch
        }

        if (passwordInput.isNullOrEmpty() || passwordInput.isValidPassword().not()) {
            errorLiveData.postValue("Please enter a valid password")
            return@launch
        }

        val user = signInRepository.loginUser(emailInput, passwordInput)

        if (user == null) {
            errorLiveData.postValue("Please check your details")
            return@launch
        } else {
            userIDLiveData.postValue(user.uid)
        }
    }

    fun getUserFromFirebase() = viewModelScope.launch(Dispatchers.IO) {
        FirebaseDatabase.getInstance().reference.child(Constants.Users).child(userIDLiveData.value!!)
            .get().addOnSuccessListener {

                try {

                    val user = User(
                        UID = it.child("uid").value.toString().trim(),
                        Name = it.child("name").value.toString().trim(),
                        Age = it.child("age").value.toString().trim().toInt(),
                        Email = it.child("email").value.toString().trim(),
                        Phone = it.child("phone").value.toString().trim(),
                        isDoctor = it.child("doctor").value.toString().trim(),
                        Specialist = it.child("specialist").value.toString().trim(),
                        Gender = it.child("gender").value.toString().trim(),
                        Address = it.child("address").value.toString().trim(),
                        Stats = it.child("stats").value.toString().trim(),
                        Prescription = it.child("prescription").value.toString().trim(),
                    )

                    userLiveData.postValue(user)
                } catch (e: Exception) {
                    Logger.debugLog("Error: ${e.message} and age is ${it.child("age").value.toString().trim()}")
                    errorLiveData.postValue(e.message.toString())
                }

            }.addOnFailureListener {
                errorLiveData.postValue(it.message.toString())
            }
    }

    fun saveInSharedPreference(sharedPreferences: SharedPreferences) {
        val user = userLiveData.value
        sharedPreferences.saveUserToSharedPrefs(user!!)
        sharedPreferenceLiveData.postValue(true)
    }

    private fun updateButtonState() {
        val requiredField =
            email.value.isNullOrEmpty() || password.value.isNullOrEmpty()
        enableLoginButton.value = requiredField.not()
    }
}