package com.geekymusketeers.medify.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.geekymusketeers.medify.ui.auth.forgotPassword.ForgotPasswordViewModel
import com.geekymusketeers.medify.ui.auth.signInScreen.SignInViewModel
import com.geekymusketeers.medify.ui.auth.signUpScreen.SecondScreen.SignUpSecondViewModel
import com.geekymusketeers.medify.ui.auth.signUpScreen.firstScreen.SignUpFirstViewModel
import com.geekymusketeers.medify.ui.mainFragments.home.HomeViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            // Get the Application object from extras
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            when {
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(application)
                }
                isAssignableFrom(SignUpFirstViewModel::class.java) -> {
                    SignUpFirstViewModel(application)
                }
                isAssignableFrom(SignUpSecondViewModel::class.java) -> {
                    SignUpSecondViewModel(application)
                }
                isAssignableFrom(SignInViewModel::class.java) -> {
                    SignInViewModel(application)
                }
                isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                    ForgotPasswordViewModel(application)
                }
//                isAssignableFrom(FirebaseViewModel::class.java) -> {
//                    FirebaseViewModel()
//                }
//                isAssignableFrom(CustomizeQRViewModel::class.java) -> {
//                    CustomizeQRViewModel(application)
//                }

                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
}