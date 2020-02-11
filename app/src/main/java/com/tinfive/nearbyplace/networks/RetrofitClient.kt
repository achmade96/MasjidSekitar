package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MASJID
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var ourInstance: Retrofit?=null
    var categories: List<Fasilitas>?=ArrayList()



    //get fasilitas list
    fun getFasilitasList() =  Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(ApiWorker.client)
        .build()
        .create(MasjidApi::class.java)

    //get masjid list
    fun getMosqueList() =  Retrofit.Builder()
        .baseUrl(BASE_URL_MASJID)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(ApiWorker.client)
        .build()
        .create(MasjidApi::class.java)

}