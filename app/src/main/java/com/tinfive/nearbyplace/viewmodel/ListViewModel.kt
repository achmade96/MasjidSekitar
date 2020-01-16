package com.tinfive.nearbyplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.MasjidService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {


    private val masjidSrv = MasjidService()
    private val googleSrv = MasjidService()

    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<DataMasjid>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchMasjid()
    }


    private fun fetchMasjid() {
        loading.value = true
        disposable.add(
            masjidSrv.getMasjid()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<DataMasjid>>() {
                    override fun onComplete() {
                        loading.value = false
                        //todo : Load Map Place
                    }

                    override fun onNext(t: List<DataMasjid>) {
                        println("DATA ${t.size}")
                        masjid.value = t
                        masjidLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        masjidLoadError.value = true
                        loading.value = false
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}