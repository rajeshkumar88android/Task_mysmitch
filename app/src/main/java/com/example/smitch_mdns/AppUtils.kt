package com.example.smitch_mdns

import android.content.Context
import android.net.nsd.NsdManager
import android.view.Gravity
import android.widget.Toast


object AppUtils {
    /**
     * @param mContext
     * @return
     */
    @JvmStatic
    fun initializeNSDManger(mContext: Context): NsdManager {
        return mContext.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

    /**
     * Method to show the Toast message.
     *
     * @param message String which indicates the message to be displayed as
     * Toast.
     */
    @JvmStatic
    fun showToast(context: Context?, message: String?) {
        if (context != null) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}