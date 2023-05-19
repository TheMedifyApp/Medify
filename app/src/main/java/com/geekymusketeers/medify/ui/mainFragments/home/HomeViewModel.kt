package com.geekymusketeers.medify.ui.mainFragments.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.SharedPrefsExtension.getUserFromSharedPrefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val _searchedDoctor: MutableLiveData<String> by lazy { MutableLiveData() }
    val searchedDoctor: LiveData<String> = _searchedDoctor
    var totalRating: MutableLiveData<Float> = MutableLiveData()
    var user: MutableLiveData<User> = MutableLiveData()
    var doctorList = MutableLiveData<List<User>>()
    private var doctorListTemp: MutableList<User> = mutableListOf()

    init {
        totalRating.value = 5.0f
    }

    fun getDataFromSharedPreference(sharedPreference: SharedPreferences) =
        viewModelScope.launch(Dispatchers.IO) {
            val userObj = sharedPreference.getUserFromSharedPrefs()
            user.postValue(userObj)
        }


    fun getTotalRating() = viewModelScope.launch(Dispatchers.IO) {
        FirebaseDatabase.getInstance().reference.child(Constants.Users).child(user.value?.UID!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("totalRating").exists()) {
                        totalRating.value =
                            snapshot.child("totalRating").value.toString().toFloat()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getDoctorList() {
        FirebaseDatabase.getInstance().reference.child(Constants.Users)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it?.let {
                            if (it.child("doctor").value.toString()
                                    .trim() == Doctor.IS_DOCTOR.toItemString()
                            ) {
                                addAsDoctor(it)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Logger.debugLog("Error in getting doctors list: ${error.message}")
                }
            })
        doctorList.value = doctorListTemp
        doctorListTemp = mutableListOf()
    }

    private fun addAsDoctor(it: DataSnapshot) {

        try {
            val doctorItem = User(
                UID = it.child("uid").value.toString().trim(),
                Name = it.child("name").value.toString().trim(),
                Age = it.child("age").value.toString().toInt(),
                Email = it.child("email").value.toString().trim(),
                Phone = it.child("phone").value.toString().trim(),
                isDoctor = it.child("isDoctor").value.toString().trim(),
                Specialist = it.child("specialist").value.toString().trim(),
                Gender = it.child("gender").value.toString().trim(),
                Address = it.child("address").value.toString().trim(),
                Stats = it.child("stats").value.toString().trim(),
                Prescription = it.child("prescription").value.toString().trim(),
            )
            doctorListTemp.add(doctorItem)
            doctorList.value = doctorListTemp
        } catch (e: Exception) {
            Logger.debugLog("Error in adding doctor: ${e.message} and the snapshot is: $it")
        }
    }


    fun searchDoctor(doctorName: String) {
        _searchedDoctor.value = doctorName
    }
}