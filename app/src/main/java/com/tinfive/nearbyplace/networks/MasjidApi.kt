package com.tinfive.nearbyplace.networks

import com.example.nearbyplaces.model.MyPlaces
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.EndPoint.Fasilitas
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import retrofit2.http.*

interface MasjidApi {

    @GET
    fun getMarkerPlace(@Url url: String): Observable<MyPlaces>

    @GET(Masjid)
    fun getMosque(): Observable<ApiRespons.MosqueRespon>

    @GET(Fasilitas)
    fun getFilteredMasjid() : Observable<List<Fasilitas>>

    @POST("rest/public/mosque_facilities")
    @FormUrlEncoded
    fun filterSubmit(@Query (value = "full_time") full_time: String,
                     @Query (value = "ac") ac: String,
                     @Query (value = "free_water") free_water: String,
                     @Query (value = "easy_access") easy_access: String) : Observable<ApiRespons.FilterRespons>


}