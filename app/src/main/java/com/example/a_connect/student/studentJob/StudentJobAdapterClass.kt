package com.example.a_connect.student.studentJob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.student.studentJob.studentJobDataitem
import com.example.a_connect.databinding.StudentJobCardBinding
class StudentJobAdapterClass (private val datalist: List<studentJobDataitem>): RecyclerView.Adapter<StudentJobAdapterClass.ViewHolder>(){
    class ViewHolder(private val binding:StudentJobCardBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: studentJobDataitem){
            binding.jobRole.text=data.jobRole
            binding.companyName.text=data.companyName
            binding.companyLocation.text=data.jobLocation
            binding.time.text=data.time
            binding.Date.text=data.date
            binding.companyLogo.setImageResource(data.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=StudentJobCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }
}
