package com.geekymusketeers.medify.utils

import android.content.SharedPreferences
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
}