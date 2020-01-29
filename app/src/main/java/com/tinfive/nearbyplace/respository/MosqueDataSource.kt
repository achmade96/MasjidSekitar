/*
package com.tinfive.nearbyplace.respository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.tinfive.nearbyplace.common.Constans.FIRST_PAGE
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.MasjidApi
import com.tinfive.nearbyplace.networks.RetrofitClient
import com.tinfive.nearbyplace.utils.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MosqueDataSource(
    private val apiService: MasjidApi,
    private val disposable: CompositeDisposable
) : PageKeyedDataSource<Int, Masjid>() {
    val page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    val service = RetrofitClient.getMosqueList()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Masjid>
    ) {
        networkState.postValue(NetworkState.LOADING)
        disposable.add(
            service.getMosque(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(it.data.data, page - 1, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, { err ->
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Masjid>) {
        disposable.add(service.getMosque(params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (it.data.total >= params.key) {
                    callback.onResult(it.data.data, params.key + 1)
                }
            }, { err ->
                networkState.postValue(NetworkState.ERROR)
            }

            ))
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Masjid>) {

    }
}
*/
