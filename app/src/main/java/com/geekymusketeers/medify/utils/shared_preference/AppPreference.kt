package com.geekymusketeers.medify.utils.shared_preference

import android.content.Context
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.shared_preference.SharedPreferenceKeys
import java.util.Locale

/**
 * Shared preference class for app related data
 */
class AppPreference(context: Context) : SharedPreference(context, Constants.KEY_APP_PREFERENCE) {
    companion object {
        //Keys which needs to be stored even after user logs out
        private val reservedKeys = HashSet<String>()
    }

    init {
        reservedKeys.add(SharedPreferenceKeys.AppPreferenceKeys.APP_LANGUAGE)
    }

    inline var appLanguage: String
        get() = getString(SharedPreferenceKeys.AppPreferenceKeys.APP_LANGUAGE, Constants.English)!!
        set(value) = putString(SharedPreferenceKeys.AppPreferenceKeys.APP_LANGUAGE, value)

    @Suppress("ApplySharedPref")
    fun logOut(hardLogout: Boolean = false) {
        val editor = getPreference().edit()
        if (hardLogout) {
            editor.clear()
        } else {
            for (key in getPreference().all.keys) {
                if (!reservedKeys.contains(key))
                    editor.remove(key)
            }
        }
        editor.commit()
    }
}