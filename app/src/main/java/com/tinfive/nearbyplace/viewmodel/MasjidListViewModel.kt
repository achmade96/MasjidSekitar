package com.tinfive.nearbyplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.respons.ApiRespons
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.tinfive.nearbyplace.model.Masjid as mas

class MasjidListViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<mas>>()
    val fasilitasData = MutableLiveData<List<Fasilitas>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
//    val successSubmit = MutableLiveData<ApiRespons.FilterRespons>()

    fun refresh() {
        fetchMasjid()
    }

    //GET LIST VIEW MASJID
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