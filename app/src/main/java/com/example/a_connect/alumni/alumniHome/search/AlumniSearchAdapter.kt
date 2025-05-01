package com.example.a_connect.alumni.alumniHome.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.databinding.AlumniSearchItemBinding


class AlumniSearchAdapter(private val onClick: (AlumniSearchDataClass)->Unit) :
    ListAdapter<AlumniSearchDataClass, AlumniSearchAdapter.AlumniViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumniViewHolder {
        val binding = AlumniSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlumniViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlumniViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlumniViewHolder(private val binding:AlumniSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alumni: AlumniSearchDataClass) {
            binding.nameTextView.text = alumni.name
            binding.graduationYearTextView.text = alumni.graduationYear.toString()
            binding.collegeTextView.text = alumni.collegeName

            binding.root.setOnClickListener {
                onClick(alumni)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlumniSearchDataClass>() {
            override fun areItemsTheSame(oldItem: AlumniSearchDataClass, newItem: AlumniSearchDataClass): Boolean {
                return oldItem.email == newItem.email
            }

            override fun areContentsTheSame(oldItem: AlumniSearchDataClass, newItem: AlumniSearchDataClass): Boolean {
                return oldItem == newItem
            }
        }
    }
}
