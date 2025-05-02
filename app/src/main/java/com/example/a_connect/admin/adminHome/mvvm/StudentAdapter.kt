package com.example.a_connect.admin.adminHome.mvvm
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R

class StudentAdapter(
    private var studentList: List<StudentItem>
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailText: TextView = itemView.findViewById(R.id.tvStudentEmail)
        val collegeText: TextView = itemView.findViewById(R.id.tvStudentCollege)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.emailText.text = student.email
        holder.collegeText.text = student.college
    }

    override fun getItemCount(): Int = studentList.size

    fun updateStudentList(newList: List<StudentItem>) {
        studentList = newList
        notifyDataSetChanged()
    }
}