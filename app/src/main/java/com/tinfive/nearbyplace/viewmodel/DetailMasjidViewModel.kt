package com.tinfive.nearbyplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailMasjidViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    val mosquesData = MutableLiveData<Masjid>()
    val masjidLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    /*fun refresh(id:Int){
        fetchMasjid(id)
    }*/

    /*private fun fetchMasjid(id:Int) {
        loading.value = true
        println("data = $id")
        disposable.add(
            RetrofitClient.getMosqueList().getDetailMosque(id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mosqueRespons ->
                    println("MOSQUE RESP ${mosqueRespons.data}")

                    mosquesData.value = mosqueRespons.data
                    masjidLoadError.value = false
                    loading.value = false
                },{ err->
                    masjidLoadError.value = true
                    loading.value = false
                }))
    }*/
}