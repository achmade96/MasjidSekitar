package com.tinfive.nearbyplace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Bank (
    @SerializedName("name")
    @Expose
    val bankName: String
)