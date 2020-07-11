package com.example.smitch_mdns.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smitch_mdns.listener.MyResolvedListener
import com.example.smitch_mdns.model.ScannedData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {
    val mutableScannedData = MutableLiveData<ScannedData?>()

    /*fun connectDiscover() {
        compositeDisposable.add(myResolvedListener.addMyResolveListener()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    mutableScannedData.value = it
                })
    }*/

    fun updateScannedData(data: ScannedData?) {
        mutableScannedData.value = data
    }
}