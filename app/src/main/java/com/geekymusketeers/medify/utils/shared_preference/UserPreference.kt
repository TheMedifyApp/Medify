package com.geekymusketeers.medify.utils.shared_preference

import android.content.Context
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants

/**
 * encrypted shared preference class for user-session related data
 */
class UserPreference(context: Context) : EncryptedSharedPreference(context, Constants.USER_PREFERENCE_KEY) {
    // The inline modifier can be used on accessors of properties that don't have backing fields
    inline var mobileNumber: String
        get() = getString(SharedPreferenceKeys.UserPreferenceKeys.MOBILE_NUMBER, "") ?: ""
        set(value) = putString(SharedPreferenceKeys.UserPreferenceKeys.MOBILE_NUMBER, value)

    inline var userProfile: User?
        get() = getObject(SharedPreferenceKeys.UserPreferenceKeys.USER_PROFILE, User::class.java)
        set(value) = putObject(SharedPreferenceKeys.UserPreferenceKeys.USER_PROFILE, value)

    inline var historyToolTipCounter: Int
        get() = getInt(SharedPreferenceKeys.UserPreferenceKeys.HISTORY_TOOLTIP_COUNTER, 0)
        set(value) = putInt(SharedPreferenceKeys.UserPreferenceKeys.HISTORY_TOOLTIP_COUNTER, value)

    inline var isUserAdmin: Boolean
        get() = getBoolean(SharedPreferenceKeys.UserPreferenceKeys.ADMIN_USER, false)
        set(value) = putBoolean(SharedPreferenceKeys.UserPreferenceKeys.ADMIN_USER, value)

    inline val isUserLoggedIn: Boolean
        get() = false
//        get() = (userProfile != null)
}