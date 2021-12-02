package com.android04.godfisherman.utils

import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

fun SharedPreferences.Editor.putParcelable(key: String, parcelable: Parcelable) {
    val json = Gson().toJson(parcelable)
    putString(key, json)
    apply()
}

inline fun <reified T : Parcelable?> SharedPreferences.getParcelable(key: String, default: T): T {
    val json = getString(key, null)
    return try {
        if (json != null)
            Gson().fromJson(json, T::class.java)
        else default
    } catch (_: JsonSyntaxException) {
        default
    }
}
