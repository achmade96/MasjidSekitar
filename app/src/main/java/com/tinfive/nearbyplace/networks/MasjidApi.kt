package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.EndPoint.Fasilitas
import com.tinfive.nearbyplace.networks.EndPoint.Masjid
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface MasjidApi {

    @GET(Masjid)
    fun getMosque(): Observable<ApiRespons.MasjidResponDummy>

    @GET(Fasilitas)
    fun getFilteredMasjid() : Observable<List<Fasilitas>>

    @GET("rest/public/mosques/{id}")
    fun getDetailMosque(@Path (value = "id") id: String): Single<ApiRespons.MasjidResponDummy>

    @GET("rest/public/mosque_facilities")
    @FormUrlEncoded
    fun filterSubmit(@Query (value = "full_time") full_time: String,
                     @Query (value = "ac") ac: String,
                     @Query (value = "free_water") free_water: String,
                     @Query (value = "easy_access") easy_access: String) : Observable<ApiRespons.FilterRespons>


}