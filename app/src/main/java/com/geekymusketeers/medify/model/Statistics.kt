package com.geekymusketeers.medify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.TreeMap

@Parcelize
data class HealthData(
    var name: String? = null,
    var tests: TreeMap<String, TestResult>? = null
) : Parcelable

@Parcelize
data class TestResult(
    var result: String? = null,
    var dateTime: String? = null
) : Parcelable