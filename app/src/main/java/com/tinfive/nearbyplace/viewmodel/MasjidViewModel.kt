/*
package com.tinfive.nearbyplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.respository.MosquePagedListRepository
import com.tinfive.nearbyplace.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MasjidViewModel(private val mosqueRepository : MosquePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  mosquePagedList : LiveData<PagedList<Masjid>> by lazy {
        mosqueRepository.fetchLiveMosquePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        mosqueRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return mosquePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}*/
