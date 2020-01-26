package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class District (

    @SerializedName("name")
    @Expose
    val districtName: String
)