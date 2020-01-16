package com.example.nearbyplaces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Results : Serializable {

    @SerializedName("name")
    var name: String? = ""


    var geometry: Geometry? = null


}
