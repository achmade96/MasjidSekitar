package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Bank (
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val bankCode: String,
    @SerializedName("name")
    val bankName: String
)