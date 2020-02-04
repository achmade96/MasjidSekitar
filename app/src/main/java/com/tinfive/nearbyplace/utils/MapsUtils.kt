package com.tinfive.nearbyplace.utils

import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.util.Log
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import kotlin.math.ln
private lateinit var mLastLocation: Location
@Suppress("DEPRECATION")
class MapsUtils {
    companion object{
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getUrl(latitude: Double, longitude: Double): String {
            val googlePlaceUrl = StringBuilder("rest/public/mosques")
                /*.append("?location=$latitude,$longitude")
                .append("&radius=1000")
                .append("&type=mosque")
                .append("&key=AIzaSyBHRZoFAHUe_dkkhrclrS5FyH3jAsFgCAk")*/

            return googlePlaceUrl.toString()
        }

        fun getZoomLevel(circle : Circle) : Int {
            val radius : Double = circle.radius
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
        dist *= 60 * 1.1515
        return dist
    }

    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1: Double = StartP.latitude
        val lat2: Double = EndP.latitude
        val lon1: Double = StartP.longitude
        val lon2: Double = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        return Radius * c
    }

}