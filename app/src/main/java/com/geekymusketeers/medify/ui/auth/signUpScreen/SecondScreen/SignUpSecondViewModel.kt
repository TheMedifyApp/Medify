package com.geekymusketeers.medify.ui.auth.signUpScreen.SecondScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.ui.auth.signUpScreen.SignUpRepository
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpSecondViewModel(application: Application) : BaseViewModel(application) {

    private var userPassword = MutableLiveData<String>()
    private var userLiveData = MutableLiveData<User>()
    private var userAddress = MutableLiveData<String>()
    var userIsDoctor = MutableLiveData<String>()
    private var userSpecialization = MutableLiveData<String>()
    var userAccountCreationLiveData = MutableLiveData<Boolean>()
    var userDataBaseUpdate = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    private val signInRepository: SignUpRepository = SignUpRepository()

    val enableCreateAccountButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    init {
        userIsDoctor.value = Doctor.IS_NOT_DOCTOR.toItemString()
    }

    fun setUserPassword(password: String) {
        userPassword.value = password
    }

    fun setUpUser(user: User) {
        userLiveData.value = user
    }

    fun createAccount() = viewModelScope.launch(Dispatchers.IO) {
        val email = userLiveData.value!!.Email!!
        val password = userPassword.value!!

        val address = userAddress.value
        val isDoctor = userIsDoctor.value
        val specialization = if (isDoctor == Doctor.IS_DOCTOR.toItemString()) userSpecialization.value else null

        userLiveData.value!!.apply {
            Address = address
            if (isDoctor != null) {
                this.isDoctor = isDoctor
            }
            Specialist = specialization
        }

        val userID = signInRepository.registerUser(email, password)?.uid

        Logger.debugLog("User ID after account creation is $userID")

        if (userID == null) {
            errorLiveData.postValue("Please check your details (User is null)")
            return@launch
        }
        userLiveData.value!!.UID = userID
        FirebaseDatabase.getInstance().reference.child(Constants.Users).child(userID)
            .setValue(userLiveData.value).addOnSuccessListener {
                FirebaseAuth.getInstance().signOut()
                Logger.debugLog("User database created successfully and userID is $userID")
                userAccountCreationLiveData.value = true
            }.addOnFailureListener {
                Logger.debugLog("Exception caught at creating user database: ${it.message.toString()}")
                errorLiveData.postValue(it.message.toString())
            }
    }

    fun createUserDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid.toString()
        FirebaseDatabase.getInstance().reference.child(Constants.Users).child(userId)
            .setValue(userLiveData.value).addOnSuccessListener {
                Logger.debugLog("User database created successfully and userID is $userId")
                userDataBaseUpdate.value = true
            }.addOnFailureListener {
                Logger.debugLog("Exception caught at creating user database: ${it.message.toString()}")
                userDataBaseUpdate.value = false
            }
        if (userLiveData.value!!.isDoctor == Doctor.IS_DOCTOR.toItemString()) {
            FirebaseDatabase.getInstance().reference.child(Constants.Doctor).child(userId)
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

    fun setUserIsDoctor(isDoctor: String) {
        userIsDoctor.value = isDoctor
        updateButtonState()
    }

    fun setUserSpecialization(specialization: String) {
        userSpecialization.value = specialization
        updateButtonState()
    }

    private fun updateButtonState() {
        val requiredField =
            userAddress.value.isNullOrEmpty() || if (userIsDoctor.value == Doctor.IS_DOCTOR.toItemString()) {
                userSpecialization.value.isNullOrEmpty()
            } else {
                false
            }
        enableCreateAccountButtonLiveData.value = requiredField.not()
    }

}