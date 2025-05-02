package com.example.a_connect.student.studentProfile.milestone

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R

class StudentMilestoneAdapter(private val milestones: List<StudentMilestone>) : RecyclerView.Adapter<StudentMilestoneAdapter.TimelineViewHolder>() {

    // ViewHolder to hold the views for each milestone item
    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val milestoneTitle: TextView = itemView.findViewById(R.id.milestoneTitle)
        val milestoneDescription: TextView = itemView.findViewById(R.id.milestoneDescription)
        val milestoneDate: TextView = itemView.findViewById(R.id.milestoneDate)
        val milestoneTimeline: View = itemView.findViewById(R.id.timelineIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
        return TimelineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val milestone = milestones[position]
        // Log the milestone data
        Log.d("TimelineAdapter", "Binding milestone: $milestone")

        // Set data to views
        holder.milestoneTitle.text = milestone.title
        holder.milestoneDescription.text = milestone.description
        holder.milestoneDate.text = milestone.date

        // Timeline line indicator with a dynamic color (example alternating between blue/green)
        holder.milestoneTimeline.setBackgroundColor(
            if (position % 2 == 0) Color.parseColor("#6200EE") // Use purple for even position
            else Color.parseColor("#03DAC5") // Teal for odd position
        )
    }

    override fun getItemCount(): Int {
        return milestones.size
    }
}
