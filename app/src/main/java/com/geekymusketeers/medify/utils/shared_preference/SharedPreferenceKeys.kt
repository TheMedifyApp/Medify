package com.geekymusketeers.medify.utils.shared_preference


/**
 * class containing @see[AppPreference] and @see[UserPreference] keys
 */
interface SharedPreferenceKeys {

    /**
     * Preference keys holds user session related data
     */
    interface UserPreferenceKeys {
        companion object {
            const val MOBILE_NUMBER = "MOBILE_NUMBER"
            const val USER_PROFILE = "USER_PROFILE"
            const val HISTORY_TOOLTIP_COUNTER = "HISTORY_TOOLTIP_COUNTER"
            const val ADMIN_USER = "ADMIN_USER"
        }
    }

    /**
     * Preference keys holding app related data
     */
    interface AppPreferenceKeys {
        companion object {
            const val APP_LANGUAGE = "APP_LANGUAGE"
        }
    }
}