package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MAPS
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MASJID
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MasjidService {

    private val api: MasjidApi = Retrofit.Builder()
        .baseUrl(BASE_URL_MASJID)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MasjidApi::class.java)

    fun getMasjid(): Observable<List<DataMasjid>> {
        return api.getMosque()
    }

}