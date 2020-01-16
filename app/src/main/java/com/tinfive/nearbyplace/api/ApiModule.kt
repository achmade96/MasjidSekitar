package com.tinfive.nearbyplace.api

import com.tinfive.nearbyplace.networks.EndPoint
import com.tinfive.nearbyplace.networks.MasjidApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiModule {

    fun getClient(): MasjidApi {
        return Retrofit.Builder()
            .baseUrl(EndPoint.BASE_URL_MASJID)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MasjidApi::class.java)
    }
}