package com.android04.godfisherman.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.android04.godfisherman.data.DTO.Gps
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
        val editor = sharedPref.edit()
        editor.remove(key).apply()
    }

    fun getString(key: String) = sharedPref.getString(key, "")

    fun saveGps(value: Gps) {
        editor.putParcelable(KEY_GPS, value)
    }

    fun getGps(): Gps? {
       return sharedPref.getParcelable(KEY_GPS, null)
    }

    companion object {
        const val KEY_GPS = "gps"
        const val PREF_APP_FILE = "pref_app_file"
        const val PREF_LOCATION = "pref_location"
    }
}

