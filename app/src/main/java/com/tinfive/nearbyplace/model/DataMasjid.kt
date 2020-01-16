package com.tinfive.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class DataMasjid (

    @SerializedName("id") val id : Int,
    @SerializedName("kabkota") val kabkota : String,
    @SerializedName("kecamatan") val kecamatan : String,
    @SerializedName("nama_masjid") val nama_masjid : String,
    @SerializedName("tipologi") val tipologi : String,
    @SerializedName("alamat") val alamat : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("long") val long : Double,
    @SerializedName("foto") val foto : String
)