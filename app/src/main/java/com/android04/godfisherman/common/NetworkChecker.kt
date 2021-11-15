package com.android04.godfisherman.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkChecker {

    fun isConnected (): Boolean {
        val context = App.applicationContext()
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = manager.activeNetwork ?: return false
        val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}