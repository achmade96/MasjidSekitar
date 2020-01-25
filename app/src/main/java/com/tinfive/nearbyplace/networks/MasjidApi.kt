package com.tinfive.nearbyplace.networks

import com.example.nearbyplaces.model.MyPlaces
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import retrofit2.http.*

interface MasjidApi {


    @GET
    fun getNearbyPlaces(@Url url: String): Observable<MyPlaces>

    @GET
    fun getMarkerPlace(@Url url: String): Observable<MyPlaces>

    @GET(Masjid)
    fun getMosque(): Observable<List<DataMasjid>>

    @POST("filter")
    @FormUrlEncoded
    fun getFilteredMasjid(@Field("data")data: String) : Observable<List<DataMasjid>>
}