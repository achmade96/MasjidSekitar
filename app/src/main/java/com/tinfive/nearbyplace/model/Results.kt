package com.example.nearbyplaces.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tinfive.nearbyplace.model.Bank
import com.tinfive.nearbyplace.model.District
import com.tinfive.nearbyplace.model.Province
import com.tinfive.nearbyplace.model.Regency
import java.io.Serializable

class Results : Serializable {

    @SerializedName("name")
    var name: String? = ""


    var geometry: Geometry? = null


}
