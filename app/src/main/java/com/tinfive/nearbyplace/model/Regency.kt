package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Regency (

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("province_id")
    @Expose
    val provinceId: String,
    @SerializedName("name")
    @Expose
    val name: String
)