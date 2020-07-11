package com.example.smitch_mdns.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smitch_mdns.R
import com.example.smitch_mdns.model.ScannedData
import java.util.*

class ScanDataAdapter(private val mContext: Context) : RecyclerView.Adapter<ScanDataAdapter.ViewHolder>() {
    private val dataList: MutableList<ScannedData> = ArrayList()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_data, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val data = dataList[i]
        viewHolder.txt_service_ip.text = "IP Address :  " + data.hostAddress
        viewHolder.txt_service_name.text = "Service Name :  " + data.serviceName
        viewHolder.txt_service_type.text = "Service Type :  " + data.serviceType
        viewHolder.txt_service_port.text = "Port:  " + data.port
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_service_name: TextView
        var txt_service_type: TextView
        var txt_service_ip: TextView
        var txt_service_port: TextView

        init {
            txt_service_port = itemView.findViewById(R.id.txt_service_port)
            txt_service_ip =   itemView.findViewById(R.id.txt_service_ip)
            txt_service_name = itemView.findViewById(R.id.txt_service_name)
            txt_service_type = itemView.findViewById(R.id.txt_service_type)
        }
    }

    fun updateList(data: ScannedData) {
        dataList.add(data)
        removeDuplicate()
        notifyDataSetChanged()
    }

    private fun removeDuplicate() {
        val treeList = TreeSet(Comparator<ScannedData> { o1, o2 -> o1.hostAddress.toString().compareTo(o2.hostAddress.toString()) })
        treeList.addAll(dataList)
        dataList.clear()
        dataList.addAll(treeList)
    }

    fun refreshAdapter() {
        dataList.clear()
        notifyDataSetChanged()
    }

}