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

class ListViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    val masjid = MutableLiveData<List<mas>>()
    val fasilitasData = MutableLiveData<List<Fasilitas>>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val successSubmit = MutableLiveData<ApiRespons.FilterRespons>()

    fun refresh() {
        fetchMasjid()
    }

    fun loadFasilitas() {
        fetchFacilities()
    }

    //GET LIST FASILITAS
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

    //GET LIST VIEW MASJID
    private fun fetchMasjid() {
        loading.value = true
        disposable.add(
            RetrofitClient.getMosqueList().getMosque()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mosqueRespons ->
                    //                println("Mosque ${mosqueRespons}")
                    masjid.value = mosqueRespons.data.data
                    masjidLoadError.value = false
                    loading.value = false
                }, { err ->
                    masjidLoadError.value = true
                    loading.value = false
                })
        )
    }
    //get Filter Data
    fun submitFilter(full_time: String, ac: String, free_water: String, easy_access: String) {
        loading.value = true
        disposable.add(RetrofitClient.getPostFilter().filterSubmit(full_time, ac, free_water, easy_access)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filterRespons ->
                println("SUBMIT RESP ${filterRespons}")

                successSubmit.value = filterRespons
                masjidLoadError.value = false
                loading.value = false
            },{ err->
                masjidLoadError.value = true
                loading.value = false
            }))

    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}