package com.example.smitch_mdns.listener

import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdServiceInfo

class MyDisCoveryListener(discovery: NetworkDiscovery?) : DiscoveryListener {
    private var discovery: NetworkDiscovery? = null

    interface NetworkDiscovery {
        fun onSeriveFound(serviceInfo: NsdServiceInfo?)
        fun discoveryStatus(message: String?)
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        discovery!!.discoveryStatus("Start discovery failed")
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        discovery!!.discoveryStatus("Stop discovery failed")
    }

    override fun onDiscoveryStarted(serviceType: String) {
        discovery!!.discoveryStatus("Discovery started")
    }

    override fun onDiscoveryStopped(serviceType: String) {
        discovery!!.discoveryStatus("Discovery stop")
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        discovery!!.onSeriveFound(serviceInfo)
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
        discovery!!.discoveryStatus("Service lost")
    }

    init {
        this.discovery = discovery
    }
}