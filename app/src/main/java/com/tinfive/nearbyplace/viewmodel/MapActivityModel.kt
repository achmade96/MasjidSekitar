package com.tinfive.nearbyplace.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nearbyplaces.model.MyPlaces
import com.example.nearbyplaces.model.Results
import com.tinfive.nearbyplace.networks.MasjidApi
import com.tinfive.nearbyplace.networks.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapActivityModel(application: Application): BaseViewModel(application) {
    val masjidsList = MutableLiveData<List<Results>>()
    val masjidsListError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val jsonApi: MasjidApi = RetrofitClient.instance
    private val myCompositeDisposable = CompositeDisposable()

    fun fetchData(url:String) {
        loading.value = true
        myCompositeDisposable.add(jsonApi.getNearbyPlaces(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({myPlaces -> displayData(myPlaces)} ,
                { t: Throwable -> Log.e("RxError : ","RxError : " + t.message)
                    masjidsListError.value = true
                    loading.value = false
                })
        )
    }
    private fun displayData(myPlaces: MyPlaces){

        if (myPlaces.status.equals("OK")){
            val restaurants: Array<Results>? = myPlaces.results
            masjidsList.value = restaurants!!.toList()
            masjidsListError.value = false
            loading.value = false
            (restaurants.toList())

        }else{
            masjidsListError.value = true
            loading.value = false
        }
    }
}