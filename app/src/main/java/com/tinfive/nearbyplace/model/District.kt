package com.tinfive.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class District (

    @SerializedName("id") val id : Int,
    @SerializedName("regency_id") val regency_id : Int,
    @SerializedName("name") val name : String
)