package com.example.nearbyplaces.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.Regency

class MyPlaces {

    var html_attributions: Array<String>?=null
    var status: String?=null
    var results: Array<Results>?=null

    //get marker api from yukamal
    var listMasjid: Array<DataMasjid>?=null

}