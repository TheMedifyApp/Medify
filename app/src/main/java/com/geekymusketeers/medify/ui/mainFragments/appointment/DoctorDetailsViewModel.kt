package com.geekymusketeers.medify.ui.mainFragments.appointment

import android.app.Application
import androidx.lifecycle.ViewModel
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.User

class DoctorDetailsViewModel(application: Application) : BaseViewModel(application) {

    private var doctor: User = User()

    fun initDoctor(doctorDetails: User) {
        doctor = doctorDetails
    }

    fun getDoctor(): User {
        return doctor
    }

}
