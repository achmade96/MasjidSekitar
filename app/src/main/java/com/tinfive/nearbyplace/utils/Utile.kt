package com.tinfive.nearbyplace.utils

import android.content.Context
import android.net.ConnectivityManager
import java.lang.StringBuilder

@Suppress("DEPRECATION")
class Utile {
    companion object{
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getUrl(latitude: Double, longitude: Double): String {
            val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
                .append("?location=$latitude,$longitude")
                .append("&radius=2000")
                .append("&type=mosque")
                .append("&key=AIzaSyBHRZoFAHUe_dkkhrclrS5FyH3jAsFgCAk")

            return googlePlaceUrl.toString()
        }

    }
}