package com.tinfive.nearbyplace.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.maps.model.Circle
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

    /**
     * convert kilometer
     */

    fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}