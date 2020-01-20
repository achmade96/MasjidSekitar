package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Province (

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val provinceName: String
)