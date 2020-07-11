package com.example.smitch_mdns.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.InetAddress


class ScannedData (
    var serviceType: String? = null,
    var serviceName: String? = null,
    var port: Int = 0,
    var hostAddress: InetAddress? = null
)