package com.tinfive.nearbyplace.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.MasjidApi
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapActivityModel(application: Application): BaseViewModel(application) {
    val masjidsList = MutableLiveData<List<Masjid>>()
    val masjidsListError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val jsonApi: MasjidApi = RetrofitClient.instance
    private val myCompositeDisposable = CompositeDisposable()

    fun fetchData(url:String) {
        loading.value = true
        myCompositeDisposable.add(jsonApi.getMarkerPlace(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({myPlaces -> displayData(myPlaces.data.data)} ,
                { t: Throwable -> Log.e("RxError : ","RxError : " + t.message)
                    masjidsListError.value = true
                    loading.value = false
                })
        )
    }
    private fun displayData(myPlaces: List<Masjid>){

        if (myPlaces.equals("OK")){
            val masjids: List<Masjid> = myPlaces
            masjidsList.value = masjids
            masjidsListError.value = false
            loading.value = false
            (masjids.toList())

        }else{
            masjidsListError.value = true
            loading.value = false
        }
    }
}