package com.geekymusketeers.medify.ui.auth.signUpScreen.SecondScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.ui.auth.signUpScreen.SignUpRepository
import com.geekymusketeers.medify.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpSecondViewModel(application: Application) : BaseViewModel(application) {

    var userPassword = MutableLiveData<String>()
    var userLiveData = MutableLiveData<User>()
    var userAddress = MutableLiveData<String>()
    var userIsDoctor = MutableLiveData<Doctor>()
    var userSpecialization = MutableLiveData<String>()
    var userAccountCreationLiveData = MutableLiveData<Boolean>()
    var userDataBaseUpdate = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    val enableCreateAccountButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    fun setUserPassword(password: String) {
        userPassword.value = password
    }

    fun setUpUser(user: User) {
        userLiveData.value = user
    }

    fun createAccount() = viewModelScope.launch(Dispatchers.IO) {
        val email = userLiveData.value!!.Email!!
        val password = userPassword.value!!
        val userID = SignUpRepository().registerUser(email, password)?.uid

        if (userID.isNullOrEmpty()) {
            errorLiveData.postValue("Please check your details")
            return@launch
        } else {
            userLiveData.value!!.UID = userID
            userAccountCreationLiveData.postValue(true)
            FirebaseAuth.getInstance().signOut()
        }
    }

    fun createUserDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid.toString()
        FirebaseDatabase.getInstance().reference.child("Users").child(userId)
            .setValue(userLiveData.value).addOnSuccessListener {
                Logger.debugLog("User database created successfully")
                userDataBaseUpdate.value = true
            }.addOnFailureListener {
                Logger.debugLog("Exception caught at creating user database: ${it.message.toString()}")
                userDataBaseUpdate.value = false
            }
        if (userLiveData.value!!.isDoctor == Doctor.IS_DOCTOR) {
            FirebaseDatabase.getInstance().reference.child("Doctor").child(userId)
                .setValue(userLiveData.value).addOnSuccessListener {
                    Logger.debugLog("Doctor database created successfully")
                }.addOnFailureListener {
                    Logger.debugLog("Exception caught at creating doctor database: ${it.message.toString()}")
                }
        }
    }

    fun setUserAddress(address: String) {
        userAddress.value = address
        updateButtonState()
    }

    fun setUserIsDoctor(isDoctor: Doctor) {
        userIsDoctor.value = isDoctor
        updateButtonState()
    }

    fun setUserSpecialization(specialization: String) {
        userSpecialization.value = specialization
        updateButtonState()
    }

    private fun updateButtonState() {
        val requiredField =
            userAddress.value.isNullOrEmpty() || if (userIsDoctor.value == Doctor.IS_DOCTOR) {
                userSpecialization.value.isNullOrEmpty()
            } else {
                false
            }
        enableCreateAccountButtonLiveData.value = requiredField.not()
    }

}