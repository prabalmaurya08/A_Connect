package com.example.a_connect.alumni.alumniJob

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.databinding.AlumniJobCardBinding

class alumniJobAdapterClass(private val datalist: List<alumniJobDataitem>): RecyclerView.Adapter<alumniJobAdapterClass.ViewHolder>(),
    Parcelable {
    class ViewHolder(private val binding:AlumniJobCardBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:alumniJobDataitem){
            binding.jobRole.text=data.jobRole
            binding.companyName.text=data.companyName
            binding.companyLocation.text=data.jobLocation
            binding.time.text=data.time
            binding.Date.text=data.date
          binding.companyLogo.setImageResource(data.image)
        }
  }

    constructor(parcel: Parcel) : this(TODO("datalist")) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=AlumniJobCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<alumniJobAdapterClass> {
        override fun createFromParcel(parcel: Parcel): alumniJobAdapterClass {
            return alumniJobAdapterClass(parcel)
        }

        override fun newArray(size: Int): Array<alumniJobAdapterClass?> {
            return arrayOfNulls(size)
        }
    }
}