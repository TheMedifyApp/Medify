package com.geekymusketeers.medify.utils.shared_preference

import android.content.Context
import android.content.SharedPreferences
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.SharedPrefsExtension.put
import com.google.gson.Gson

open class SharedPreference(context: Context, preferenceName: String) : IPreferences {
    private val gson = Gson()

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        preferenceName, Context.MODE_PRIVATE
    )

    override fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String?) = putValue(key, value)

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int?) = putValue(key, value)

    override fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long?) = putValue(key, value)

    override fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float?) = putValue(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean?) = putValue(key, value)

    /**
     * Generic function to put the value in SharedPreferences and the Cache.
     */
    private inline fun <reified T> putValue(key: String, value: T?) {
        if (value == null)
            remove(key)
        else {
            try {
                sharedPreferences.put(key, value)
            } catch (e: UnsupportedOperationException) {
                Logger.logException(TAG, e, Logger.LogLevel.ERROR, true)
            }
        }
    }

    override fun getPreference() = sharedPreferences

    override fun getGsonParser() = gson

    override fun clearLocalCacheData(key: String?) {
        //NA
    }

    companion object {
        private const val TAG = "SharedPreference"
    }
}