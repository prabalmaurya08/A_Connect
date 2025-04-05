package com.example.a_connect.alumni.alumniJob.mvvm



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


import com.example.a_connect.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale



class AlumniJobAdapter  ( private val onJobClick: (AlumniJobDataClass) -> Unit,
private val onApply: (AlumniJobDataClass) -> Unit
) : ListAdapter<AlumniJobDataClass,AlumniJobAdapter.JobViewHolder>(JobDiffCallback()) {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val jobRole: TextView = itemView.findViewById(R.id.job_role)
        val companyLogo: ImageView = itemView.findViewById(R.id.company_logo)
        val companyLocation: TextView = itemView.findViewById(R.id.company_location)
        val time: TextView = itemView.findViewById(R.id.time)
        val date: TextView = itemView.findViewById(R.id.Date)
        val jobDetailButton: MaterialCardView = itemView.findViewById(R.id.job_detail_button)
        val applyButton: MaterialCardView = itemView.findViewById(R.id.Apply_button)

      // Assuming job.logo is a URL string


        fun bind(job: AlumniJobDataClass) {
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
            applyButton.setOnClickListener { onApply(job) }
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumniJobAdapter.JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alumni_job_card, parent, false)
        return JobViewHolder(view)
    }




    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: AlumniJobAdapter.JobViewHolder, position: Int) {
        val job = getItem(position)

        holder.bind(job)
    }

    // DiffUtil callback to compare old and new items
    class JobDiffCallback : DiffUtil.ItemCallback<AlumniJobDataClass>() {
        override fun areItemsTheSame(oldItem: AlumniJobDataClass, newItem: AlumniJobDataClass): Boolean {
            // Compare by job ID or any unique identifier
            return oldItem.jobId == newItem.jobId
        }

        override fun areContentsTheSame(oldItem: AlumniJobDataClass, newItem: AlumniJobDataClass): Boolean {
            // Compare the content of the items
            return oldItem == newItem
        }
    }


}