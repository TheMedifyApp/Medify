package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.TreeMap

//@Parcelize
//data class HealthData(
//    var name: String? = null,
//    var tests: TreeMap<String, TestResult>? = null,
//    var healthId : String? = null
//) : Parcelable

@Parcelize
data class HealthData(
    var name: String? = null,
    var tests: List<TestResult>? = null,
    var healthId: String? = null
) : Parcelable {
//    fun getSortedTests(): List<TestResult> {
//        return tests?.sortedBy { it.dateTime } ?: emptyList()
//    }
}

@Parcelize
data class TestResult(
    var result: String? = null,
    var dateTime: String? = null
) : Parcelable