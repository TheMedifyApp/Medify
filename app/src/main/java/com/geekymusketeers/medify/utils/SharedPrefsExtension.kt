package com.geekymusketeers.medify.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.geekymusketeers.medify.model.User
import com.google.gson.Gson


object SharedPrefsExtension {
    fun SharedPreferences.getUserFromSharedPrefs() : User {
        val userFromSharedPreferencesAsGson =
            this.getString(Constants.SAVED_USER, null)
        val gson = Gson()
        return gson.fromJson(userFromSharedPreferencesAsGson, User::class.java) as User
    }

    fun SharedPreferences.saveUserToSharedPrefs(user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        this.edit().putString(Constants.SAVED_USER, json).apply()
    }

    inline fun <reified T> SharedPreferences.put(key: String, value: T) {
        edit() {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                else -> {
                    throw UnsupportedOperationException("SharedPreferences put() not support ${T::class.qualifiedName.toString()} type of data.")
                }
            }
        }
    }
}