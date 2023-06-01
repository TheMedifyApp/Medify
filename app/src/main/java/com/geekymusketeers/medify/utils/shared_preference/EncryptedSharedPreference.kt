package com.geekymusketeers.medify.utils.shared_preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.collection.LruCache
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
import com.geekymusketeers.medify.utils.SharedPrefsExtension.put
import com.google.gson.Gson

open class EncryptedSharedPreference(
    context: Context,
    preferencesName: String,
) : IPreferences {
    private val gson = Gson()
    private val cache: LruCache<String, Any>? by lazy {
        LruCache<String, Any>(Constants.PREF_DEFAULT_IN_MEMORY_CACHE_SIZE)
    }

    /*
     Stable version of security-crypto library supports for Android API >=23 only
     */
    private val sharedPrefs: SharedPreferences =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EncryptedSharedPreferences.create(
                preferencesName,
                getMasterKey(),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            context.getSharedPreferences(
                preferencesName,
                Context.MODE_PRIVATE
            )
        }

    private fun getMasterKey(): String {
        return MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        cache?.get(key)?.let { return it as String }
        val value: String? = sharedPrefs.getString(key, defaultValue)
        value?.let { cache?.put(key, it) }
        return value ?: defaultValue
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        cache?.get(key)?.let { return it as Int }
        val value: Int = sharedPrefs.getInt(key, defaultValue)
        cache?.put(key, value)
        return value
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        cache?.get(key)?.let { return it as Long }
        val value: Long = sharedPrefs.getLong(key, defaultValue)
        cache?.put(key, value)
        return value
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        cache?.get(key)?.let { return it as Float }
        val value: Float = sharedPrefs.getFloat(key, defaultValue)
        cache?.put(key, value)
        return value
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        cache?.get(key)?.let { return it as Boolean }
        val value: Boolean = sharedPrefs.getBoolean(key, defaultValue)
        cache?.put(key, value)
        return value
    }

    override fun putString(key: String, value: String?) = putValue(key, value)
    override fun putInt(key: String, value: Int?) = putValue(key, value)
    override fun putLong(key: String, value: Long?) = putValue(key, value)
    override fun putFloat(key: String, value: Float?) = putValue(key, value)
    override fun putBoolean(key: String, value: Boolean?) = putValue(key, value)

    override fun getPreference() = sharedPrefs
    override fun getGsonParser() = gson
    override fun clearLocalCacheData(key: String?) {
        if (key.isNullOrEmpty())
            cache?.evictAll()
        else
            cache?.remove(key)
    }

    /**
     * Generic function to put the value in SharedPreferences and the Cache.
     */
    private inline fun <reified T> putValue(key: String, value: T?) {
        if (value == null)
            remove(key)
        else {
            try {
                sharedPrefs.put(key, value)
            } catch (e: UnsupportedOperationException) {
                Logger.logException(TAG, e, Logger.LogLevel.ERROR, true)
            }
            cache?.put(key, value)
        }
    }

    companion object {
        private const val TAG = "EncryptedSharedPrefs"
    }
}