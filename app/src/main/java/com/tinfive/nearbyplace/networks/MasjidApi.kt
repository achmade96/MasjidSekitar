package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.Masjid
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

    @get:GET("categories")
    val fasilitasList:Observable<List<Fasilitas>>

    @POST("filter")
    @FormUrlEncoded
    fun getFilteredMasjid(@Field("data")data : String) : Observable<List<Masjid>>


}