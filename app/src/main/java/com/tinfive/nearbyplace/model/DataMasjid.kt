package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataMasjid(

    @SerializedName("id")
    val mosqueId: Int,
    @SerializedName("type")
    val mosqueType: String,
    @SerializedName("code")
    val mosqueCode: String,
    @SerializedName("name")
    val mosqueName: String,
    @SerializedName("identity")
    val mosqueIdentity: String,
    @SerializedName("surface_area")
    val surfaceArea: String,
    @SerializedName("building_area")
    val buildingArea: String,
    @SerializedName("los")
    val los: String,
    @SerializedName("since")
    val since: String,
    @SerializedName("bank_id")
    val bankId: String,
    @SerializedName("rek")
    val rek: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("province_id")
    val provinceId: String,
    @SerializedName("estimate")
    val estimate: String,
    @SerializedName("estimate_date")
    val estimateDate: String,
    @SerializedName("city_id")
    val cityId: String,
    @SerializedName("kec_id")
    val kecId: String,
    @SerializedName("kel_id")
    val kelId: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("bank")
    val bank : Bank ,
    @SerializedName("province")
    val province: Province,
    @SerializedName("regency")
    val regency: Regency,
    @SerializedName("district")
    val district: District,
    @SerializedName("village")
    val village: String
)