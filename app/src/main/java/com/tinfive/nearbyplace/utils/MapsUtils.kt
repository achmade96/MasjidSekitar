package com.tinfive.nearbyplace.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.maps.model.Circle
import java.lang.StringBuilder
import kotlin.math.ln

@Suppress("DEPRECATION")
class MapsUtils {
    companion object{
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getUrl(latitude: Double, longitude: Double): String {
            val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
                .append("?location=$latitude,$longitude")
                .append("&radius=1000")
                .append("&type=mosque")
                .append("&key=AIzaSyBHRZoFAHUe_dkkhrclrS5FyH3jAsFgCAk")

            return googlePlaceUrl.toString()
        }

        fun getZoomLevel(circle : Circle) : Int {
            var radius : Double = circle.radius
            val scale = radius / 500
            val zoomLevel = (16 - ln(scale) / ln(2.toDouble())).toInt()
            return zoomLevel
        }

    }

}