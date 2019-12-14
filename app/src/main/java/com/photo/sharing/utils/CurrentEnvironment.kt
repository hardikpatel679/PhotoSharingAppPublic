package com.photo.sharing.utils

import com.pixplicity.easyprefs.library.Prefs
import com.photo.sharing.enumconstants.EnvironmentKey

object CurrentEnvironment {

    fun get(key: EnvironmentKey): String? {
        return Prefs.getString(key.name, null)
    }

    /*Method to return empty string*/
    fun getNotNull(key: EnvironmentKey): String {
        return Prefs.getString(key.name, "")
    }

    fun set(key: EnvironmentKey, value: String?) {
        Prefs.putString(key.name, value)
    }

    fun getBoolean(key: EnvironmentKey): Boolean {
        return Prefs.getBoolean(key.name, false)
    }

    fun setBoolean(key: EnvironmentKey, value: Boolean) {
        Prefs.putBoolean(key.name, value)
    }

    fun setLong(key: EnvironmentKey, value: Long) {
        Prefs.putLong(key.name, value)
    }

    fun getLong(key: EnvironmentKey): Long {
        return Prefs.getLong(key.name, 0)
    }

    fun setInt(key: EnvironmentKey, value: Int) {
        Prefs.putInt(key.name, value)
    }

    fun getInt(key: EnvironmentKey): Int {
        return Prefs.getInt(key.name, 0)
    }
    fun clear() {
        Prefs.clear()
    }
}