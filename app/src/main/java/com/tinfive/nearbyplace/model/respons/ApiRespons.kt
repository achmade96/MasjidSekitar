package com.tinfive.nearbyplace.model.respons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tinfive.nearbyplace.model.MasjidModel

class ApiRespons {


    data class MosqueRespon(

        @SerializedName("message")
        @Expose
        val message: String,
        @SerializedName("data")
        @Expose
        val data: MasjidModel,
        @SerializedName("code")
        @Expose
        val code: Int


    )
}