package com.geekymusketeers.medify.ui.mainFragments.settings.profile

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.SharedPrefsExtension.getUserFromSharedPrefs
import com.geekymusketeers.medify.utils.SharedPrefsExtension.saveUserToSharedPrefs
import com.geekymusketeers.medify.utils.Validator.Companion.isValidName
import com.geekymusketeers.medify.utils.Validator.Companion.isValidPhone
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel(application: Application) : BaseViewModel(application) {

    var userLiveData = MutableLiveData<User>()
    var userName = MutableLiveData<String>()
    var userPhone = MutableLiveData<String>()
    var userAddress = MutableLiveData<String>()
    var userAge = MutableLiveData<Int>()
    val enableNextButtonLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    var isProfileUpdated: MutableLiveData<Boolean> = MutableLiveData()

    //Validation
    val isValidName: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidAddress: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidPhoneNumber: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isValidAge: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    init {
        userAge.value = 0
    }

    fun getDataFromSharedPreferences(sharedPreferences: SharedPreferences) {
        userLiveData.postValue(sharedPreferences.getUserFromSharedPrefs())
    }

    fun setUserName(name: String) {
        userName.value = name
        updateButtonState()
    }

    fun setUserAge(age: Int) {
        userAge.value = age
        updateButtonState()
    }

    fun setUserPhone(phone: String) {
        userPhone.value = phone
        updateButtonState()
    }

    fun setUserAddress(address: String) {
        userAddress.value = address
        updateButtonState()
    }

    fun updateUserProfile(sharedPreferences: SharedPreferences) {
        var name: String = userName.value.toString().trim()
        if (name.isEmpty()) name = userLiveData.value!!.Name.toString()

        var address: String = userAddress.value.toString().trim().lowercase()
        if (address.isEmpty()) address = userLiveData.value!!.Address.toString()

        var phone: String = userPhone.value.toString().trim()
        if (phone.isEmpty()) phone = userLiveData.value!!.Phone.toString()

        var age: Int = userAge.value!!
        if (age == 0) age = userLiveData.value!!.Age

        val isDoctor = userLiveData.value!!.isDoctor

        if (name.isValidName().not()) {
            isValidName.postValue(false)
            return
        }
        if (address.isEmpty()) {
            isValidAddress.postValue(false)
            return
        }
        if (phone.isValidPhone().not()) {
            isValidPhoneNumber.postValue(false)
            return
        }
        if (isDoctor == Doctor.IS_DOCTOR.toItemString() && age < 25 || age == 0) {
            isValidAge.postValue(false)
            return
        }

        pushIntoFireBase(
            name,
            address,
            phone,
            age,
            userLiveData.value!!.UID.toString(),
            sharedPreferences
        )
    }

    private fun pushIntoFireBase(
        name: String,
        address: String,
        phone: String,
        age: Int,
        userID: String,
        sharedPreferences: SharedPreferences
    ) = viewModelScope.launch(Dispatchers.IO) {

        val sharedPrefsUser = sharedPreferences.getUserFromSharedPrefs()
        sharedPrefsUser.apply {
            Age = age
            Name = name
            Phone = phone
            Address = address
        }

        val user = mapOf(
            "name" to name,
            "age" to age,
            "phone" to phone,
            "address" to address
        )
        FirebaseDatabase.getInstance().reference.child(Constants.Users).child(userID)
            .updateChildren(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    sharedPreferences.saveUserToSharedPrefs(sharedPrefsUser)
                    isProfileUpdated.postValue(true)
                } else {
                    isProfileUpdated.postValue(false)
                }
            }
    }

    private fun updateButtonState() {
        val requiredField = userName.value.isNullOrEmpty() &&
                userPhone.value.isNullOrEmpty() && userAge.value == 0 &&
                userAddress.value.isNullOrEmpty()
        enableNextButtonLiveData.value = requiredField.not()
    }
}