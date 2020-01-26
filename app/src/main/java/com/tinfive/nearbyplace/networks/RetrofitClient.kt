package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MAPS
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MASJID
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var ourInstance: Retrofit?=null

    //get maps marker
    val instance: MasjidApi
        get() {
            if (ourInstance ==null){
                ourInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL_MAPS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return ourInstance!!.create(MasjidApi::class.java)
        }

    //get masjid list
    fun getMosqueList() =  Retrofit.Builder()
        .baseUrl(BASE_URL_MASJID)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(ApiWorker.client)
        .build()
        .create(MasjidApi::class.java)

    //get fasilitas list
    fun getFacilitiesList() =  Retrofit.Builder()
        .baseUrl(BASE_URL_MASJID)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(ApiWorker.client)
        .build()
        .create(MasjidApi::class.java)
}