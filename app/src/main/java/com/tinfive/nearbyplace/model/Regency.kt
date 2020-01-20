package com.tinfive.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class Regency (

    @SerializedName("id") val id : Int,
    @SerializedName("province_id") val province_id : Int,
    @SerializedName("name") val name : String
)