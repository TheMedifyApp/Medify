package com.geekymusketeers.medify.utils.shared_preference

import android.content.SharedPreferences
import com.google.gson.Gson

interface IPreferences {
    fun clearLocalCacheData(key: String? = null)

    fun getString(key: String, defaultValue: String?): String?

    fun putString(key: String, value: String?)

    fun getInt(key: String, defaultValue: Int): Int

    fun putInt(key: String, value: Int?)

    fun getLong(key: String, defaultValue: Long): Long

    fun putLong(key: String, value: Long?)

    fun getFloat(key: String, defaultValue: Float): Float

    fun putFloat(key: String, value: Float?)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun putBoolean(key: String, value: Boolean?)

    fun remove(key: String) {
        clearLocalCacheData(key)
        getPreference().edit().remove(key).apply()
    }

    fun containsKey(key: String): Boolean {
        return getPreference().contains(key)
    }

    fun clearAll(): Boolean {
        clearLocalCacheData()
        return getPreference().edit().clear().commit()
    }

    fun putObject(key: String, value: Any?) {
        if (value == null)
            remove(key)
        else
            putString(key, getGsonParser().toJson(value))
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val data = getString(key, null)
        data?.let {
            return getGsonParser().fromJson(it, clazz)
        } ?: run {
            return null
        }
    }

    fun getPreference(): SharedPreferences

    fun getGsonParser(): Gson
}