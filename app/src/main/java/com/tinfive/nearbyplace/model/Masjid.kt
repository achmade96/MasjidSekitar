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

    @SerializedName("identity")
    @Expose
    val mosqueIdentity: String,

    @SerializedName("surface_area")
    @Expose
    val surfaceArea: String,
    @SerializedName("building_area")
    @Expose
    val buildingArea: String,
    @Expose
    val kelId: String,
    @SerializedName("los")
    @Expose
    val los: String,
    @SerializedName("since")
    @Expose
    val since: String,
    @SerializedName("rek")
    @Expose
    val rek: String,
    @SerializedName("address")
    @Expose
    val address: String,
    @SerializedName("latitude")
    @Expose
    val latitude: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String,
    @SerializedName("estimate")
    @Expose
    val estimate: String,
    @SerializedName("estimate_date")
    @Expose
    val estimateDate: String,
    @SerializedName("pic")
    @Expose
    val pic: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("bank")
    @Expose
    val bank : Bank ,
    @SerializedName("province")
    @Expose
    val province: Province,
    @SerializedName("regency")
    @Expose
    val regency: Regency,
    @SerializedName("district")
    @Expose
    val district: District,
    @SerializedName("village")
    @Expose
    val village: String
)