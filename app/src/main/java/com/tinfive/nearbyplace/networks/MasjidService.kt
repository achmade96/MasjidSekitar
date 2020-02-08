package com.tinfive.nearbyplace.networks

import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.EndPoint.BASE_URL_MASJID
import io.reactivex.Observable
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

    fun getMasjid(): Observable<ApiRespons.MasjidResponDummy> {
        return api.getMosque()
    }

}