package com.geekymusketeers.medify.ui.mainFragments.appointment

import androidx.lifecycle.ViewModel
import com.geekymusketeers.medify.model.User

class DoctorDetailsViewModel : ViewModel() {

    private var doctor: User = User()

    fun initDoctor(doctorDetails: User) {
        doctor = doctorDetails
    }

    fun getDoctor(): User {
        return doctor
    }

}
