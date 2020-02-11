package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Masjid(

    @SerializedName("id")
    @Expose
    val mosqueId: Int,
    @SerializedName("type")
    @Expose
    val mosqueType: String,
    @SerializedName("code")
    @Expose
    val mosqueCode: String,
    @SerializedName("name")
    @Expose
    val mosqueName: String,
    @SerializedName("address")
    @Expose
    val mosqueAddress: String,
    @SerializedName("latitude")
    @Expose
    val mosqueLat: String,
    @SerializedName("longitude")
    @Expose
    val mosqueLng: String,
    @SerializedName("pic")
    @Expose
    val mosquePict: String

)