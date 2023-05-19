package com.geekymusketeers.medify.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.geekymusketeers.medify.ui.mainFragments.appointment.AppointmentBookingViewModel
import com.geekymusketeers.medify.ui.mainFragments.appointment.DoctorDetailsViewModel
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
                isAssignableFrom(AppointmentBookingViewModel::class.java) -> {
                    AppointmentBookingViewModel(application)
                }
                isAssignableFrom(DoctorDetailsViewModel::class.java) -> {
                    DoctorDetailsViewModel(application)
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