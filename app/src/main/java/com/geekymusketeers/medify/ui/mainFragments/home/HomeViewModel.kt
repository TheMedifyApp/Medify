package com.geekymusketeers.medify.ui.mainFragments.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.medify.base.BaseViewModel


class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val _searchedDoctor: MutableLiveData<String> by lazy { MutableLiveData() }
    val searchedDoctor: LiveData<String> = _searchedDoctor

    fun searchDoctor(doctorName: String) {
        _searchedDoctor.value = doctorName
    }
}