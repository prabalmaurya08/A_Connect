package com.example.a_connect.alumini.aluminiJob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.databinding.AlumniJobCardBinding

class alumniJobAdapterClass(private val datalist: List<alumniJobDataitem>): RecyclerView.Adapter<alumniJobAdapterClass.ViewHolder>(){
    class ViewHolder(private val binding:AlumniJobCardBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:alumniJobDataitem){
            binding.alumniJobRole.text=data.jobRole
            binding.aluminiCompanyName.text=data.companyName
            binding.alumniLocation.text=data.jobLocation
            binding.aluminiTime.text=data.time
            binding.Date.text=data.date
            binding.alumniCompanyLogo.setImageResource(data.image)
        }
  }

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
}