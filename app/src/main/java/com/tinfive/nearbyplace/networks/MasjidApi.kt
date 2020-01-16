package com.tinfive.nearbyplace.networks

import com.example.nearbyplaces.model.MyPlaces
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface MasjidApi {


    @GET
    fun getNearbyPlaces(@Url url: String): Observable<MyPlaces>

    @GET(Masjid)
    fun getMosque(): Observable<List<DataMasjid>>
}