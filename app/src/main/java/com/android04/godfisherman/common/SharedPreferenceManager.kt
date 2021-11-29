package com.android04.godfisherman.common

import android.content.Context
import android.content.SharedPreferences
import com.android04.godfisherman.presentation.main.Gps
import com.android04.godfisherman.utils.getParcelable
import com.android04.godfisherman.utils.putParcelable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(
    @ApplicationContext context: Context
){

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_APP_FILE, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun deleteString(key: String) {
        editor.remove(key).apply()
    }

    fun getString(key: String) = sharedPref.getString(key, null)

    fun saveGps(value: Gps) {
        editor.putParcelable(KEY_GPS, value)
    }

    fun loadGps(): Gps? {
       return sharedPref.getParcelable(KEY_GPS, null)
    }

    fun saveTIme(value: Long) {
        editor.putLong(KEY_TIME, value).apply()
    }

    fun loadTime(): Long? {
        return sharedPref.getLong(KEY_TIME, ERROR_TIME)
    }

    fun deleteTime() {
        editor.remove(KEY_TIME).apply()
    }

    companion object {
        const val ERROR_TIME = -1L
        const val KEY_TIME = "time"
        const val KEY_GPS = "gps"
        const val PREF_APP_FILE = "pref_app_file"
        const val PREF_LOCATION = "pref_location"
    }
}

