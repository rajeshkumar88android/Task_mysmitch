package com.example.smitch_mdns.listener

import android.annotation.SuppressLint
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.example.smitch_mdns.model.ScannedData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MyResolvedListener(myResolveListener: MyResolveListener?) : NsdManager.ResolveListener {
    private var myResolveListener: MyResolveListener? = null
    private var observable: Observable<ScannedData>? = null

    interface MyResolveListener {
        fun onDeviceFound(data: ScannedData?)
    }

    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}

    @SuppressLint("CheckResult")
    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
        val data = ScannedData()
        data.hostAddress = serviceInfo.host
        data.port = serviceInfo.port
        data.serviceName = serviceInfo.serviceName
        data.serviceType = serviceInfo.serviceType
        myResolveListener?.onDeviceFound(data)
        observable = Observable.just(data)
        (observable as Observable<ScannedData>?)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { result -> myResolveListener?.onDeviceFound(result) }
    }

    /*fun addMyResolveListener(): Flowable<ScannedData> {
        return Flowable.create<ScannedData>({emitter ->
            setMyResolveListener(object : MyResolveListener {
                override fun onDeviceFound(data: ScannedData?) {
                    data?.let {emitter.onNext(it) }
                }
            })
        }, BackpressureStrategy.BUFFER)
    }*/

    init {
        this.myResolveListener = myResolveListener
    }
}