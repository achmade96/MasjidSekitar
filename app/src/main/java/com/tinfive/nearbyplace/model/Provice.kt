package com.tinfive.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class Province (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)