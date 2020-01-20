package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class District (

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("regency_id")
    @Expose
    val regencyId: String,
    @SerializedName("name")
    @Expose
    val name: String
)