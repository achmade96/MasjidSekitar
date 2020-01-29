package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Fasilitas(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("full_time")
    @Expose
    var fullTime: String,
    @SerializedName("ac")
    @Expose
    var ac: String,
    @SerializedName("car_parking")
    @Expose
    var carParking: String,
    @SerializedName("free_water")
    @Expose
    var freeWater: String,
    @SerializedName("easy_access")
    @Expose
    var easyAccess: String,
    @SerializedName("mosque")
    @Expose
    var mosque: MosqueFacilities

    /*    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
    @SerializedName("mosque")
    @Expose
    var mosque: MosqueFacilities*/

)