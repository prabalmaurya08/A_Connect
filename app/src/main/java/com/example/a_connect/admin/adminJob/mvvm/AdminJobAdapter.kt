package com.example.a_connect.admin.adminJob.mvvm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R
import com.google.android.material.card.MaterialCardView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AdminJobAdapter(
    private val onJobClick: (AdminJobDataClass) -> Unit,
    private val onJobDelete: (AdminJobDataClass) -> Unit
) : ListAdapter<AdminJobDataClass, AdminJobAdapter.JobViewHolder>(JobDiffCallback()) {

    // ViewHolder class to bind job data
    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val jobRole: TextView = itemView.findViewById(R.id.job_role)
        val companyLogo: ImageView = itemView.findViewById(R.id.company_logo)
        val companyLocation: TextView = itemView.findViewById(R.id.company_location)
        val time: TextView = itemView.findViewById(R.id.time)
        val date: TextView = itemView.findViewById(R.id.Date)
        val jobDetailButton: MaterialCardView = itemView.findViewById(R.id.job_detail_button)
        val deleteButton: CardView = itemView.findViewById(R.id.delete_button)

        fun bind(job: AdminJobDataClass) {
            Log.e("AdminJobAdapter", "Binding job: ${job.companyName}")
            companyName.text = job.companyName
            jobRole.text = job.designation
            companyLocation.text = job.location

            time.text = job.startDate?.let { formatTimeAgo(it) } ?: "N/A"
            date.text = job.endDate?.let { formatDate(it) } ?: "N/A"

            // Load company logo using Glide
            Glide.with(companyLogo.context)
                .load(job.logo)
                .placeholder(R.drawable.ic_app_icon)  // Optional: A placeholder image
                .error(R.drawable.ic_app_icon)  // Optional: An error image if loading fails
                .into(companyLogo)

            // Handle job details click
            jobDetailButton.setOnClickListener { onJobClick(job) }

            // Handle delete job click
            deleteButton.setOnClickListener { onJobDelete(job) }
        }

        // Format the time difference from the start date
        private fun formatTimeAgo(timestamp: Timestamp): String {
            val diffInMillis = System.currentTimeMillis() - timestamp.toDate().time
            val diffInHours = diffInMillis / (1000 * 60 * 60)
            return if (diffInHours < 24) {
                "$diffInHours hours ago"
            } else {
                val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                "$diffInDays days ago"
            }
        }

        // Format the date (start date or end date) as "MMMM dd, yyyy"
        private fun formatDate(timestamp: Timestamp): String {
            val date = timestamp.toDate()
            val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return sdf.format(date)
        }
    }

    // Create a new ViewHolder to bind the job data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_job_card, parent, false)
        return JobViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)

        holder.bind(job)
    }

    // DiffUtil callback to compare old and new items
    class JobDiffCallback : DiffUtil.ItemCallback<AdminJobDataClass>() {
        override fun areItemsTheSame(oldItem: AdminJobDataClass, newItem: AdminJobDataClass): Boolean {
            // Compare by job ID or any unique identifier
            return oldItem.jobId == newItem.jobId
        }

        override fun areContentsTheSame(oldItem: AdminJobDataClass, newItem: AdminJobDataClass): Boolean {
            // Compare the content of the items
            return oldItem == newItem
        }
    }
}
