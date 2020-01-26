package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Regency (

    @SerializedName("name")
    @Expose
    val regencyName: String
)