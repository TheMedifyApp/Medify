package com.geekymusketeers.medify.ui.mainFragments.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.User
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

    init {
        totalRating.value = 5.0f
    }

    fun getDataFromSharedPreference(sharedPreference: SharedPreferences) =
        viewModelScope.launch(Dispatchers.IO) {

            val userID = sharedPreference.getString("uid", "Not found").toString()
            val userName =
                sharedPreference.getString("name", "Not found").toString()
            val userEmail =
                sharedPreference.getString("email", "Not found").toString()
            val userPhone =
                sharedPreference.getString("phone", "Not found").toString()
            val userPosition =
                sharedPreference.getString("isDoctor", "Not fount").toString()
            val userPrescription =
                sharedPreference.getString("prescription", "false").toString()



            user.postValue(
                User(
                    UID = userID,
                    Name = userName,
                    Email = userEmail,
                    Phone = userPhone,
                    Specialist = userPosition,
                    Prescription = userPrescription,
                    isDoctor = userPosition
                )
            )
        }


    fun getTotalRating() = viewModelScope.launch(Dispatchers.IO) {
        FirebaseDatabase.getInstance().reference.child("Users").child(user.value?.UID!!)
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


    fun searchDoctor(doctorName: String) {
        _searchedDoctor.value = doctorName
    }
}