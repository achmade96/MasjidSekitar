package com.tinfive.nearbyplace.networks

import com.example.nearbyplaces.model.MyPlaces
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.EndPoint.Fasilitas
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import retrofit2.http.*

interface MasjidApi {

    /*@GET
    fun getMarkerPlace(@Url url: String): Observable<MyPlaces>*/
    @GET
    fun getMarkerPlace(@Url url: String): Observable<ApiRespons.MosqueRespon>

    @GET(Masjid)
    fun getMosque(): Observable<ApiRespons.MosqueRespon>

    @GET(Fasilitas)
    fun getFilteredMasjid() : Observable<List<Fasilitas>>
}