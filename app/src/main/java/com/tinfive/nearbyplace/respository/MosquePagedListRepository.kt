/*
package com.tinfive.nearbyplace.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.MasjidApi
import androidx.paging.PagedList
import com.tinfive.nearbyplace.common.Constans
import com.tinfive.nearbyplace.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MosquePagedListRepository (private val apiService : MasjidApi) {
    lateinit var moviePagedList: LiveData<PagedList<Masjid>>
    lateinit var moviesDataSourceFactory: MosqueDataSourceFactory

    fun fetchLiveMosquePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Masjid>> {
        moviesDataSourceFactory = MosqueDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constans.POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MosqueDataSource, NetworkState>(
            moviesDataSourceFactory.mosqueLiveDataSource, MosqueDataSource::networkState
        )
    }
}
*/
