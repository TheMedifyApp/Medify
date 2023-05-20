package com.geekymusketeers.medify.ui.mainFragments.stats

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.geekymusketeers.medify.base.BaseViewModel
import com.geekymusketeers.medify.model.HealthData
import com.geekymusketeers.medify.model.TestResult
import com.geekymusketeers.medify.utils.DateTimeExtension
import com.geekymusketeers.medify.utils.Logger
import java.util.TreeMap

class AddStatsDataViewModel(application: Application) : BaseViewModel(application) {

    var healthData = MutableLiveData<HealthData>()
    var testName = MutableLiveData<String>()
    var healthId = MutableLiveData<String>()
    var statsTreeMap = MutableLiveData<TreeMap<String, TestResult>>()
    var enableButton = MutableLiveData<Boolean>()

    init {
        healthData.value = HealthData()
        statsTreeMap.value = TreeMap()
    }

    fun setHealthData(healthData: HealthData) {
        healthData.name?.let {
            setTestName(it)
        }
//        this.healthData.value = healthData
    }

    fun setHealthId() {
        this.healthId.value = DateTimeExtension.getTimeStamp()
        updateButtonState()
    }

    fun setTestResult(testResult: String) {
        val timeStamp = DateTimeExtension.getTimeStamp()
        val result = TestResult(testResult, timeStamp)
        statsTreeMap.value?.let {
            it[timeStamp] = result
            if (it.size > 28) {
                it.pollFirstEntry()
            }
        }
        updateButtonState()
    }

    fun setTestName(testName: String) {
        this.testName.value = testName
        updateButtonState()
    }

    fun saveDataInFirebase() {
        val healthData = HealthData(
            testName.value,
            statsTreeMap.value,
            healthId.value
        )
        this.healthData.value = healthData
        Logger.debugLog("HealthData")
    }


    private fun updateButtonState() {
        val requiredField =
            testName.value.isNullOrEmpty() || healthId.value.isNullOrEmpty() || statsTreeMap.value.isNullOrEmpty()
        enableButton.value = requiredField.not()
    }



}