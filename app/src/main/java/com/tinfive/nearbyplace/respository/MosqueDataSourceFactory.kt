/*
package com.tinfive.nearbyplace.respository

import androidx.lifecycle.MutableLiveData
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.MasjidApi
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.DataSource

class MosqueDataSourceFactory (private val apiService : MasjidApi, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Masjid>() {

    val mosqueLiveDataSource = MutableLiveData<MosqueDataSource>()

    override fun create(): DataSource<Int, Masjid>{
        val mosqueDataSource = MosqueDataSource(apiService, compositeDisposable)
        mosqueLiveDataSource.postValue(mosqueDataSource)
        return mosqueDataSource
    }
}
*/
