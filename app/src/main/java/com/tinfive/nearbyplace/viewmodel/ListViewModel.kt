package com.tinfive.nearbyplace.viewmodel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.MasjidModel
import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<MasjidModel>>()
    val fasilitasData = MutableLiveData<List<Fasilitas>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchMasjid()
    }

    fun loadFasilitas() {
        fetchFacilities()
    }

    private fun fetchFacilities() {
        loading.value = true
        disposable.add(
            RetrofitClient.getFacilitiesList().getFilteredMasjid()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ facilitiesRespon ->
                    fasilitasData.value = facilitiesRespon
//                    println("DATA FASILITIES ${facilitiesRespon.size}")
                }, { err ->
                    //                println("DATA ${err.message}")
                })
        )
    }

    private fun fetchMasjid() {
        loading.value = true
        disposable.add(
            RetrofitClient.getMosqueList().getMosque()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mosqueRespons ->
                    //                println("Mosque ${mosqueRespons}")
                    masjid.value = mosqueRespons.data
                    masjidLoadError.value = false
                    loading.value = false
                }, { err ->
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