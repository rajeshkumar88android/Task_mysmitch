package com.example.smitch_mdns

import android.content.Context
import android.content.SharedPreferences
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smitch_mdns.AppUtils.initializeNSDManger
import com.example.smitch_mdns.AppUtils.showToast
import com.example.smitch_mdns.MainActivity.PreferenceHelper.checkpublish
import com.example.smitch_mdns.adapters.ScanDataAdapter
import com.example.smitch_mdns.listener.MyDisCoveryListener
import com.example.smitch_mdns.listener.MyDisCoveryListener.NetworkDiscovery
import com.example.smitch_mdns.listener.MyRegistrationListener
import com.example.smitch_mdns.listener.MyRegistrationListener.MyNewtorkRegistration
import com.example.smitch_mdns.listener.MyResolvedListener
import com.example.smitch_mdns.listener.MyResolvedListener.MyResolveListener
import com.example.smitch_mdns.model.ScannedData
import com.example.smitch_mdns.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), MyNewtorkRegistration, NetworkDiscovery, MyResolveListener {

    private var mNsdManager: NsdManager? = null
    private var isServicePublished = false
    private var isDisCoveryRunning = false
    var isPublishedClicked = false
    var isScanClicked = false
    var mContext: Context? = null
    private var scanDataAdapter: ScanDataAdapter? = null
    var disCoveryListener = MyDisCoveryListener(this)
    var mRegistrationListener = MyRegistrationListener(this)
    var changeservice : Int = 0;
    val mainViewModel: MainViewModel by viewModels()
    val CUSTOM_PREF_NAME = "Smitch_data"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this@MainActivity
        mNsdManager = initializeNSDManger(this)

        var btn_scan: Button? = null
        var btn_publish: Button? = null
        var recyclerView: RecyclerView? = null
        btn_scan = findViewById(R.id.btn_scan)
        btn_publish = findViewById(R.id.btn_publish)
        recyclerView = findViewById(R.id.mRecyclerView)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        prefs.edit().clear()

        btn_scan.setOnClickListener{
            val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
            if(prefs.checkpublish.toString().equals("published")) {
                isPublishedClicked = false
                isScanClicked = true
                disCoverService()
            }else{
                Toast.makeText(this@MainActivity,"Start Publish then Scan later", Toast.LENGTH_LONG).show()
            }
        }
        btn_publish.setOnClickListener{
            val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
            prefs.checkpublish = "published"

            isPublishedClicked = true
            isScanClicked = false

            registerService(AppConstant.PORT)

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
     //   this.recyclerView.setLayoutManager(LinearLayoutManager(mContext))
        recyclerView.setHasFixedSize(true)
        scanDataAdapter = ScanDataAdapter(this)
        recyclerView.setAdapter(scanDataAdapter)
        mainViewModel.mutableScannedData.observe(this, Observer {
            it?.let {
                scanDataAdapter?.updateList(it)
            }
        })
    }

    override fun onPause() {
        if (mNsdManager != null) {
//            if (isPublishedClicked) {
            unRegisterService()
            //            }
//            if (isScanClicked) {
            stopDisCoverService()
            //            }
        }
        if (countDownTimer != null) {
            stopTimer()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mNsdManager != null) {
            if (isPublishedClicked) {
                registerService(AppConstant.PORT)
            }
            if (isScanClicked) {
                disCoverService()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Discover service
     */
    fun disCoverService() {
        if (!isDisCoveryRunning) {
            isDisCoveryRunning = true
            startTime()
            scanDataAdapter!!.refreshAdapter()
            mNsdManager!!.discoverServices(AppConstant.SERVICE_TYPE,
                    NsdManager.PROTOCOL_DNS_SD, disCoveryListener)
        }
    }

    /**
     * Stop discoverService
     */
    fun stopDisCoverService() {
        if (isDisCoveryRunning) {
            isDisCoveryRunning = false
            mNsdManager!!.stopServiceDiscovery(disCoveryListener)
        }
    }

    /**
     * Register service
     *
     * @param port
     */
    fun registerService(port: Int) {
            val serviceInfo = NsdServiceInfo()
            serviceInfo.serviceName = AppConstant.SERVICE_NAME
            serviceInfo.serviceType = AppConstant.SERVICE_TYPE
            serviceInfo.port = port
            if (!isServicePublished) {
                isServicePublished = true
                mNsdManager!!.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                        mRegistrationListener)
            }

    }

    /**
     * Unregister service
     */
    fun unRegisterService() {
        if (isServicePublished) {
            isServicePublished = false
            mNsdManager!!.unregisterService(mRegistrationListener)
        }
    }


    override fun onDeviceRegistration(message: String?) {
        showToast(mContext, message)
    }

    override fun onSeriveFound(serviceInfo: NsdServiceInfo?) {
        mNsdManager!!.resolveService(serviceInfo, MyResolvedListener(this))
    }

    override fun discoveryStatus(message: String?) {
        showToast(mContext, message)
    }

    /*override fun onDeviceFound(data: ScannedData?) {
        runOnUiThread {  }
    }*/

    var countDownTimer: CountDownTimer? = null


    fun startTime() {
       // var  btn_scan = findViewById(R.id.btn_scan)
        countDownTimer = object : CountDownTimer(10000, 1000) {
            // count down for 10seconds

            override fun onTick(millisUntilFinished: Long) {

           //     btn_scan!!.text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {
                stopDisCoverService()
             //   btn_scan!!.text = "SCAN"
            }
        }.start()
    }

    fun stopTimer() {

      //  btn_scan!!.text = "SCAN"
        countDownTimer!!.cancel()
    }

    /*class MainViewModelFactory(val myResolvedListener: MyResolvedListener) :
            ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
                MainViewModel(myResolvedListener) as T
    }*/

    override fun onDeviceFound(data: ScannedData?) {
      runOnUiThread { mainViewModel.updateScannedData(data) }
    }



    object PreferenceHelper {

        val USER_PASSWORD = "PASSWORD"

        fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

        inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
            val editMe = edit()
            operation(editMe)
            editMe.apply()
        }

        inline fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
            val key = pair.first
            val value = pair.second
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                else -> error("Only primitive types can be stored in SharedPreferences")
            }
        }



        var SharedPreferences.checkpublish
            get() = getString(USER_PASSWORD, "")
            set(value) {
                editMe {
                    //it.put(USER_PASSWORD to value)
                    it.putString(USER_PASSWORD, value)
                }
            }

        var SharedPreferences.clearValues
            get() = { }
            set(value) {
                editMe {
                    /*it.remove(USER_ID)
                    it.remove(USER_PASSWORD)*/
                    it.clear()
                }
            }
    }




}