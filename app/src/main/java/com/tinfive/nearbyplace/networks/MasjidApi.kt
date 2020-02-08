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

    @POST("rest/public/mosque_facilities")
    @FormUrlEncoded
    fun filterSubmit(@Field (value = "full_time") full_time: String,
                     @Field (value = "ac") ac: String,
                     @Field (value = "car_parking") car_parking: String,
                     @Field (value = "free_water") free_water: String,
                     @Field (value = "easy_access") easy_access: String) : Observable<ApiRespons.FilterRespons>


}