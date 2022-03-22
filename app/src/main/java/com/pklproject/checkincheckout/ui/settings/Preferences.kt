package com.pklproject.checkincheckout.ui.settings

import android.content.Context
import android.preference.PreferenceManager

class Preferences (context: Context) {

    private companion object {
        const val LOGINKEY = "LOGINKEYVALUE"
        const val THEMEKEY = "THEMEKEYVALUE"
    }

    private val loggedIn = PreferenceManager.getDefaultSharedPreferences(context)
    var isLoggedIn = loggedIn.getBoolean(LOGINKEY, false)
        set(value) = loggedIn.edit().putBoolean(LOGINKEY, value).apply()

    private val changedTheme = PreferenceManager.getDefaultSharedPreferences(context)
    var changeTheme = changedTheme.getInt(THEMEKEY, 0)
        set(value) = changedTheme.edit().putInt(THEMEKEY, value).apply()
}