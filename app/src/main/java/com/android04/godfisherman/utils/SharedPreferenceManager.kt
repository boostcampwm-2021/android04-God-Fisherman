package com.android04.godfisherman.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(
    @ApplicationContext context: Context
){
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_APP_FILE, Context.MODE_PRIVATE)


    fun saveString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value).apply()
    }

    fun getString(key: String) = sharedPref.getString(key, "")

    companion object {
        const val PREF_APP_FILE = "pref_app_file"
        const val PREF_LOCATION = "pref_location"
    }
}
