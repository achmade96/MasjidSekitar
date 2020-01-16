package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MAPS
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var ourInstance: Retrofit?=null

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

}