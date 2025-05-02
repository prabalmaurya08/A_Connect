package com.example.a_connect.admin.adminHome.mvvm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R



class AlumniAdapter(
    private var alumniList: List<AlumniItem>
) : RecyclerView.Adapter<AlumniAdapter.AlumniViewHolder>() {

    inner class AlumniViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailText: TextView = itemView.findViewById(R.id.tvAlumniEmail)
        val collegeText: TextView = itemView.findViewById(R.id.tvAlumniCollege)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumniViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumni, parent, false)
        return AlumniViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumniViewHolder, position: Int) {
        val alumni = alumniList[position]
        holder.emailText.text = alumni.email
        holder.collegeText.text = alumni.college
    }

    override fun getItemCount(): Int = alumniList.size

    fun updateList(newList: List<AlumniItem>) {
        alumniList = newList
        notifyDataSetChanged()
    }
}
