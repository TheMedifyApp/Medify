package com.geekymusketeers.medify.model

data class SettingsItem(
    val itemID: Int,
    val drawableInt: Int,
    val itemName: String
)

enum class SettingsState(int: Int) {
    TO_EDIT_PROFILE(0),
    TO_UPLOAD_PRESCRIPTION(1),
    TO_UPI_QR(2),
    TO_ABOUT_US(3),
    TO_FEEDBACK(4),
    TO_NEED_HELP(5),
    TO_LOGOUT(6);

    companion object {
        fun getSettingsState(state: SettingsState) : Int {
            return when (state) {
                TO_EDIT_PROFILE -> 0
                TO_UPLOAD_PRESCRIPTION -> 1
                TO_UPI_QR -> 2
                TO_ABOUT_US -> 3
                TO_FEEDBACK-> 4
                TO_NEED_HELP-> 5
                TO_LOGOUT-> 6
            }
        }
    }
}