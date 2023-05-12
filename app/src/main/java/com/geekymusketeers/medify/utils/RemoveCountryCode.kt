package com.geekymusketeers.medify.utils

object RemoveCountryCode {
    fun remove(number: String) : String {
        val numLength = number.length
        return if (numLength > 10) {
            val startIndex: Int = numLength - 10
            val newNumber: String = number.substring(startIndex, numLength)
            newNumber
        } else if (numLength < 10) {
            ""
        } else {
            number
        }
    }
}