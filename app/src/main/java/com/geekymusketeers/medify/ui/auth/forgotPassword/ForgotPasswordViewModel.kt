package com.geekymusketeers.medify.ui.auth.forgotPassword

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.medify.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {

    private var email = MutableLiveData<String>()
    var forgotPasswordLiveData = MutableLiveData<Boolean>()
    var enableSendMailButton = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun setEmail(email: String) {
        this.email.value = email
        updateEnableSendMailButton()
    }

    fun getEmail() = email.value.toString()

    fun sendResetLinkMail() {
        val email = email.value!!
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                forgotPasswordLiveData.value = true
            } else {
                errorLiveData.postValue(it.exception?.message)
            }
        }.addOnFailureListener {
            errorLiveData.postValue(it.message)
        }
    }


    private fun updateEnableSendMailButton() {
        enableSendMailButton.value = email.value?.isNotEmpty()
    }

}