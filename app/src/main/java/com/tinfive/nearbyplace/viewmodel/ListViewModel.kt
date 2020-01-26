package com.tinfive.nearbyplace.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.networks.MasjidService
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
//    private val masjidSrv = MasjidService()

    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<DataMasjid>>()
    val fasilitasData = MutableLiveData<List<Fasilitas>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchMasjid()
    }

    fun loadFasilitas(){
        fetchFacilities()
    }

    private fun fetchFacilities() {
        loading.value = true
        disposable.add(RetrofitClient.getFacilitiesList().getFilteredMasjid()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ facilitiesRespon ->
                val myHandler = Handler()
                myHandler.postDelayed(object : Runnable{
                    override fun run() {
                        fasilitasData.value = facilitiesRespon
                    }
                }, 3000)
                println("DATA FASILITIES ${facilitiesRespon.size}")
            },{err ->
//                println("DATA ${err.message}")
            })
        )
    }

    private fun fetchMasjid() {
        loading.value = true
        disposable.add(RetrofitClient.getMosqueList().getMosque()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ mosqueRespons ->
//                println("Mosque ${mosqueRespons}")
                masjid.value = mosqueRespons
                masjidLoadError.value = false
                loading.value = false
            },{err ->
                masjidLoadError.value = true
                loading.value = false
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}